package controllers;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.PasswordReset;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ActivityType;
import utils.HashUtil;
import utils.MailUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@WebServlet(name = "ForgotPasswordController", value = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private UserDAO userDAO;
    private PasswordResetDAO passwordResetDAO;
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);
    private static final String LOG_PREFIX = "[FORGOT_PASSWORD_CONTROLLER]";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        passwordResetDAO = new PasswordResetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("verify".equals(req.getParameter("step"))) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                Long createdAt = (Long) session.getAttribute("otpCreatedAt");
                if (createdAt != null) {
                    long elapsed   = (System.currentTimeMillis() - createdAt) / 1000;
                    long remaining = Math.max(0, 300 - elapsed);
                    req.setAttribute("otpSecondsRemaining", remaining);
                }
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            logger.warn("{} Received post request without action parameter.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        logger.debug("{} Dispatching workflow for action step: '{}'", LOG_PREFIX, action);
        switch (action) {
            case "sendCode":    sendResetCode(req, resp);  break;
            case "verifyCode":  verifyCode(req, resp);     break;
            case "resendCode":  resendCode(req, resp);     break;
            case "resetPassword": resetPassword(req, resp); break;
            default:
                logger.warn("{} Unexpected action value parameter: '{}'", LOG_PREFIX, action);
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }

    private void sendResetCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        User user = userDAO.findUserByEmail(email);

        if (user == null) {
            logger.warn("{} Reset code dispatch blocked: Email address '{}' not associated with an existing account.", LOG_PREFIX, email);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=emailNotFound");
            return;
        }

        if (passwordResetDAO.isLocked(user.getId())) {
            logger.warn("{} Reset code dispatch blocked: Account associated with User ID {} is currently locked out.", LOG_PREFIX, user.getId());
            String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=locked&remaining=" + remaining);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String otp = generateOtp();
        String otpHash = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));

        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        try {
            MailUtil.sendOtp(email, otp, "Xác thực email - Quên mật khẩu");
            logger.info("{} Password reset OTP generated and dispatched via email to user ID {}.", LOG_PREFIX, user.getId());
        } catch (Exception e) {
            logger.error("{} Severe error sending password reset OTP email to user ID {}: ", LOG_PREFIX, user.getId(), e);
        }

        HttpSession session = req.getSession();
        session.setAttribute("resetEmail", email);
        session.setAttribute("lastResendTime", System.currentTimeMillis());
        session.setAttribute("otpCreatedAt", System.currentTimeMillis());

        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify");
    }

    private void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession existingSession = req.getSession(false);
        if (existingSession != null && Boolean.TRUE.equals(existingSession.getAttribute("otpVerified"))) {
            logger.warn("{} Verification sequence halted: OTP already confirmed within the active session scope.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&error=alreadyUsed&t=0");
            return;
        }

        String code  = req.getParameter("confirmCode");
        String email = (String) req.getSession().getAttribute("resetEmail");

        if (email == null) {
            logger.warn("{} Verification aborted: Missing active reset session tracking email information.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            logger.error("{} Verification structural error: Reset email session attribute matches no active platform user account.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        if (passwordResetDAO.isLocked(user.getId())) {
            logger.warn("{} Code verification rejected: Account associated with User ID {} is locked out.", LOG_PREFIX, user.getId());
            String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&error=locked&remaining=" + remaining);
            return;
        }

        if (code == null || code.isBlank()) {
            logger.warn("{} Code verification rejected: Empty or white-space confirmation token received.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&error=invalidCode");
            return;
        }

        String tokenHash = HashUtil.sha256(code.trim());
        Optional<PasswordReset> opt = passwordResetDAO.findValidToken(tokenHash, user.getId());

        if (opt.isEmpty()) {
            boolean expired = passwordResetDAO.isTokenExpired(tokenHash, user.getId());
            if (expired) {
                logger.info("{} Verification token submitted for User ID {} has expired.", LOG_PREFIX, user.getId());
                passwordResetDAO.deleteByUser(user.getId());
                resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&error=expiredCode&t=0");
                return;
            }

            logger.warn("{} Verification failed: Invalid code hash submitted for User ID {}.", LOG_PREFIX, user.getId());
            boolean justLocked = passwordResetDAO.incrementAttempts(user.getId());
            if (justLocked) {
                logger.warn("{} SECURITY NOTICE: Max confirmation failure thresholds met. User ID {} is now locked out.", LOG_PREFIX, user.getId());
                String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
                req.getSession().invalidate();
                resp.sendRedirect(req.getContextPath() + "/forgot-password?error=locked&remaining=" + remaining);
                return;
            }

            HttpSession verifySession = req.getSession(false);
            if (verifySession != null) {
                verifySession.removeAttribute("lastResendTime");
            }

            long t = passwordResetDAO.getSecondsRemaining(user.getId());
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&error=invalidCode&t=" + t);
            return;
        }

        logger.info("{} Verification success: Token matches and confirmed for User ID {}. Clearing active validation token records.", LOG_PREFIX, user.getId());
        passwordResetDAO.resetAttempts(user.getId());
        passwordResetDAO.deleteByUser(user.getId());

        HttpSession session = req.getSession();
        session.setAttribute("resetTokenID", opt.get().getId());
        session.setAttribute("resetUserID",  opt.get().getUserID());
        session.setAttribute("otpVerified",   true);
        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset");
    }

    private void resendCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String email = session != null ? (String) session.getAttribute("resetEmail") : null;

        if (email == null) {
            logger.warn("{} Resend token processing rejected: No bound contextual active registration session metadata.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            logger.error("{} Resend token structural error: Registration reset metadata target email addresses no known account.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        if (passwordResetDAO.isLocked(user.getId())) {
            logger.warn("{} Token resend blocked: User ID {} is locked out.", LOG_PREFIX, user.getId());
            String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=locked&remaining=" + remaining);
            return;
        }

        Long lastResend = (Long) session.getAttribute("lastResendTime");
        if (lastResend != null && System.currentTimeMillis() - lastResend < 30_000) {
            long remaining = 30 - (System.currentTimeMillis() - lastResend) / 1000;
            logger.warn("{} Token resend blocked: Cooldown active for User ID {}. Remaining context: {}s", LOG_PREFIX, user.getId(), remaining);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&resendCooldown=" + remaining);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String otp = generateOtp();
        String otpHash = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));

        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        try {
            MailUtil.sendOtp(email, otp, "Xác thực email - Quên mật khẩu");
            logger.info("{} Replacement confirmation code dispatched to User ID {}.", LOG_PREFIX, user.getId());
        } catch (Exception e) {
            logger.error("{} Severe error sending replacement confirmation email code to User ID {}: ", LOG_PREFIX, user.getId(), e);
        }

        session.setAttribute("lastResendTime", System.currentTimeMillis());
        session.setAttribute("otpCreatedAt", System.currentTimeMillis());
        session.removeAttribute("otpVerified");
        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&resent=true");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("resetUserID") == null) {
            logger.warn("{} Form completion blocked: State metadata context not authorized for credential replacement.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword     = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || newPassword.isBlank()) {
            logger.warn("{} Update cancelled: Received zero length replacement input values.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=passwordEmpty");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            logger.warn("{} Update cancelled: Re-entered password text parameters mismatch.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=passwordMismatch");
            return;
        }
        if (!isStrongPassword(newPassword)) {
            logger.warn("{} Update cancelled: Replacement text fails to satisfy structural platform policy rules.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=weakPassword");
            return;
        }

        int userID  = (Integer) session.getAttribute("resetUserID");
        User user   = userDAO.getUserByID(userID);

        userDAO.updatePassword(userID, newPassword);
        logger.info("{} Core authorization modification successful for User ID {}. Inactivating verification reset session scope context.", LOG_PREFIX, userID);
        session.invalidate();

        if (user != null) {
            try {
                MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.RESET_PASSWORD);
                logger.info("{} Transaction activity summary alert dispatched to client tracking address: '{}'.", LOG_PREFIX, user.getEmail());
            } catch (Exception e) {
                logger.error("{} Severe operational alert error notifying User ID {} regarding modification: ", LOG_PREFIX, userID, e);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/login?msg=reset_success");
    }

    private boolean isStrongPassword(String pw) {
        return pw.length() >= 10
                && pw.matches(".*[A-Z].*")
                && pw.matches(".*[a-z].*")
                && pw.matches(".*[0-9].*")
                && pw.matches(".*[^A-Za-z0-9].*");
    }

    private String generateOtp() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}