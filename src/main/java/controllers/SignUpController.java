package controllers;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.MailUtil;

import java.io.IOException;

@WebServlet(name = "SignUpController", value = ("/sign-up"))
public class SignUpController extends HttpServlet {
    private UserDAO userDAO;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String LOG_PREFIX = "[LOGIN_CONTROLLER]";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("fname");
        if (username != null) username = username.trim();
        String email = req.getParameter("userAndEmail");
        if (email != null) email = email.trim();
        String phoneNumber = req.getParameter("phoneNumber");
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if(username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                phoneNumber == null || phoneNumber.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            logger.warn("{} Registration rejected: One or more fields are empty.", LOG_PREFIX);
            req.setAttribute("error_msg", "Vui lòng điền đầy đủ thông tin.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(username.length() < 3) {
            logger.warn("{} Registration rejected: Username '{}' is too short.", LOG_PREFIX, username);
            req.setAttribute("error_msg", "Tên phải có ít nhất 3 ký tự.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(emailRegex)) {
            logger.warn("{} Registration rejected: Invalid email format format for '{}'.", LOG_PREFIX, email);
            req.setAttribute("error_msg", "Email không hợp lệ.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(!phoneNumber.matches("\\d{10,11}")) {
            logger.warn("{} Registration rejected: Invalid phone number format '{}'.", LOG_PREFIX, phoneNumber);
            req.setAttribute("error_msg", "Số điện thoại không hợp lệ (10–11 số).");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";
        if(!password.matches(passwordRegex)) {
            logger.warn("{} Registration rejected: Password for user '{}' does not meet security requirements.", LOG_PREFIX, username);
            req.setAttribute("error_msg", "Mật khẩu phải có ít nhất 8 ký tự, có chữ hoa chữ thường và kí tự đặc biệt");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(!password.equals(confirmPassword)) {
            logger.warn("{} Registration rejected: Confirmation password mismatch for user '{}'.", LOG_PREFIX, username);
            req.setAttribute("error_msg", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(userDAO.checkAvailableUserNameOrEmail(username) || userDAO.checkAvailableUserNameOrEmail(email)) {
            logger.warn("{} Registration rejected: Username '{}' or Email '{}' is already taken.", LOG_PREFIX, username, email);
            req.setAttribute("error_msg", "Tên tài khoản hoặc email đã được sử dụng");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        HttpSession session = req.getSession();
        session.setAttribute("signup_username", username);
        session.setAttribute("signup_email", email);
        session.setAttribute("signup_phoneNumber", phoneNumber);
        session.setAttribute("signup_password", password);
        session.setAttribute("otp", otp);
        session.setAttribute("otp_expire", System.currentTimeMillis() + 120000);

        logger.info("{} Registration step 1 success: Session initialized for user '{}' (Email: '{}'). Generating OTP.", LOG_PREFIX, username, email);


        try {
            MailUtil.sendOtp(email, otp, "Xác thực email - Đăng ký tài khoản");
            logger.info("{} Registration OTP successfully dispatched via email to {}", LOG_PREFIX, email);
        } catch (Exception e) {
            logger.error("{} Servere error: Failed to send registratino OTP email to {}", LOG_PREFIX, email);
        }
        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }
}
