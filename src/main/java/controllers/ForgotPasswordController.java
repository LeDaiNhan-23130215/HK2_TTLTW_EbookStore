package controllers;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.PasswordReset;
import models.User;
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

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        passwordResetDAO = new PasswordResetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        switch (action) {
            case "sendCode":    sendResetCode(req, resp);  break;
            case "verifyCode":  verifyCode(req, resp);     break;
            case "resendCode":  resendCode(req, resp);     break;
            case "resetPassword": resetPassword(req, resp); break;
            default: resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }

    private void sendResetCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        User user = userDAO.findUserByEmail(email);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=emailNotFound");
            return;
        }

        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(
                    passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");

            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?error=locked&remaining=" + remaining);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String otp = generateOtp();
        String otpHash = HashUtil.sha256(otp);

        Timestamp expiresAt =
                Timestamp.from(Instant.now().plus(15, java.time.temporal.ChronoUnit.MINUTES));

        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        MailUtil.sendOtp(email, otp);

        HttpSession session = req.getSession();
        session.setAttribute("resetEmail", email);

        resp.sendRedirect(req.getContextPath()
                + "/forgot-password?step=verify");
    }

    private void verifyCode(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String code  = req.getParameter("confirmCode");
        String email = (String) req.getSession().getAttribute("resetEmail");

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        // Kiểm tra đang bị khoá trước khi làm gì khác
        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(
                    passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?step=verify&error=locked&remaining=" + remaining);
            return;
        }

        // Chưa nhập mã
        if (code == null || code.isBlank()) {
            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?step=verify&error=invalidCode");
            return;
        }

        String tokenHash = HashUtil.sha256(code.trim());
        Optional<PasswordReset> opt = passwordResetDAO.findValidToken(tokenHash, user.getId());

        if (opt.isEmpty()) {
            // Phân biệt hết hạn vs sai mã
            boolean expired = passwordResetDAO.isTokenExpired(tokenHash, user.getId());
            if (expired) {
                passwordResetDAO.deleteByUser(user.getId());
                resp.sendRedirect(req.getContextPath()
                        + "/forgot-password?step=verify&error=expiredCode&t=0");
                return;
            }

            // Sai mã → tăng attempts, trả true nếu lần này vừa đủ 5 → khoá
            boolean justLocked = passwordResetDAO.incrementAttempts(user.getId());
            if (justLocked) {
                String remaining = URLEncoder.encode(
                        passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
                req.getSession().invalidate();
                resp.sendRedirect(req.getContextPath()
                        + "/forgot-password?error=locked&remaining=" + remaining);
                return;
            }

            long t = passwordResetDAO.getSecondsRemaining(user.getId());
            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?step=verify&error=invalidCode&t=" + t);
            return;
        }

        // Đúng mã → reset attempts, tiếp tục
        passwordResetDAO.resetAttempts(user.getId());
        HttpSession session = req.getSession();
        session.setAttribute("resetTokenID", opt.get().getId());
        session.setAttribute("resetUserID",  opt.get().getUserID());
        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset");
    }

    private void resendCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String email = session != null ? (String) session.getAttribute("resetEmail") : null;

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        // Không cho gửi lại khi đang bị khoá
        if (passwordResetDAO.isLocked(user.getId())) {
            String remaining = URLEncoder.encode(
                    passwordResetDAO.getLockRemainingTime(user.getId()), "UTF-8");
            session.invalidate();
            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?error=locked&remaining=" + remaining);
            return;
        }

        // Cooldown 30 giây
        Long lastResend = (Long) session.getAttribute("lastResendTime");
        if (lastResend != null && System.currentTimeMillis() - lastResend < 30_000) {
            long remaining = 30 - (System.currentTimeMillis() - lastResend) / 1000;
            resp.sendRedirect(req.getContextPath()
                    + "/forgot-password?step=verify&resendCooldown=" + remaining);
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String    otp       = generateOtp();
        String    otpHash   = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(15, java.time.temporal.ChronoUnit.MINUTES));

        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);
        MailUtil.sendOtp(email, otp);

        session.setAttribute("lastResendTime", System.currentTimeMillis());
        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify&resent=true");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("resetUserID") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword     = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || newPassword.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=passwordEmpty");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=passwordMismatch");
            return;
        }
        if (!isStrongPassword(newPassword)) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset&error=weakPassword");
            return;
        }

        int userID  = (Integer) session.getAttribute("resetUserID");
        int tokenId = (Integer) session.getAttribute("resetTokenID");
        User user   = userDAO.getUserByID(userID);

        userDAO.updatePassword(userID, newPassword);
        passwordResetDAO.markTokenUsed(tokenId);
        session.invalidate();

        if (user != null) {
            MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.RESET_PASSWORD);
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