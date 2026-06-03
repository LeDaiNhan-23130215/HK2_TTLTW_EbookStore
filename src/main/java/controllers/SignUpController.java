package controllers;

import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ActivityType;
import utils.MailUtil;

import java.io.IOException;

@WebServlet(name = "SignUpController", value = "/sign-up")
public class SignUpController extends HttpServlet {

    private UserDAO userDAO;
    private static final Logger logger     = LoggerFactory.getLogger(SignUpController.class);
    private static final String LOG_PREFIX = "[SIGN_UP_CONTROLLER]";

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String step = req.getParameter("step");

        if ("verify".equals(step) || "password".equals(step)) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("signup_email") == null) {
                logger.warn("{} Direct access blocked for step='{}': no valid signup session.", LOG_PREFIX, step);
                resp.sendRedirect(req.getContextPath() + "/sign-up");
                return;
            }
            if ("verify".equals(step)) {
                Long createdAt = (Long) session.getAttribute("signup_otp_created_at");
                if (createdAt != null) {
                    long elapsed   = (System.currentTimeMillis() - createdAt) / 1000;
                    long remaining = Math.max(0, 300 - elapsed);
                    req.setAttribute("otpSecondsRemaining", remaining);
                }
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }
        switch (action) {
            case "sendInfo":       sendInfo(req, resp);      break;
            case "verifyCode":     verifyCode(req, resp);    break;
            case "resendCode":     resendCode(req, resp);    break;
            case "createPassword": createPassword(req, resp); break;
            default:
                logger.warn("{} Unknown action: '{}'", LOG_PREFIX, action);
                resp.sendRedirect(req.getContextPath() + "/sign-up");
        }
    }

    private void sendInfo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username    = trim(req.getParameter("fname"));
        String email       = trim(req.getParameter("userAndEmail"));
        String phoneNumber = trim(req.getParameter("phoneNumber"));

        if (isEmpty(username) || isEmpty(email)) {
            forwardStep1(req, resp, "Vui lòng điền đầy đủ thông tin bắt buộc.", username, email, phoneNumber);
            return;
        }
        if (username.length() < 3) {
            forwardStep1(req, resp, "Tên người dùng phải có ít nhất 3 ký tự.", username, email, phoneNumber);
            return;
        }
        if (!email.matches(EMAIL_REGEX)) {
            logger.warn("{} Invalid email '{}'.", LOG_PREFIX, email);
            forwardStep1(req, resp, "Email không hợp lệ (vd: ten@example.com).", username, email, phoneNumber);
            return;
        }
        if (!isEmpty(phoneNumber) && !phoneNumber.matches("^0\\d{9}$")) {
            forwardStep1(req, resp, "Số điện thoại không hợp lệ (10 chữ số, bắt đầu bằng 0).", username, email, phoneNumber);
            return;
        }

        if (userDAO.checkAvailableUserNameOrEmail(username)) {
            logger.warn("{} Username '{}' already taken.", LOG_PREFIX, username);
            forwardStep1(req, resp, "Tên người dùng đã được sử dụng.", username, email, phoneNumber);
            return;
        }
        if (userDAO.checkAvailableUserNameOrEmail(email)) {
            logger.warn("{} Email '{}' already taken.", LOG_PREFIX, email);
            forwardStep1(req, resp, "Email đã được sử dụng.", username, email, phoneNumber);
            return;
        }
        if (!isEmpty(phoneNumber) && userDAO.checkPhoneExists(phoneNumber)) {
            logger.warn("{} Phone '{}' already taken.", LOG_PREFIX, phoneNumber);
            forwardStep1(req, resp, "Số điện thoại đã được sử dụng.", username, email, phoneNumber);
            return;
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        HttpSession session = req.getSession();
        session.setAttribute("signup_username",    username);
        session.setAttribute("signup_email",       email);
        session.setAttribute("signup_phoneNumber", isEmpty(phoneNumber) ? null : phoneNumber);
        session.setAttribute("signup_otp",             otp);
        session.setAttribute("signup_otp_created_at",  System.currentTimeMillis());
        session.setAttribute("signup_last_resend",     System.currentTimeMillis());
        session.removeAttribute("signup_otp_verified");

        logger.info("{} Step 1 OK for '{}' ({}). Sending OTP.", LOG_PREFIX, username, email);

        try {
            MailUtil.sendOtp(email, otp, "Xác thực email - Đăng ký tài khoản");
            logger.info("{} OTP sent to {}", LOG_PREFIX, email);
        } catch (Exception e) {
            logger.error("{} Failed to send OTP to {}: ", LOG_PREFIX, email, e);
        }

        resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify");
    }

    private void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("signup_email") == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }

        if (Boolean.TRUE.equals(session.getAttribute("signup_otp_verified"))) {
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&error=alreadyUsed&t=0");
            return;
        }

        String inputOtp    = req.getParameter("confirmCode");
        String sessionOtp  = (String) session.getAttribute("signup_otp");
        Long   createdAt   = (Long) session.getAttribute("signup_otp_created_at");
        String email       = (String) session.getAttribute("signup_email");

        if (createdAt == null || (System.currentTimeMillis() - createdAt) > 300_000) {
            logger.info("{} OTP expired for '{}'.", LOG_PREFIX, email);
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&error=expiredCode&t=0");
            return;
        }
        if (inputOtp == null || inputOtp.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&error=invalidCode");
            return;
        }
        if (!inputOtp.trim().equals(sessionOtp)) {
            logger.warn("{} Wrong OTP for '{}'.", LOG_PREFIX, email);
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&error=invalidCode");
            return;
        }

        session.setAttribute("signup_otp_verified", true);
        logger.info("{} OTP verified for '{}'. Proceeding to password step.", LOG_PREFIX, email);
        resp.sendRedirect(req.getContextPath() + "/sign-up?step=password");
    }

    private void resendCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        String email = session != null ? (String) session.getAttribute("signup_email") : null;

        if (email == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }

        Long lastResend = (Long) session.getAttribute("signup_last_resend");
        if (lastResend != null && System.currentTimeMillis() - lastResend < 30_000) {
            long remaining = 30 - (System.currentTimeMillis() - lastResend) / 1000;
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&resendCooldown=" + remaining);
            return;
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        session.setAttribute("signup_otp",             otp);
        session.setAttribute("signup_otp_created_at",  System.currentTimeMillis());
        session.setAttribute("signup_last_resend",     System.currentTimeMillis());
        session.removeAttribute("signup_otp_verified");

        try {
            MailUtil.sendOtp(email, otp, "Xác thực email - Đăng ký tài khoản");
            logger.info("{} OTP resent to {}", LOG_PREFIX, email);
        } catch (Exception e) {
            logger.error("{} Failed to resend OTP to {}: ", LOG_PREFIX, email, e);
        }

        resp.sendRedirect(req.getContextPath() + "/sign-up?step=verify&resent=true");
    }

    private void createPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null
                || session.getAttribute("signup_email") == null
                || !Boolean.TRUE.equals(session.getAttribute("signup_otp_verified"))) {
            logger.warn("{} Unauthorized access to createPassword.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }

        String newPassword     = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (newPassword == null || newPassword.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=password&error=passwordEmpty");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=password&error=passwordMismatch");
            return;
        }
        if (!isStrongPassword(newPassword)) {
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=password&error=weakPassword");
            return;
        }

        String username    = (String) session.getAttribute("signup_username");
        String email       = (String) session.getAttribute("signup_email");
        String phoneNumber = (String) session.getAttribute("signup_phoneNumber");

        logger.info("{} Creating account for '{}' ({}).", LOG_PREFIX, username, email);
        boolean isCreated = userDAO.signUp(username, email, phoneNumber, newPassword);

        if (isCreated) {
            logger.info("{} Account created successfully for '{}'.", LOG_PREFIX, username);
            try {
                MailUtil.sendAccountActivity(email, username, ActivityType.REGISTER);
                logger.info("{} Welcome mail sent to {}", LOG_PREFIX, email);
            } catch (Exception e) {
                logger.error("{} Failed to send welcome mail to {}: ", LOG_PREFIX, email, e);
            }
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            session.invalidate();
            HttpSession newSession = req.getSession();
            if (redirectAfterLogin != null && !redirectAfterLogin.isEmpty()) {
                newSession.setAttribute("redirectAfterLogin", redirectAfterLogin);
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=signup_success");
        } else {
            logger.error("{} DB insert failed for '{}'.", LOG_PREFIX, username);
            resp.sendRedirect(req.getContextPath() + "/sign-up?step=password&error=dbError");
        }
    }

    private String trim(String s)    { return s != null ? s.trim() : null; }
    private boolean isEmpty(String s){ return s == null || s.isEmpty(); }

    private boolean isStrongPassword(String pw) {
        return pw.length() >= 10
                && pw.matches(".*[A-Z].*")
                && pw.matches(".*[a-z].*")
                && pw.matches(".*[0-9].*")
                && pw.matches(".*[^A-Za-z0-9].*");
    }

    private void forwardStep1(HttpServletRequest req, HttpServletResponse resp,
                              String errorMsg, String username, String email, String phone)
            throws ServletException, IOException {
        req.setAttribute("error_msg",    errorMsg);
        req.setAttribute("fname",        username);
        req.setAttribute("userAndEmail", email);
        req.setAttribute("phoneNumber",  phone);
        req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
    }
}