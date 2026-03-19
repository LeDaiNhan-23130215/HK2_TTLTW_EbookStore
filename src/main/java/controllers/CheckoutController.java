package controllers;

import DTO.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.Checkout;
import models.PaymentMethod;
import models.User;
import services.BookshelfService;
import services.CartService;
import services.CheckoutService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {
    private CheckoutService checkoutService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
        checkoutService = new CheckoutService();
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

        req.getRequestDispatcher("/WEB-INF/views/payment.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login");
            return;
        }

        int paymentMethodID = checkoutService.getPMIDByName(req.getParameter("paymentMethod"));
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

        Checkout checkout = new Checkout(userId, paymentMethodID, totalPrice, "Pending");
        boolean result = checkoutService.checkout(checkout, cartItems);
        Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
        if (result) {
            BookshelfService bookshelfService = new BookshelfService();
            for (CartItem item : cartItems) {
                bookshelfService.addBookToBookshelf(userId, item.getEbook().getId());
            }
            cartService.clearCart(cart.getId());
            session.removeAttribute("totalCartDetails");
            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
        } else {
            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
        }
    }
}
