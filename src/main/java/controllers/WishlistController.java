package controllers;

import enums.AddBookResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Ebook;
import models.User;
import services.WishlistService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "WishlistController", value = "/wishlist")
public class WishlistController extends HttpServlet {

    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userID = (Integer) session.getAttribute("userID");

        List<Ebook> wishlist = wishlistService.getWishlistWithDetails(userID);
        List<Integer> wishlistIds = wishlist.stream().map(Ebook::getId).toList();

        req.setAttribute("wishlist",    wishlist);
        req.setAttribute("wishlistIds", wishlistIds);
        req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User   user    = (User) session.getAttribute("user");
        int    userID  = user.getId();
        String action  = req.getParameter("action");
        int    ebookId = Integer.parseInt(req.getParameter("ebookId"));

        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));

        String  msg        = null;
        String  type       = "success";
        boolean inWishlist = false;

        if ("add".equalsIgnoreCase(action)) {
            AddBookResult result = wishlistService.addToWishlist(userID, ebookId);
            switch (result) {
                case ALREADY_OWNED:
                    msg        = "📚 Bạn đã sở hữu sách này rồi";
                    type       = "error";
                    inWishlist = false;
                    break;
                case ALREADY_EXISTS:
                    msg        = "❤️ Sách đã có trong danh sách yêu thích";
                    type       = "warning";
                    inWishlist = true;
                    break;
                case SUCCESS:
                    msg        = "❤️ Đã thêm sản phẩm vào danh sách yêu thích";
                    type       = "success";
                    inWishlist = true;
                    break;
            }
        } else if ("remove".equalsIgnoreCase(action)) {
            wishlistService.removeFromWishlist(userID, ebookId);
            msg        = "💔 Đã xóa khỏi danh sách yêu thích";
            type       = "success";
            inWishlist = false;
        }

        if (isAjax) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write(
                    "{\"msg\":"        + jsonStr(msg)
                            + ",\"type\":\""     + type + "\""
                            + ",\"inWishlist\":" + inWishlist + "}");
        } else {
            if (msg != null) {
                String key = "error".equals(type)   ? "toastError"
                        : "warning".equals(type) ? "toastWarning"
                        : "toastSuccess";
                session.setAttribute(key, msg);
            }
            String referer = req.getHeader("Referer");
            resp.sendRedirect(referer != null ? referer : req.getContextPath() + "/home");
        }
    }

    private String jsonStr(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\"", "\\\"") + "\"";
    }
}