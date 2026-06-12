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
import utils.MailUtil;
import utils.VNPayUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "CheckoutController", value = "/checkout")
public class CheckoutController extends HttpServlet {

    private CheckoutService checkoutService;
    private CartService     cartService;

    private static final Logger logger= LoggerFactory.getLogger(CheckoutController.class);
    private static final String LOG_PREFIX= "[CHECKOUT_CONTROLLER]";

    private static final String SESSION_CHECKOUT_MODE= "checkoutMode";
    private static final String SESSION_CHECKOUT_BOOK_ID= "checkoutBookId";

    @Override
    public void init() throws ServletException {
        cartService = new CartService();
        checkoutService = new CheckoutService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // ── Access Control: kiểm tra token điều hướng 1 lần ──
        String tokenParam = req.getParameter("token");
        String sessionToken = (String) session.getAttribute(CartController.CHECKOUT_TOKEN_KEY);

        boolean validToken = tokenParam != null
                && sessionToken != null
                && tokenParam.equals(sessionToken);

        if (!validToken) {
            logger.warn("{} Checkout blocked: invalid/missing nav token for userId={}", LOG_PREFIX, user.getId());
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        // Token hợp lệ → xóa ngay (single-use)
        session.removeAttribute(CartController.CHECKOUT_TOKEN_KEY);

        int userId = user.getId();

        String mode = req.getParameter("mode");
        String bookIdParam = req.getParameter("bookId");
        boolean singleMode = "single".equalsIgnoreCase(mode)
                && bookIdParam != null && !bookIdParam.isBlank();

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

            req.setAttribute("singleMode",   true);
            req.setAttribute("singleBookId", singleBookId);
            session.setAttribute(SESSION_CHECKOUT_MODE,    "single");
            session.setAttribute(SESSION_CHECKOUT_BOOK_ID, singleBookId);

            if (cartItems.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        } else {
            req.setAttribute("singleMode", false);
            session.removeAttribute(SESSION_CHECKOUT_MODE);
            session.removeAttribute(SESSION_CHECKOUT_BOOK_ID);
        }

        double totalPrice = cartItems.stream().mapToDouble(CartItem::getPriceAtADD).sum();
        session.setAttribute("checkoutTotal", totalPrice);

        session.setAttribute("checkoutItems",  cartItems);
        session.setAttribute("checkoutCartId", cart.getId());

        req.setAttribute("cart",      cart);
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("totalPrice", totalPrice);

        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = user.getId();

        List<CartItem> checkoutItems =
                (List<CartItem>) session.getAttribute("checkoutItems");
        Integer cartId = (Integer) session.getAttribute("checkoutCartId");

        if (checkoutItems == null || checkoutItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        double rawTotal = checkoutItems.stream().mapToDouble(CartItem::getPriceAtADD).sum();
        Double discount = (Double) session.getAttribute("discount");
        double finalTotal = (discount != null) ? Math.max(0, rawTotal - discount) : rawTotal;

        Integer singleBookId = resolveSingleBookId(req, session);

        // Đơn hàng miễn phí: ghi thẳng DB, không qua VNPAY
        if (finalTotal == 0) {
            int pmId = checkoutService.getPMIDByName("free");
            Checkout checkout = new Checkout(userId, pmId, 0.0, "Pending");
            boolean saved = checkoutService.checkout(checkout, checkoutItems);

            if (!saved) {
                logger.error("{} Free order DB save failed. userId={}", LOG_PREFIX, userId);
                req.setAttribute("errorMessage", "Lỗi lưu đơn hàng. Vui lòng thử lại.");
                req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
                return;
            }

            // Tăng used count voucher nếu có
            Voucher voucher = (Voucher) session.getAttribute("voucher");
            if (voucher != null) {
                try { new VoucherDAO().increaseUsedCount(voucher.getId()); } catch (Exception ignored) {}
            }

            // Cấp sách
            try {
                BookshelfService bookshelfService = new BookshelfService();
                WishlistService wishlistService = new WishlistService();
                for (CartItem item : checkoutItems) {
                    bookshelfService.addBookToBookshelf(userId, item.getEbook().getId());
                    wishlistService.removeFromWishlist(userId, item.getEbook().getId());
                }
                if (singleBookId != null) {
                    cartService.removeItem(cartId, singleBookId);
                    session.setAttribute("totalCartDetails",
                            cartService.getTotalCartDetails(cartId));
                } else {
                    cartService.clearCart(cartId);
                    session.removeAttribute("totalCartDetails");
                }
            } catch (Exception e) {
                logger.error("{} Free order post-save sync error: ", LOG_PREFIX, e);
            }

            // Dọn session
            session.removeAttribute("checkoutMode");
            session.removeAttribute("checkoutBookId");
            session.removeAttribute("checkoutItems");
            session.removeAttribute("checkoutCartId");
            session.removeAttribute("checkoutTotal");
            session.removeAttribute("voucher");
            session.removeAttribute("discount");
            session.removeAttribute("finalPrice");
            session.removeAttribute("voucherError");

            // Gửi email
            try {
                Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
                PaymentMethod pm = pmMap.get(pmId);
                MailUtil.sendOrderConfirmation(
                        user.getEmail(), user.getUsername(),
                        checkout, checkoutItems, pm
                );
            } catch (Exception e) {
                logger.warn("{} Free order email send failed: {}", LOG_PREFIX, e.getMessage());
            }

            session.setAttribute("lastCheckoutId", checkout.getId());

            Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
            req.setAttribute("checkout",      checkout);
            req.setAttribute("paymentMethod", pmMap.get(pmId));
            req.setAttribute("checkoutItems", checkoutItems);
            req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
            return;
        }

        // ── Thanh toán qua VNPAY ──
        session.setAttribute("vnp_checkoutItems", checkoutItems);
        session.setAttribute("vnp_totalAmount",   (long) finalTotal);
        session.setAttribute("vnp_userId",        userId);
        session.setAttribute("vnp_cartId",        cartId);
        session.setAttribute("vnp_singleBookId",  singleBookId);

        String txnRef = VNPayUtil.generateTxnRef();
        session.setAttribute("vnp_txnRef", txnRef);

        String orderInfo = "Thanh toan EbookStore " + txnRef;

        String paymentUrl = VNPayUtil.buildPaymentUrl(
                (long) finalTotal,
                txnRef,
                orderInfo,
                VNPayUtil.getClientIp(req)
        );

        logger.info("{} Redirecting userId={} to VNPAY. txnRef={}, amount={}",
                LOG_PREFIX, userId, txnRef, (long) finalTotal);
        resp.sendRedirect(paymentUrl);
    }

    private Integer resolveSingleBookId(HttpServletRequest req, HttpSession session) {
        String mode        = req.getParameter("mode");
        String bookIdParam = req.getParameter("bookId");
        if ("single".equalsIgnoreCase(mode) && bookIdParam != null && !bookIdParam.isBlank())
            return Integer.parseInt(bookIdParam);

        Object sessionMode   = session.getAttribute(SESSION_CHECKOUT_MODE);
        Object sessionBookId = session.getAttribute(SESSION_CHECKOUT_BOOK_ID);
        if ("single".equals(String.valueOf(sessionMode)) && sessionBookId != null) {
            if (sessionBookId instanceof Integer) return (Integer) sessionBookId;
            if (sessionBookId instanceof String)  return Integer.parseInt((String) sessionBookId);
        }
        return null;
    }
}