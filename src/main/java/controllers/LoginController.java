package controllers;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.User;
import services.CartService;

import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private UserDAO userDAO;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String input = req.getParameter("userAndEmail");
        String password = req.getParameter("password");

        if (input == null || input.isEmpty() ||
                password == null || password.isEmpty()) {

            req.setAttribute("error_msg", "Please enter username and password");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.login(input, password);

        if (user == null) {
            req.setAttribute("error_msg", "Invalid username or password");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userID", user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("phoneNum", user.getPhoneNum());
        session.setAttribute("role", user.getRole());

        Cart cart = cartService.getCartByUserID(user.getId());
        int totalCartDetails = 0;
        if (cart != null) {
            totalCartDetails = cartService.getTotalCartDetails(cart.getId());
        }
        session.setAttribute("totalCartDetails", totalCartDetails);


        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
