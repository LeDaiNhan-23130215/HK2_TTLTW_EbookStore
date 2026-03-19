package controllers;

import services.AdminServices;
import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUserController", value = "/admin-user")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminUserController extends HttpServlet{
    private AdminServices adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            List<User> users = adminServices.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/WEB-INF/views/admin-user.jsp").forward(req, resp);
            return;
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            adminServices.deleteUser(id);
            resp.sendRedirect(req.getContextPath() + "/admin-user");
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            User user = adminServices.getUserById(id);
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/admin-user-edit.jsp").forward(req, resp);
            return;
        }

        List<User> users = adminServices.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("/WEB-INF/views/admin-user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String mode = req.getParameter("mode");
        if ("add".equals(action)) {
            if("manual".equals(mode)) {
                String userName = req.getParameter("userName");
                String email = req.getParameter("email");
                String phoneNum = req.getParameter("phoneNum");
                String role = req.getParameter("role");
                String password = req.getParameter("password");

                // Bắt buộc có password
                if (password == null || password.isEmpty()) {
                    req.getRequestDispatcher("/WEB-INF/views/admin-user.jsp").forward(req, resp);
                    return;
                }

                User user = new User(userName, email, phoneNum, password, role);

                adminServices.addUser(user);

                resp.sendRedirect(req.getContextPath() + "/admin-user");
                return;
            }
            else if ("import".equals(mode)) {
                Part filePart = req.getPart("file");

                if (filePart != null && filePart.getSize() > 0) {
                    adminServices.importUserFile(filePart);
                }

                resp.sendRedirect(req.getContextPath() + "/admin-user");
                return;
            }
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String userName = req.getParameter("userName");
            String email = req.getParameter("email");
            String phoneNum = req.getParameter("phoneNum");
            String role = req.getParameter("role");

            User user = new User(id);
            user.setUsername(userName);
            user.setEmail(email);
            user.setPhoneNum(phoneNum);
            user.setRole(role);
            adminServices.updateUser(user);

            resp.sendRedirect(req.getContextPath() + "/admin-user");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin-user");
    }
}
