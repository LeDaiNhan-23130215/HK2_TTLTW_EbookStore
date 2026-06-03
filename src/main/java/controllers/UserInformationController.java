package controllers;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.PasswordReset;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ActivityType;
import utils.HashUtil;
import utils.MailUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@WebServlet(name = "UserInformationController", value = "/userInformation")
public class UserInformationController extends HttpServlet {

    private static final Logger logger     = LoggerFactory.getLogger(UserInformationController.class);
    private static final String LOG_PREFIX = "[USER_INFORMATION_CONTROLLER]";

    private static final String USERNAME_REGEX = "^.{3,30}$";
    private static final String PHONE_REGEX    = "^0\\d{9}$";
    private static final String EMAIL_REGEX    =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    private UserDAO          userDAO;
    private PasswordResetDAO passwordResetDAO;

    @Override
    public void init() throws ServletException {
        userDAO          = new UserDAO();
        passwordResetDAO = new PasswordResetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("verify-email".equals(req.getParameter("step"))) {
            Long createdAt = (Long) session.getAttribute("ueOtpCreatedAt");
            if (createdAt != null) {
                long elapsed   = (System.currentTimeMillis() - createdAt) / 1000;
                long remaining = Math.max(0, 300 - elapsed);
                req.setAttribute("otpSecondsRemaining", remaining);
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/user-infor.jsp").forward(req, resp);
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

        switch (action) {
            case "updateUsername": updateUsername(req, resp, user); break;
            case "updatePhone":    updatePhone(req, resp, user);    break;
            case "verifyPassword": verifyPasswordStep(req, resp, user); break;
            case "submitNewEmail": submitNewEmailStep(req, resp, user); break;
            case "verifyEmailOtp": verifyEmailOtp(req, resp, user);     break;
            case "confirmEmail":   confirmEmail(req, resp, user);       break;
            case "unlinkGoogle":   unlinkGoogle(req, resp, user);       break;
            default:
                resp.sendRedirect(req.getContextPath() + "/userInformation");
        }
    }

    private void updateUsername(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        String newUsername = trim(req.getParameter("newUsername"));

        if (isEmpty(newUsername) || !newUsername.matches(USERNAME_REGEX)) {
            setToastError(req.getSession(), "Tên người dùng phải từ 3–30 ký tự.");
            resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
        }
        if (userDAO.checkUsernameExists(newUsername)
                && !newUsername.equals(user.getUsername())) {
            setToastError(req.getSession(), "Tên người dùng đã được sử dụng.");
            resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
        }

        boolean ok = userDAO.updateUsername(user.getId(), newUsername);
        if (ok) {
            user.setUsername(newUsername);
            req.getSession().setAttribute("user",     user);
            req.getSession().setAttribute("userName", newUsername);
            try { MailUtil.sendAccountActivity(user.getEmail(), newUsername, ActivityType.CHANGE_USERNAME); }
            catch (Exception e) { logger.warn("{} Mail failed CHANGE_USERNAME userId={}", LOG_PREFIX, user.getId()); }
            req.getSession().setAttribute("toastSuccess", "✅ Cập nhật tên người dùng thành công!");
        } else {
            setToastError(req.getSession(), "Cập nhật thất bại, vui lòng thử lại.");
        }
        resp.sendRedirect(req.getContextPath() + "/userInformation");
    }

    private void updatePhone(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        String newPhone = trim(req.getParameter("newPhone"));

        if (isEmpty(newPhone) || !newPhone.matches(PHONE_REGEX)) {
            setToastError(req.getSession(), "Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0).");
            resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
        }
        if (userDAO.checkPhoneExists(newPhone) && !newPhone.equals(user.getPhoneNum())) {
            setToastError(req.getSession(), "Số điện thoại đã được sử dụng.");
            resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
        }

        boolean ok = userDAO.updatePhone(user.getId(), newPhone);
        if (ok) {
            user.setPhoneNum(newPhone);
            req.getSession().setAttribute("user",     user);
            req.getSession().setAttribute("phoneNum", newPhone);
            try { MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.CHANGE_PHONE); }
            catch (Exception e) { logger.warn("{} Mail failed CHANGE_PHONE userId={}", LOG_PREFIX, user.getId()); }
            req.getSession().setAttribute("toastSuccess", "✅ Cập nhật số điện thoại thành công!");
        } else {
            setToastError(req.getSession(), "Cập nhật thất bại, vui lòng thử lại.");
        }
        resp.sendRedirect(req.getContextPath() + "/userInformation");
    }

    private void verifyPasswordStep(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            req.getSession().setAttribute("uePasswordVerified", true);
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=new-email"); return;
        }

        String password = req.getParameter("password");
        if (isEmpty(password)) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=change-email&error=emptyPassword"); return;
        }

        boolean correct = userDAO.verifyPassword(user.getId(), password);
        if (!correct) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=change-email&error=wrongPassword"); return;
        }

        req.getSession().setAttribute("uePasswordVerified", true);
        resp.sendRedirect(req.getContextPath() + "/userInformation?step=new-email");
    }

    private void submitNewEmailStep(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        if (!Boolean.TRUE.equals(req.getSession().getAttribute("uePasswordVerified"))) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=change-email"); return;
        }

        String newEmail = trim(req.getParameter("newEmail"));
        if (isEmpty(newEmail) || !newEmail.matches(EMAIL_REGEX)) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=new-email&error=invalidEmail"); return;
        }
        if (userDAO.checkEmailExists(newEmail)) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=new-email&error=emailTaken"); return;
        }

        passwordResetDAO.deleteByUser(user.getId());
        String otp     = generateOtp();
        String otpHash = HashUtil.sha256(otp);
        Timestamp expiresAt = Timestamp.from(Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));
        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        try { MailUtil.sendOtp(newEmail, otp, "Xác thực email mới - Đổi địa chỉ email"); }
        catch (Exception e) { logger.error("{} Failed to send email OTP userId={}: ", LOG_PREFIX, user.getId(), e); }

        HttpSession session = req.getSession();
        session.setAttribute("uePendingEmail",   newEmail);
        session.setAttribute("ueOtpCreatedAt",   System.currentTimeMillis());
        session.setAttribute("ueLastResendTime", System.currentTimeMillis());
        session.removeAttribute("ueOtpVerified");

        resp.sendRedirect(req.getContextPath() + "/userInformation?step=verify-email");
    }

    private void verifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession(false);

        if ("true".equals(req.getParameter("resend"))) {
            Long lastResend = (Long) session.getAttribute("ueLastResendTime");
            if (lastResend != null && System.currentTimeMillis() - lastResend < 30_000) {
                long cooldown = 30 - (System.currentTimeMillis() - lastResend) / 1000;
                resp.sendRedirect(req.getContextPath()
                        + "/userInformation?step=verify-email&resendCooldown=" + cooldown); return;
            }
            String pendingEmail = (String) session.getAttribute("uePendingEmail");
            if (pendingEmail == null) {
                resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
            }
            passwordResetDAO.deleteByUser(user.getId());
            String otp     = generateOtp();
            String otpHash = HashUtil.sha256(otp);
            Timestamp expiresAt = Timestamp.from(Instant.now().plus(5, java.time.temporal.ChronoUnit.MINUTES));
            passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);
            try { MailUtil.sendOtp(pendingEmail, otp, "Xác thực email mới - Đổi địa chỉ email"); }
            catch (Exception e) { logger.error("{} Resend OTP failed userId={}: ", LOG_PREFIX, user.getId(), e); }
            session.setAttribute("ueLastResendTime", System.currentTimeMillis());
            session.setAttribute("ueOtpCreatedAt",   System.currentTimeMillis());
            session.removeAttribute("ueOtpVerified");
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=verify-email&resent=true"); return;
        }

        String code = trim(req.getParameter("confirmCode"));
        if (isEmpty(code)) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=verify-email&error=invalidCode"); return;
        }

        String tokenHash = HashUtil.sha256(code);
        Optional<PasswordReset> opt = passwordResetDAO.findValidToken(tokenHash, user.getId());

        if (opt.isEmpty()) {
            boolean expired = passwordResetDAO.isTokenExpired(tokenHash, user.getId());
            if (expired) {
                passwordResetDAO.deleteByUser(user.getId());
                resp.sendRedirect(req.getContextPath() + "/userInformation?step=verify-email&error=expiredCode"); return;
            }
            passwordResetDAO.incrementAttempts(user.getId());
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=verify-email&error=invalidCode"); return;
        }

        passwordResetDAO.resetAttempts(user.getId());
        passwordResetDAO.deleteByUser(user.getId());
        session.setAttribute("ueOtpVerified", true);
        resp.sendRedirect(req.getContextPath() + "/userInformation?step=confirm-email");
    }

    private void confirmEmail(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !Boolean.TRUE.equals(session.getAttribute("ueOtpVerified"))) {
            resp.sendRedirect(req.getContextPath() + "/userInformation?step=change-email"); return;
        }

        String newEmail = (String) session.getAttribute("uePendingEmail");
        if (newEmail == null) {
            resp.sendRedirect(req.getContextPath() + "/userInformation"); return;
        }

        boolean ok = userDAO.updateEmail(user.getId(), newEmail);
        if (ok) {
            String oldEmail = user.getEmail();

            try {
                MailUtil.sendAccountActivity(
                        oldEmail,
                        user.getUsername(),
                        ActivityType.CHANGE_EMAIL
                );
            } catch (Exception e) {
                logger.warn("{} Mail CHANGE_EMAIL(old) failed userId={}",
                        LOG_PREFIX, user.getId());
            }

            try {
                MailUtil.sendAccountActivity(
                        newEmail,
                        user.getUsername(),
                        ActivityType.CHANGE_EMAIL
                );
            } catch (Exception e) {
                logger.warn("{} Mail CHANGE_EMAIL(new) failed userId={}",
                        LOG_PREFIX, user.getId());
            }

            user.setEmail(newEmail);

            session.setAttribute("user", user);
            session.setAttribute("email", newEmail);

            session.removeAttribute("uePasswordVerified");
            session.removeAttribute("uePendingEmail");
            session.removeAttribute("ueOtpVerified");
            session.removeAttribute("ueOtpCreatedAt");
            session.removeAttribute("ueLastResendTime");

            session.setAttribute("toastSuccess", "✅ Đổi email thành công!");
        } else {
            session.removeAttribute("uePasswordVerified");
            session.removeAttribute("uePendingEmail");
            session.removeAttribute("ueOtpVerified");
            session.removeAttribute("ueOtpCreatedAt");
            session.removeAttribute("ueLastResendTime");
            setToastError(session, "Đổi email thất bại, vui lòng thử lại.");
        }
        resp.sendRedirect(req.getContextPath() + "/userInformation");
    }

    private void unlinkGoogle(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        boolean ok = userDAO.unlinkOAuth(user.getId());
        if (ok) {
            user.setProvider(null);
            user.setProviderId(null);
            req.getSession().setAttribute("user", user);
            try { MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.UNLINK_GOOGLE); }
            catch (Exception e) { logger.warn("{} Mail UNLINK_GOOGLE failed userId={}", LOG_PREFIX, user.getId()); }
            req.getSession().setAttribute("toastSuccess", "✅ Đã huỷ liên kết tài khoản Google.");
        } else {
            setToastError(req.getSession(), "Huỷ liên kết thất bại, vui lòng thử lại.");
        }
        resp.sendRedirect(req.getContextPath() + "/userInformation");
    }

    private String trim(String s)     { return s != null ? s.trim() : null; }
    private boolean isEmpty(String s) { return s == null || s.isEmpty(); }
    private String generateOtp()      { return String.valueOf((int)(Math.random() * 900000) + 100000); }
    private void setToastError(HttpSession session, String msg) {
        session.setAttribute("toastError", msg);
    }
}