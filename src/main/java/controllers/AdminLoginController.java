package controllers;

import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "AdminLoginController", value = "/admin-login")
public class AdminLoginController extends HttpServlet {
    private UserDAO userDAO;
    private static final Logger logger = LoggerFactory.getLogger(AdminLoginController.class);
    private static final String LOG_PREFIX = "[ADMIN_LOGIN_CONTROLLER]";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin-login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("userAndEmail");
        String password = req.getParameter("password");

        if (userName == null || userName.isEmpty() || password == null || password.isEmpty()){
            logger.warn("{} Admin authentication rejected: Missing username/email or password.", LOG_PREFIX);
            req.setAttribute("error_msg", "Please enter username and password");
            req.getRequestDispatcher("/WEB-INF/views/admin-login.jsp").forward(req,resp);
            return;
        }

        boolean isValid = userDAO.checkAdminLogin(userName, password);

        if (!isValid){
            logger.warn("{} SECURITY WARNING: Failed admin login attempt for identity: '{}'", LOG_PREFIX, userName);
            req.setAttribute("error_msg", "Invalid username or password");
            req.getRequestDispatcher("/WEB-INF/views/admin-login.jsp").forward(req,resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("admin", userName);

        logger.info("{} Access Granted: Admin session initialized for '{}'. Redirecting to /admin-dashboard.", LOG_PREFIX, userName);

        resp.sendRedirect(req.getContextPath()+"/admin-dashboard");
    }
}