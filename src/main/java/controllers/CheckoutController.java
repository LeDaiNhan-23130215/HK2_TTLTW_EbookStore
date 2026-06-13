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
import services.VoucherService;
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
    private VoucherService  voucherService;
    private static final Logger logger= LoggerFactory.getLogger(CheckoutController.class);
    private static final String LOG_PREFIX= "[CHECKOUT_CONTROLLER]";


    @Override
    public void init() throws ServletException {
        cartService    = new CartService();
        checkoutService = new CheckoutService();
        voucherService = new VoucherService(); // THÊM
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

        // Access Control: kiểm tra token điều hướng 1 lần
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
        List<CartItem> cartItems = loadCartItems(userId, singleMode, bookIdParam);

        if (singleMode) {
            req.setAttribute("singleMode",   true);
            req.setAttribute("singleBookId", Integer.parseInt(bookIdParam));

            if (cartItems.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }
        } else {
            req.setAttribute("singleMode", false);
        }

        double totalPrice = cartItems.stream().mapToDouble(CartItem::getPriceAtADD).sum();

        req.setAttribute("cartItems",  cartItems);
        req.setAttribute("totalPrice", totalPrice);
        req.setAttribute("voucherCode",    null);
        req.setAttribute("discount",       null);
        req.setAttribute("finalPrice",     totalPrice);
        req.setAttribute("voucherError",   null);
        req.setAttribute("appliedVoucher", null);

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
        String step        = req.getParameter("step");
        String mode        = req.getParameter("mode");
        String bookIdParam = req.getParameter("bookId");
        String voucherCode = req.getParameter("voucherCode");
        boolean singleMode = "single".equalsIgnoreCase(mode)
                && bookIdParam != null && !bookIdParam.isBlank();

        List<CartItem> cartItems = loadCartItems(userId, singleMode, bookIdParam);

        if (cartItems == null || cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        double total = cartItems.stream().mapToDouble(CartItem::getPriceAtADD).sum();
        double  discount     = 0;
        String  voucherError = null;
        Voucher voucher      = null;

        if (voucherCode != null && !voucherCode.isBlank()) {
            voucher = new VoucherDAO().findByCode(voucherCode.trim().toUpperCase());
            String error = voucherService.validateVoucher(voucher, total, userId);
            if (error != null) {
                voucherError = error;
                voucher      = null;
            } else {
                discount = voucherService.calculateDiscount(voucher, total);
            }
        }

        double finalTotal = total - discount;

        if ("preview".equals(step)) {
            req.setAttribute("cartItems",     cartItems);
            req.setAttribute("totalPrice",    total);
            req.setAttribute("discount",      discount > 0 ? discount : null);
            req.setAttribute("finalPrice",    finalTotal);
            req.setAttribute("voucherCode",   voucherCode);
            req.setAttribute("voucherError",  voucherError);
            req.setAttribute("appliedVoucher", voucher);
            req.setAttribute("singleMode",    singleMode);
            if (singleMode) {
                req.setAttribute("singleBookId", Integer.parseInt(bookIdParam));
            }

            req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
            return;
        }
        Cart cart = cartService.getCartByUserID(userId);
        Integer cartId = (cart != null) ? cart.getId() : null;

        Integer singleBookId = singleMode ? Integer.parseInt(bookIdParam) : null;

        // Đơn hàng miễn phí: ghi thẳng DB, không qua VNPAY
        if (finalTotal == 0) {
            int pmId = checkoutService.getPMIDByName("free");
            Checkout checkout = new Checkout(userId, pmId, 0.0, "Pending");
            boolean saved = checkoutService.checkout(checkout, cartItems);

            if (!saved) {
                logger.error("{} Free order DB save failed. userId={}", LOG_PREFIX, userId);
                req.setAttribute("errorMessage", "Lỗi lưu đơn hàng. Vui lòng thử lại.");
                req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
                return;
            }

            // Ghi nhận lượt sử dụng voucher (nếu có)
            if (voucher != null) {
                try { new VoucherDAO().recordUsage(voucher.getId(), userId); } catch (Exception ignored) {}
            }

            // Cấp sách
            try {
                BookshelfService bookshelfService = new BookshelfService();
                WishlistService wishlistService = new WishlistService();
                for (CartItem item : cartItems) {
                    bookshelfService.addBookToBookshelf(userId, item.getEbook().getId());
                    wishlistService.removeFromWishlist(userId, item.getEbook().getId());
                }
                if (singleBookId != null && cartId != null) {
                    cartService.removeItem(cartId, singleBookId);
                    session.setAttribute("totalCartDetails",
                            cartService.getTotalCartDetails(cartId));
                } else if (cartId != null) {
                    cartService.clearCart(cartId);
                    session.removeAttribute("totalCartDetails");
                }
            } catch (Exception e) {
                logger.error("{} Free order post-save sync error: ", LOG_PREFIX, e);
            }

            // Gửi email
            try {
                Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
                PaymentMethod pm = pmMap.get(pmId);
                MailUtil.sendOrderConfirmation(
                        user.getEmail(), user.getUsername(),
                        checkout, cartItems, pm
                );
            } catch (Exception e) {
                logger.warn("{} Free order email send failed: {}", LOG_PREFIX, e.getMessage());
            }

            session.setAttribute("lastCheckoutId", checkout.getId());

            Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
            req.setAttribute("checkout",      checkout);
            req.setAttribute("paymentMethod", pmMap.get(pmId));
            req.setAttribute("checkoutItems", cartItems);
            req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
            return;
        }

        // ── Thanh toán qua VNPAY ──
        session.setAttribute("vnp_checkoutItems", cartItems);
        session.setAttribute("vnp_totalAmount",   (long) finalTotal);
        session.setAttribute("vnp_userId",        userId);
        session.setAttribute("vnp_cartId",        cartId);
        session.setAttribute("vnp_singleBookId",  singleBookId);

        if (voucher != null) {
            session.setAttribute("vnp_voucherId", voucher.getId());
        } else {
            session.removeAttribute("vnp_voucherId");
        }

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

    private List<CartItem> loadCartItems(int userId, boolean singleMode, String bookIdParam) {
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
        }

        return cartItems;
    }
}