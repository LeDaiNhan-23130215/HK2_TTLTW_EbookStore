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

        // Ebook đã được hydrate authors + images
        List<Ebook> wishlist = wishlistService.getWishlistWithDetails(userID);

        List<Integer> wishlistIds = wishlist.stream()
                .map(Ebook::getId)
                .toList();

        req.setAttribute("wishlist", wishlist);
        req.setAttribute("wishlistIds", wishlistIds);

        req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);

        User user =
                (User) session.getAttribute("user");
        int userID = user.getId();
        String action = req.getParameter("action");
        int ebookId = Integer.parseInt(req.getParameter("ebookId"));

        if ("add".equalsIgnoreCase(action)) {
            AddBookResult result = wishlistService.addToWishlist(userID, ebookId);

            switch (result) {
                case ALREADY_OWNED:
                    req.getSession().setAttribute(
                            "toastError",
                            "📚 Bạn đã sở hữu sách này rồi"
                    );
                    break;

                case ALREADY_EXISTS:
                    req.getSession().setAttribute(
                            "toastWarning",
                            "⚠️ Sách đã có trong giỏ hàng"
                    );
                    break;

                case SUCCESS:
                    req.getSession().setAttribute(
                            "toastSuccess",
                            "✅ Đã thêm sách vào giỏ hàng"
                    );
                    break;
            }
        } else if ("remove".equalsIgnoreCase(action)) {
            wishlistService.removeFromWishlist(userID, ebookId);
        }

        resp.sendRedirect(
                req.getHeader("Referer") != null
                        ? req.getHeader("Referer")
                        : req.getContextPath() + "/home"
        );
    }
}

