package controllers;

import DAO.UserDAO;
import DTO.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.User;
import services.CartService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ContactInformationController", value = "/contact-information")
public class ContactInformationController extends HttpServlet {
    private CartService cartService;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        int userId = user.getId();

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());
        double totalPrice = 0;
        for (CartItem ci : cartItems) {
            totalPrice += ci.getPriceAtADD();
        }

        req.setAttribute("cart", cart);
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalPrice", totalPrice);

        req.getRequestDispatcher("/WEB-INF/views/contact-information.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("userAndEmail");
        String name = req.getParameter("fname");
        String phone = req.getParameter("phoneNumber");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            user.setUsername(name);
            user.setEmail(email);
            user.setPhoneNum(phone);

            boolean updated = userDAO.updateUserInfo(user);

            if (updated) {
                session.setAttribute("user", user);
            }
        }


        session.setAttribute("checkoutEmail", email);
        session.setAttribute("checkoutName", name);
        session.setAttribute("checkoutPhone", phone);

        resp.sendRedirect(req.getContextPath() + "/checkout");
    }
}
