package controllers;

import DAO.VoucherDAO;
import DTO.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Checkout;
import models.PaymentMethod;
import models.User;
import models.Voucher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.BookshelfService;
import services.CartService;
import services.CheckoutService;
import services.WishlistService;
import utils.MailUtil;
import utils.VNPayUtil;
import utils.VNPayErrorCodeUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "VNPayReturnController", value = "/vnpay-return")
public class VNPayReturnController extends HttpServlet {

    private static final Logger logger     = LoggerFactory.getLogger(VNPayReturnController.class);
    private static final String LOG_PREFIX = "[VNPAY_RETURN]";

    private CheckoutService checkoutService;
    private CartService cartService;
    private BookshelfService bookshelfService;
    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        checkoutService= new CheckoutService();
        cartService= new CartService();
        bookshelfService= new BookshelfService();
        wishlistService= new WishlistService();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // 1. Xác minh chữ ký VNPAY
        if (!VNPayUtil.verifyHash(req.getParameterMap())) {
            logger.warn("{} Invalid hash. userId={}", LOG_PREFIX, user.getId());
            req.setAttribute("errorMessage", "Chữ ký không hợp lệ.");
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
            return;
        }

        String responseCode = req.getParameter("vnp_ResponseCode");
        String txnRef       = req.getParameter("vnp_TxnRef");
        String transNo      = req.getParameter("vnp_TransactionNo");

        logger.info("{} Return: txnRef={}, responseCode={}", LOG_PREFIX, txnRef, responseCode);

        // 2. Đọc context từ session
        List<CartItem> checkoutItems = (List<CartItem>) session.getAttribute("vnp_checkoutItems");
        Long     totalAmount  = (Long)    session.getAttribute("vnp_totalAmount");
        Integer  storedUserId = (Integer) session.getAttribute("vnp_userId");
        Integer  cartId       = (Integer) session.getAttribute("vnp_cartId");
        Integer  singleBookId = (Integer) session.getAttribute("vnp_singleBookId");

        // Dọn session VNPAY
        session.removeAttribute("vnp_checkoutItems");
        session.removeAttribute("vnp_totalAmount");
        session.removeAttribute("vnp_userId");
        session.removeAttribute("vnp_cartId");
        session.removeAttribute("vnp_singleBookId");
        session.removeAttribute("vnp_txnRef");

        if (checkoutItems == null || totalAmount == null) {
            req.setAttribute("errorMessage", "Phiên thanh toán đã hết hạn. Vui lòng thử lại.");
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
            return;
        }

        // 3. Thanh toán thất bại
        if (!"00".equals(responseCode)) {
            logger.warn("{} Payment failed. txnRef={}, code={}", LOG_PREFIX, txnRef, responseCode);
            req.setAttribute("vnpResponseCode", responseCode);
            req.setAttribute("vnpDesc", VNPayErrorCodeUtil.getDescription(responseCode));
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
            return;
        }

        // 4. Thành công → lưu DB
        int userId     = (storedUserId != null) ? storedUserId : user.getId();
        int vnpayPmId  = checkoutService.getPMIDByName("vnpay");

        Checkout checkout = new Checkout(userId, vnpayPmId, (double) totalAmount, "Pending");
        boolean saved = checkoutService.checkout(checkout, checkoutItems);

        if (!saved) {
            logger.error("{} DB save failed after VNPAY success. txnRef={}", LOG_PREFIX, txnRef);
            req.setAttribute("errorMessage", "Thanh toán thành công nhưng lỗi lưu đơn hàng. Vui lòng liên hệ hỗ trợ.");
            req.getRequestDispatcher("/WEB-INF/views/payment-fail.jsp").forward(req, resp);
            return;
        }

        // 5. Tăng used count voucher (nếu có)
        Voucher voucher = (Voucher) session.getAttribute("voucher");
        if (voucher != null) {
            try { new VoucherDAO().increaseUsedCount(voucher.getId()); } catch (Exception ignored) {}
        }

        // 6. Cấp sách + xóa cart
        try {
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
            logger.error("{} Post-payment asset sync error: ", LOG_PREFIX, e);
        }

        // 7. Dọn session checkout + voucher
        session.removeAttribute("checkoutMode");
        session.removeAttribute("checkoutBookId");
        session.removeAttribute("checkoutItems");
        session.removeAttribute("checkoutCartId");
        session.removeAttribute("checkoutTotal");
        session.removeAttribute("voucher");
        session.removeAttribute("discount");
        session.removeAttribute("finalPrice");
        session.removeAttribute("voucherError");

        // 8. Gửi email xác nhận
        try {
            Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
            PaymentMethod pm = pmMap.get(vnpayPmId);
            MailUtil.sendOrderConfirmation(
                    user.getEmail(), user.getUsername(),
                    checkout, checkoutItems, pm
            );
        } catch (Exception e) {
            logger.warn("{} Email send failed: {}", LOG_PREFIX, e.getMessage());
        }

        // 9. Đặt flag access-control cho payment-success page
        session.setAttribute("lastCheckoutId", checkout.getId());

        Map<Integer, PaymentMethod> pmMap = checkoutService.getAllPMs();
        req.setAttribute("checkout",      checkout);
        req.setAttribute("paymentMethod", pmMap.get(vnpayPmId));
        req.setAttribute("vnpTransNo",    transNo);
        req.setAttribute("checkoutItems", checkoutItems);
        req.getRequestDispatcher("/WEB-INF/views/payment-success.jsp").forward(req, resp);
    }
}
