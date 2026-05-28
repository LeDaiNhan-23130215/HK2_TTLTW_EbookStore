package controllers;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.PasswordReset;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.UserService;
import utils.ActivityType;
import utils.HashUtil;
import utils.MailUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@WebServlet(name = "ChangePasswordController", value = "/change-password")
public class ChangePasswordController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);
    private static final String LOG_PREFIX = "[CHANGE_PASSWORD_CONTROLLER]";

    private UserService userService;
    private UserDAO userDAO;
    private PasswordResetDAO passwordResetDAO;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        userDAO = new UserDAO();
        passwordResetDAO = new PasswordResetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        boolean isOAuthOnly = (user.getPassword() == null || user.getPassword().isBlank());
        req.setAttribute("isOAuthOnly", isOAuthOnly);

        // Đồng hồ OTP nếu đang ở bước verify
        if ("verify".equals(req.getParameter("step"))) {
            Long createdAt = (Long) session.getAttribute("cpOtpCreatedAt");
            if (createdAt != null) {
                long elapsed   = (System.currentTimeMillis() - createdAt) / 1000;
                long remaining = Math.max(0, 300 - elapsed);
                req.setAttribute("otpSecondsRemaining", remaining);
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "";

        logger.debug("{} POST action='{}' userId={}", LOG_PREFIX, action, user.getId());

        switch (action) {
            case "sendOtp":      sendOtp(req, resp, user);      break;
            case "resendOtp":    resendOtp(req, resp, user);     break;
            case "verifyOtp":    verifyOtp(req, resp, user);     break;
            case "changePassword": changePassword(req, resp, user); break;
            default:
                logger.warn("{} Unknown action '{}' userId={}", LOG_PREFIX, action, user.getId());
                resp.sendRedirect(req.getContextPath() + "/change-password");
        }
    }

    private void sendOtp(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {

        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            logger.warn("{} SendOTP blocked: userId={} is locked ({})", LOG_PREFIX, user.getId(), remaining);
            resp.sendRedirect(req.getContextPath() + "/change-password?error=locked&remaining=" + remaining);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String otp     = generateOtp();
        String otpHash = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));
        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        try {
            MailUtil.sendOtp(user.getEmail(), otp, "Xác nhận đổi mật khẩu");
            logger.info("{} OTP dispatched to userId={} email={}", LOG_PREFIX, user.getId(), user.getEmail());
        } catch (Exception e) {
            logger.error("{} Failed to send OTP to userId={}: ", LOG_PREFIX, user.getId(), e);
        }

        HttpSession session = req.getSession();
        session.setAttribute("cpOtpCreatedAt", System.currentTimeMillis());
        session.setAttribute("cpLastResendTime", System.currentTimeMillis());
        session.removeAttribute("cpOtpVerified");

        resp.sendRedirect(req.getContextPath() + "/change-password?step=verify");
    }

    private void resendOtp(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(
                    passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            resp.sendRedirect(req.getContextPath()
                    + "/change-password?error=locked&remaining=" + remaining);
            return;
        }

        Long lastResend = (session != null)
                ? (Long) session.getAttribute("cpLastResendTime") : null;
        if (lastResend != null && System.currentTimeMillis() - lastResend < 30_000) {
            long cooldown = 30 - (System.currentTimeMillis() - lastResend) / 1000;
            resp.sendRedirect(req.getContextPath()
                    + "/change-password?step=verify&resendCooldown=" + cooldown);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());
        String    otp       = generateOtp();
        String    otpHash   = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(
                Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));
        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        try {
            MailUtil.sendOtp(user.getEmail(), otp, "Xác nhận đổi mật khẩu");
            logger.info("{} OTP resent to userId={}", LOG_PREFIX, user.getId());
        } catch (Exception e) {
            logger.error("{} Failed to resend OTP to userId={}: ", LOG_PREFIX, user.getId(), e);
        }

        if (session != null) {
            session.setAttribute("cpLastResendTime", System.currentTimeMillis());
            session.setAttribute("cpOtpCreatedAt",   System.currentTimeMillis());
            session.removeAttribute("cpOtpVerified");
        }

        resp.sendRedirect(req.getContextPath() + "/change-password?step=verify&resent=true");
    }

    private void verifyOtp(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session != null && Boolean.TRUE.equals(session.getAttribute("cpOtpVerified"))) {
            resp.sendRedirect(req.getContextPath() + "/change-password?step=verify&error=alreadyUsed&t=0");
            return;
        }

        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            resp.sendRedirect(req.getContextPath() + "/change-password?error=locked&remaining=" + remaining);
            return;
        }

        String code = req.getParameter("confirmCode");
        if (code == null || code.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/change-password?step=verify&error=invalidCode");
            return;
        }

        String tokenHash = HashUtil.sha256(code.trim());
        Optional<PasswordReset> opt = passwordResetDAO.findValidToken(tokenHash, user.getId());

        if (opt.isEmpty()) {
            boolean expired = passwordResetDAO.isTokenExpired(tokenHash, user.getId());
            if (expired) {
                passwordResetDAO.deleteByUser(user.getId());
                resp.sendRedirect(req.getContextPath() + "/change-password?step=verify&error=expiredCode&t=0");
                return;
            }
            boolean justLocked = passwordResetDAO.incrementAttempts(user.getId());
            if (justLocked) {
                String remaining = URLEncoder.encode(passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
                if (session != null) session.removeAttribute("cpOtpVerified");
                resp.sendRedirect(req.getContextPath() + "/change-password?error=locked&remaining=" + remaining);
                return;
            }

            if (session != null) {
                session.removeAttribute("cpLastResendTime");
            }
            long t = passwordResetDAO.getSecondsRemaining(user.getId());
            resp.sendRedirect(req.getContextPath() + "/change-password?step=verify&error=invalidCode&t=" + t);
            return;
        }

        passwordResetDAO.resetAttempts(user.getId());
        passwordResetDAO.deleteByUser(user.getId());

        if (session != null) {
            session.setAttribute("cpOtpVerified", true);
        }

        logger.info("{} OTP verified for userId={}", LOG_PREFIX, user.getId());
        resp.sendRedirect(req.getContextPath() + "/change-password?step=reset");
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session == null || !Boolean.TRUE.equals(session.getAttribute("cpOtpVerified"))) {
            logger.warn("{} changePassword blocked: OTP not verified for userId={}", LOG_PREFIX, user.getId());
            resp.sendRedirect(req.getContextPath() + "/change-password");
            return;
        }

        boolean isOAuthOnly = (user.getPassword() == null || user.getPassword().isBlank());

        String newPassword     = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");
        String oldPassword     = req.getParameter("oldPassword"); // null/empty nếu OAuth

        if (newPassword == null || newPassword.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=passwordEmpty");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=passwordMismatch");
            return;
        }
        if (!isStrongPassword(newPassword)) {
            resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=weakPassword");
            return;
        }

        if (!isOAuthOnly) {
            if (oldPassword == null || oldPassword.isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=oldPasswordEmpty");
                return;
            }
            boolean oldCorrect = userService.checkPassword(user.getId(), oldPassword);
            if (!oldCorrect) {
                logger.warn("{} Old password incorrect for userId={}", LOG_PREFIX, user.getId());
                resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=oldPasswordWrong");
                return;
            }
        }

        boolean updated = userService.changePassword(user.getId(), newPassword);
        if (!updated) {
            logger.error("{} DB update failed for userId={}", LOG_PREFIX, user.getId());
            resp.sendRedirect(req.getContextPath() + "/change-password?step=reset&error=dbError");
            return;
        }

        logger.info("{} Password changed successfully for userId={}", LOG_PREFIX, user.getId());

        try {
            MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.CHANGE_PASSWORD);
            logger.info("{} Notification email sent to userId={}", LOG_PREFIX, user.getId());
        } catch (Exception e) {
            logger.error("{} Failed to send notification email for userId={}: ", LOG_PREFIX, user.getId(), e);
        }

        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login?msg=password_changed");
    }

    private boolean isStrongPassword(String pw) {
        return pw.length() >= 10
                && pw.matches(".*[A-Z].*")
                && pw.matches(".*[a-z].*")
                && pw.matches(".*[0-9].*")
                && pw.matches(".*[^A-Za-z0-9].*");
    }

    private String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}