package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import services.UserService;

import java.io.IOException;

@WebServlet(name = "ChangePasswordController", value = "/change-password")
public class ChangePasswordController extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/change-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        // 1. Check rỗng
        if (oldPassword == null || newPassword == null || confirmPassword == null ||
                oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {

            req.setAttribute("error_msg", "Vui lòng nhập đầy đủ thông tin");
            req.getRequestDispatcher("/change-password").forward(req, resp);
            return;
        }

        // 2. Check mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("error_msg", "Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/change-password").forward(req, resp);
            return;
        }

        // 3. Verify mật khẩu cũ
        boolean valid = userService.checkPassword(user.getId(), oldPassword);
        if (!valid) {
            req.setAttribute("error_msg", "Mật khẩu cũ không đúng");
            req.getRequestDispatcher("/change-password").forward(req, resp);
            return;
        }

        // 4. Update mật khẩu
        boolean updated = userService.changePassword(user.getId(), newPassword);
        if (!updated) {
            req.setAttribute("error_msg", "Đổi mật khẩu thất bại, vui lòng thử lại");
            req.getRequestDispatcher("/change-password").forward(req, resp);
            return;
        }

        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login?msg=password_changed");
    }
}

