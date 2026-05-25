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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private static final String LOG_PREFIX = "[CHECKOUT_CONTROLLER]";

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
            logger.warn("{} Checkout page GET request blocked: Missing authenticated User session context.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = user.getId();
        logger.info("{} Processing checkout page initialization for User ID: {}.", LOG_PREFIX, userId);

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            logger.info("{} No active cart instance tracked for User ID {}. Dynamically instantiating empty cart entity structure.", LOG_PREFIX, userId);
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());
        double totalPrice = 0;
        for (CartItem ci : cartItems) {
            totalPrice += ci.getPriceAtADD();
        }

        logger.debug("{} User ID {} retrieved checkout items. Count: {}, Aggregate Calculated Cost: {}.", LOG_PREFIX, userId, cartItems.size(), totalPrice);

        req.setAttribute("cart", cart);
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalPrice", totalPrice);

        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            logger.error("{} Checkout processing transaction rejected: Post processing execution holds no active user session.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = user.getId();
        String chosenPaymentMethod = req.getParameter("paymentMethod");
        
        logger.info("{} User ID {} initiated payment checkout operation sequence using channel variant '{}'.", LOG_PREFIX, userId, chosenPaymentMethod);

        int paymentMethodID = checkoutService.getPMIDByName(chosenPaymentMethod);
        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            logger.warn("{} Unexpected State: Active cart vanished mid-transaction routing for User ID {}. Rebuilding cart record entry.", LOG_PREFIX, userId);
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());
        
        if (cartItems == null || cartItems.isEmpty()) {
            logger.warn("{} Transaction execution rejected: Empty or zero-item collection state detected for checkout processing path on User ID {}.", LOG_PREFIX, userId);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        double totalPrice = 0;
        for (CartItem ci : cartItems) {
            totalPrice += ci.getPriceAtADD();
        }

        Checkout checkout = new Checkout(userId, paymentMethodID, totalPrice, "Pending");
        
        logger.debug("{} Executing core invoice database transaction persist sequence for User ID {}. Aggregate Total: {}", LOG_PREFIX, userId, totalPrice);
        boolean result = checkoutService.checkout(checkout, cartItems);
        Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();

        if (result) {
            logger.info("{} INVOICE CONFIRMED: Payment processing transaction verified successfully for User ID {}. Total Paid: {}", LOG_PREFIX, userId, totalPrice);
            
            try {
                BookshelfService bookshelfService = new BookshelfService();
                for (CartItem item : cartItems) {
                    bookshelfService.addBookToBookshelf(userId, item.getEbook().getId());
                    logger.debug("{} Distributed digital assets permission access: Book ID {} pinned onto User ID {} virtual bookshelf repository.", LOG_PREFIX, item.getEbook().getId(), userId);
                }
                
                cartService.clearCart(cart.getId());
                session.removeAttribute("totalCartDetails");
                logger.debug("{} Cleared and flushed active user checkout temporary cart container state metadata for Cart ID: {}.", LOG_PREFIX, cart.getId());
            } catch (Exception e) {
                logger.error("{} Structural failure encountered during post-payment digital asset synchronization allocations for User ID {}: ", LOG_PREFIX, userId, e);
            }

            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
        } else {
            logger.warn("{} TRANSACTION RECORD DECLINED: Checkout sequence pipeline reported an external or internal core execution failure for User ID: {}. Invoice reference aggregate amount: {}", LOG_PREFIX, userId, totalPrice);
            
            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
        }
    }
}