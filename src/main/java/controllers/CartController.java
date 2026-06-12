package controllers;

import DAO.BookshelfDAO;
import DAO.EbookDAO;
import DTO.CartItem;
import enums.AddBookResult;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.Ebook;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CartService;
import services.DiscountResult;
import services.DiscountService;

import java.io.IOException;
import java.util.*;
import java.util.UUID;

@WebServlet(name = "CartController", value = "/cart")
public class CartController extends HttpServlet {

    private CartService cartService;
    private BookshelfDAO bookshelfDAO;
    private EbookDAO ebookDAO;
    private DiscountService discountService;
    private static final Logger logger     = LoggerFactory.getLogger(CartController.class);
    private static final String LOG_PREFIX = "[CART_CONTROLLER]";
    public static final String GUEST_CART_KEY    = "guestCart";
    public static final String CHECKOUT_TOKEN_KEY = "checkoutNavToken";

    @Override
    public void init() throws ServletException {
        cartService= new CartService();
        bookshelfDAO= new BookshelfDAO();
        ebookDAO= new EbookDAO();
        discountService= new DiscountService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            Map<Integer, Double> guestCart =
                    (Map<Integer, Double>) session.getAttribute(GUEST_CART_KEY);
            if (guestCart == null) guestCart = new LinkedHashMap<>();

            List<CartItem> guestItems = new ArrayList<>();
            for (Map.Entry<Integer, Double> e : guestCart.entrySet()) {
                Ebook ebook = ebookDAO.getEbookWithDetailsById(e.getKey());
                if (ebook != null) {
                    guestItems.add(new CartItem(0, ebook, e.getValue()));
                }
            }

            enrichCartItems(guestItems, -1, guestCart, session);

            double guestTotal = guestCart.values().stream().mapToDouble(Double::doubleValue).sum();

            req.setAttribute("guestCart",   guestCart);
            req.setAttribute("guestItems",  guestItems);
            req.setAttribute("isGuest",     true);
            req.setAttribute("totalPrice",  guestTotal);
            req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
            return;
        }

        int userId = user.getId();
        Cart cart  = getOrCreateCart(userId);
        List<CartItem> cartItems = cartService.getCartItemsByCartID(userId, cart.getId());

        enrichCartItems(cartItems, cart.getId(), null, null);

        double totalPrice = cartItems.stream().mapToDouble(CartItem::getPriceAtADD).sum();

        req.setAttribute("cart",       cart);
        req.setAttribute("cartItems",  cartItems);
        req.setAttribute("totalPrice", totalPrice);
        req.setAttribute("isGuest",    false);

        // Sinh checkout token 1 lần
        String checkoutToken = UUID.randomUUID().toString();
        session.setAttribute(CHECKOUT_TOKEN_KEY, checkoutToken);
        req.setAttribute("checkoutToken", checkoutToken);

        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User   user   = (User) session.getAttribute("user");
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            try {
                int    bookId = Integer.parseInt(req.getParameter("bookId"));
                double price  = Double.parseDouble(req.getParameter("price"));

                if (user == null) {
                    addToGuestCart(session, bookId, price);
                } else {
                    addToUserCart(session, user, bookId, price);
                }

                String toastMsg  = firstToast(session);
                String toastType = getToastType(session);
                int    count     = getCartCount(session, user);
                clearToasts(session);

                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write(
                        "{\"count\":" + count
                                + ",\"msg\":"   + jsonStr(toastMsg)
                                + ",\"type\":\"" + toastType + "\"}");
            } catch (Exception ex) {
                logger.error("{} Unexpected error in add action", LOG_PREFIX, ex);
                resp.setContentType("application/json;charset=UTF-8");
                resp.setStatus(200);
                resp.getWriter().write("{\"count\":0,\"msg\":\"⚠️ Đã xảy ra lỗi, vui lòng thử lại.\",\"type\":\"error\"}");
            }
            return;
        }

        if ("remove".equals(action)) {
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            if (user == null) {
                removeFromGuestCart(session, bookId);
            } else {
                Cart cart = getOrCreateCart(user.getId());
                cartService.removeItem(cart.getId(), bookId);
                session.setAttribute("totalCartDetails",
                        cartService.getTotalCartDetails(cart.getId()));
            }
            session.setAttribute("toastSuccess", "🗑️ Đã xoá sản phẩm khỏi giỏ hàng.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        if ("buyOne".equals(action)) {
            if (user == null) {
                int bookId = Integer.parseInt(req.getParameter("bookId"));
                saveReturnUrl(session,
                        req.getContextPath() + "/checkout?mode=single&bookId=" + bookId);
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            int bookId = Integer.parseInt(req.getParameter("bookId"));
            session.setAttribute("checkoutBookId", bookId);
            String token = UUID.randomUUID().toString();
            session.setAttribute(CHECKOUT_TOKEN_KEY, token);
            resp.sendRedirect(req.getContextPath()
                    + "/checkout?token=" + token + "&mode=single&bookId=" + bookId);
            return;
        }

        if ("checkout".equals(action)) {
            if (user == null) {
                saveReturnUrl(session, req.getContextPath() + "/checkout");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            String token = UUID.randomUUID().toString();
            session.setAttribute(CHECKOUT_TOKEN_KEY, token);
            resp.sendRedirect(req.getContextPath() + "/checkout?token=" + token);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void enrichCartItems(List<CartItem> items,
                                 int cartId,
                                 Map<Integer, Double> guestCart,
                                 HttpSession session) {
        if (items == null) return;
        for (CartItem item : items) {
            Ebook ebook = item.getEbook();
            if (ebook == null || ebook.getPrice() <= 0) continue;

            double originalPrice = ebook.getPrice();

            double currentPrice;
            String discountLabel = null;
            try {
                DiscountResult result =
                        discountService.calculateBestDiscount(ebook.getId(), originalPrice);
                currentPrice = result.getFinalPrice().doubleValue();
                if (result.hasDiscount()) {
                    discountLabel = discountService.getDiscountLabel(result.getBestDiscount());
                }
            } catch (Exception e) {
                logger.warn("{} calculateBestDiscount failed for ebookId={}, dùng giá gốc",
                        LOG_PREFIX, ebook.getId(), e);
                currentPrice = originalPrice;
            }

            double paidPrice = item.getPriceAtADD();
            if (Math.abs(currentPrice - paidPrice) > 0.001) {
                logger.info("{} Cập nhật giá ebookId={}: {} -> {}",
                        LOG_PREFIX, ebook.getId(), paidPrice, currentPrice);

                item.setPriceAtADD(currentPrice);

                if (cartId > 0) {
                    cartService.updatePrice(cartId, ebook.getId(), currentPrice);
                } else if (guestCart != null && session != null) {
                    // Guest: update session map
                    guestCart.put(ebook.getId(), currentPrice);
                    session.setAttribute(GUEST_CART_KEY, guestCart);
                }
            }

            if (currentPrice < originalPrice) {
                item.setOriginalPrice(originalPrice);
                if (discountLabel != null) {
                    item.setDiscountLabel(discountLabel);
                } else {
                    long savedPct = Math.round((1 - currentPrice / originalPrice) * 100);
                    item.setDiscountLabel("-" + savedPct + "%");
                }
            }
            else {
                item.setOriginalPrice(null);
                item.setDiscountLabel(null);
            }
        }
    }

    private void addToGuestCart(HttpSession session, int bookId, double price) {
        Map<Integer, Double> guestCart =
                (Map<Integer, Double>) session.getAttribute(GUEST_CART_KEY);
        if (guestCart == null) guestCart = new LinkedHashMap<>();

        if (guestCart.containsKey(bookId)) {
            session.setAttribute("toastWarning", "⚠️ Sách đã có trong giỏ hàng");
        } else {
            guestCart.put(bookId, price);
            session.setAttribute("toastSuccess", "✅ Đã thêm sách vào giỏ hàng");
        }
        session.setAttribute(GUEST_CART_KEY,        guestCart);
        session.setAttribute("totalCartDetails", guestCart.size());
    }

    private void removeFromGuestCart(HttpSession session, int bookId) {
        Map<Integer, Double> guestCart =
                (Map<Integer, Double>) session.getAttribute(GUEST_CART_KEY);
        if (guestCart != null) {
            guestCart.remove(bookId);
            session.setAttribute(GUEST_CART_KEY,        guestCart);
            session.setAttribute("totalCartDetails", guestCart.size());
        }
    }

    private void addToUserCart(HttpSession session, User user, int bookId, double price) {
        Cart cart = getOrCreateCart(user.getId());
        if (cart == null) {
            session.setAttribute("toastError", "⚠️ Không thể thêm vào giỏ hàng, vui lòng thử lại.");
            return;
        }
        AddBookResult result = cartService.addBookToCart(
                user.getId(), cart.getId(), bookId, price);
        switch (result) {
            case ALREADY_OWNED:
                session.setAttribute("toastError",   "📚 Bạn đã sở hữu sách này rồi"); break;
            case ALREADY_EXISTS:
                session.setAttribute("toastWarning", "⚠️ Sách đã có trong giỏ hàng");  break;
            case SUCCESS:
                session.setAttribute("toastSuccess", "✅ Đã thêm sách vào giỏ hàng");
                session.setAttribute("totalCartDetails",
                        cartService.getTotalCartDetails(cart.getId()));
                break;
        }
    }

    private Cart getOrCreateCart(int userId) {
        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }
        return cart;
    }

    public static void mergeGuestCartToUser(HttpSession session,
                                            CartService  cartService,
                                            BookshelfDAO bookshelfDAO,
                                            int          userId) {
        Map<Integer, Double> guestCart =
                (Map<Integer, Double>) session.getAttribute(GUEST_CART_KEY);
        if (guestCart == null || guestCart.isEmpty()) return;

        Cart cart = cartService.getCartByUserID(userId);
        if (cart == null) {
            cartService.createCart(userId);
            cart = cartService.getCartByUserID(userId);
        }

        for (Map.Entry<Integer, Double> e : guestCart.entrySet()) {
            cartService.addBookToCart(userId, cart.getId(),
                    e.getKey(), e.getValue());
        }

        session.removeAttribute(GUEST_CART_KEY);
        session.setAttribute("totalCartDetails",
                cartService.getTotalCartDetails(cart.getId()));
        logger.info("[CART_CONTROLLER] Merged guest cart ({} items) to userId={}",
                guestCart.size(), userId);
    }

    private int getCartCount(HttpSession session, User user) {
        if (user == null) {
            Map<Integer, Double> g =
                    (Map<Integer, Double>) session.getAttribute(GUEST_CART_KEY);
            return g == null ? 0 : g.size();
        }
        Object c = session.getAttribute("totalCartDetails");
        return c instanceof Integer ? (Integer) c : 0;
    }

    private String firstToast(HttpSession session) {
        String s = (String) session.getAttribute("toastSuccess");
        if (s != null) return s;
        s = (String) session.getAttribute("toastWarning");
        if (s != null) return s;
        return (String) session.getAttribute("toastError");
    }

    private String getToastType(HttpSession session) {
        if (session.getAttribute("toastSuccess") != null) return "success";
        if (session.getAttribute("toastWarning") != null) return "warning";
        if (session.getAttribute("toastError")   != null) return "error";
        return "info";
    }

    private void clearToasts(HttpSession session) {
        session.removeAttribute("toastSuccess");
        session.removeAttribute("toastWarning");
        session.removeAttribute("toastError");
    }

    private String jsonStr(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\"", "\\\"") + "\"";
    }

    private void saveReturnUrl(HttpSession session, String url) {
        session.setAttribute("redirectAfterLogin", url);
        session.setAttribute("toastWarning",
                "⚠️ Vui lòng đăng nhập để tiếp tục thanh toán.");
    }
}