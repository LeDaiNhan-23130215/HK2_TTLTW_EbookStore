package controllers;

import DAO.VoucherDAO;
import DTO.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.BookshelfService;
import services.CartService;
import services.CheckoutService;
import services.WishlistService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    private CheckoutService checkoutService;
    private CartService cartService;

    private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);
    private static final String LOG_PREFIX = "[CHECKOUT_CONTROLLER]";

    private static final String SESSION_CHECKOUT_MODE = "checkoutMode";
    private static final String SESSION_CHECKOUT_BOOK_ID = "checkoutBookId";

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

        String mode = req.getParameter("mode");
        String bookIdParam = req.getParameter("bookId");
        boolean singleMode = "single".equalsIgnoreCase(mode) && bookIdParam != null && !bookIdParam.isBlank();

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());

        if (singleMode) {
            int singleBookId = Integer.parseInt(bookIdParam);

            cartItems = cartItems.stream()
                    .filter(ci -> ci.getEbook().getId() == singleBookId)
                    .collect(Collectors.toList());

            req.setAttribute("singleMode", true);
            req.setAttribute("singleBookId", singleBookId);

            session.setAttribute(SESSION_CHECKOUT_MODE, "single");
            session.setAttribute(SESSION_CHECKOUT_BOOK_ID, singleBookId);

            if (cartItems.isEmpty()) {
                logger.warn("{} Single checkout requested but selected book is not available in cart for User ID {}.", LOG_PREFIX, userId);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        } else {
            req.setAttribute("singleMode", false);
            req.removeAttribute("singleBookId");

            session.removeAttribute(SESSION_CHECKOUT_MODE);
            session.removeAttribute(SESSION_CHECKOUT_BOOK_ID);
        }

        double totalPrice = 0;
        for (CartItem ci : cartItems) {
            totalPrice += ci.getPriceAtADD();
        }
        session.setAttribute("checkoutTotal", totalPrice);
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

        logger.info("{} User ID {} initiated payment checkout operation sequence using channel variant '{}'.",
                LOG_PREFIX, userId, chosenPaymentMethod);

        int paymentMethodID = checkoutService.getPMIDByName(chosenPaymentMethod);

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            logger.warn("{} Unexpected State: Active cart vanished mid-transaction routing for User ID {}. Rebuilding cart record entry.",
                    LOG_PREFIX, userId);
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        Integer singleBookId = resolveSingleBookId(req, session);
        List<CartItem> allCartItems = cartService.getCartItemsByCartID(userId, cart.getId());

        List<CartItem> checkoutItems;
        if (singleBookId != null) {
            checkoutItems = allCartItems.stream()
                    .filter(ci -> ci.getEbook().getId() == singleBookId)
                    .collect(Collectors.toList());
        } else {
            checkoutItems = allCartItems;
        }

        if (checkoutItems == null || checkoutItems.isEmpty()) {
            logger.warn("{} Transaction execution rejected: Empty or zero-item collection state detected for checkout processing path on User ID {}.",
                    LOG_PREFIX, userId);
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        double totalPrice = 0;
        for (CartItem ci : checkoutItems) {
            totalPrice += ci.getPriceAtADD();
        }

        Voucher voucher = (Voucher) session.getAttribute("voucher");
        Double discount = (Double) session.getAttribute("discount");

        if (discount != null) {
            totalPrice -= discount;
        }

        Checkout checkout = new Checkout(userId, paymentMethodID, totalPrice, "Pending");

        logger.debug("{} Executing core invoice database transaction persist sequence for User ID {}. Aggregate Total: {}",
                LOG_PREFIX, userId, totalPrice);

        boolean result = checkoutService.checkout(checkout, checkoutItems);
        Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();

        if (result) {
            logger.info("{} INVOICE CONFIRMED: Payment processing transaction verified successfully for User ID {}. Total Paid: {}",
                    LOG_PREFIX, userId, totalPrice);

            if(voucher != null) {
                VoucherDAO voucherDAO = new VoucherDAO();
                voucherDAO.increaseUsedCount(voucher.getId());
            }

            try {
                BookshelfService bookshelfService = new BookshelfService();
                WishlistService wishlistService = new WishlistService();

                for (CartItem item : checkoutItems) {
                    bookshelfService.addBookToBookshelf(userId, item.getEbook().getId());
                    wishlistService.removeFromWishlist(userId, item.getEbook().getId());

                    logger.debug("{} Distributed digital assets permission access: Book ID {} pinned onto User ID {} virtual bookshelf repository.",
                            LOG_PREFIX, item.getEbook().getId(), userId);
                }

                if (singleBookId != null) {
                    cartService.removeItem(cart.getId(), singleBookId);
                    session.setAttribute("totalCartDetails", cartService.getTotalCartDetails(cart.getId()));
                    logger.debug("{} Removed only purchased bookID {} from cartID {} after single-item checkout.",
                            LOG_PREFIX, singleBookId, cart.getId());
                } else {
                    cartService.clearCart(cart.getId());
                    session.removeAttribute("totalCartDetails");
                    logger.debug("{} Cleared entire cartID {} after full-cart checkout.", LOG_PREFIX, cart.getId());
                }

                session.removeAttribute(SESSION_CHECKOUT_MODE);
                session.removeAttribute(SESSION_CHECKOUT_BOOK_ID);
                session.removeAttribute("voucher");
                session.removeAttribute("discount");
                session.removeAttribute("finalPrice");
                session.removeAttribute("checkoutTotal");

            } catch (Exception e) {
                logger.error("{} Structural failure encountered during post-payment digital asset synchronization allocations for User ID {}: ",
                        LOG_PREFIX, userId, e);
            }

            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
        } else {
            logger.warn("{} TRANSACTION RECORD DECLINED: Checkout sequence pipeline reported an external or internal core execution failure for User ID: {}. Invoice reference aggregate amount: {}",
                    LOG_PREFIX, userId, totalPrice);

            req.setAttribute("checkout", checkout);
            req.setAttribute("paymentMethod", pmMap.get(paymentMethodID));
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
        }
    }

    private Integer resolveSingleBookId(HttpServletRequest req, HttpSession session) {
        String mode = req.getParameter("mode");
        String bookIdParam = req.getParameter("bookId");

        if ("single".equalsIgnoreCase(mode) && bookIdParam != null && !bookIdParam.isBlank()) {
            return Integer.parseInt(bookIdParam);
        }

        Object sessionMode = session.getAttribute(SESSION_CHECKOUT_MODE);
        Object sessionBookId = session.getAttribute(SESSION_CHECKOUT_BOOK_ID);

        if ("single".equals(String.valueOf(sessionMode)) && sessionBookId != null) {
            if (sessionBookId instanceof Integer) {
                return (Integer) sessionBookId;
            }
            if (sessionBookId instanceof String) {
                return Integer.parseInt((String) sessionBookId);
            }
        }

        return null;
    }
}