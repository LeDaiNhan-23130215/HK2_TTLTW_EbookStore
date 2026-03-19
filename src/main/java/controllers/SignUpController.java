package controllers;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import utils.MailUtil;

import java.io.IOException;

@WebServlet(name = "SignUpController", value = ("/sign-up"))
public class SignUpController extends HttpServlet {
    private UserDAO userDAO;

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

            req.setAttribute("error_msg", "Vui lòng điền đầy đủ thông tin.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(username.length() < 3) {
            req.setAttribute("error_msg", "Tên phải có ít nhất 3 ký tự.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if(!email.matches(emailRegex)) {
            req.setAttribute("error_msg", "Email không hợp lệ.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(!phoneNumber.matches("\\d{10,11}")) {
            req.setAttribute("error_msg", "Số điện thoại không hợp lệ (10–11 số).");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }
        String passwordRegex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";
        if(!password.matches(passwordRegex)) {
            req.setAttribute("error_msg", "Mật khẩu phải có ít nhất 8 ký tự, có chữ hoa chữ thường và kí tự đặc biệt");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(!password.equals(confirmPassword)) {
            req.setAttribute("error_msg", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
            return;
        }

        if(userDAO.checkAvailableUserNameOrEmail(username) || userDAO.checkAvailableUserNameOrEmail(email)) {
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

        MailUtil.sendOtp(email, otp);

        resp.sendRedirect(req.getContextPath() + "/verify-otp");
    }
}
