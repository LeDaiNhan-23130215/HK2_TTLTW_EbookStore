package controllers;

import DAO.EbookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Ebook;
import models.Image;
import services.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/bookdetail")
public class BookDetailController extends HttpServlet {

    private WishlistService wishlistService;
    private EbookDAO ebookDAO;
    private DiscountService discountService;
    private ImageServices imageServices;
    private BookshelfService bookshelfService;
    @Override
    public void init() throws ServletException {
        wishlistService  = new WishlistService();
        ebookDAO         = new EbookDAO();
        discountService  = new DiscountService();
        imageServices = new ImageServices();
        bookshelfService = new BookshelfService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        int ebookId;
        try {
            ebookId = Integer.parseInt(idParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        HttpSession session = request.getSession(false);
        models.User user = (session != null) ? (models.User) session.getAttribute("user") : null;
        String thumbnail = imageServices.getThumbnailByEbookId(ebookId);

        Set<Integer> ownedEbooks = (session != null) ? (Set<Integer>) session.getAttribute("ownedEbooks") : null;

        boolean isOwned = ownedEbooks != null && ownedEbooks.contains(ebookId);
        if (user != null) {
            isOwned = bookshelfService.userOwnsBook(user.getId(), ebookId);
        }
        int userID = (user != null) ? user.getId() : 0;

        List<Ebook> wishlist = wishlistService.getWishlistWithDetails(userID);
        List<Integer> wishlistIds = wishlist.stream().map(Ebook::getId).toList();

        // ===== 5. LOAD SIMILAR EBOOKS =====
        List<Ebook> similarEbooks = ebookDAO.getSimilarByCategory(
                ebook.getCategoryID(), ebook.getId(), 4);
        for (Ebook e : similarEbooks) {
            Ebook full = ebookDAO.getEbookWithDetailsById(e.getId());
            if (full != null) {
                e.setImages(full.getImages());
                e.setAuthors(full.getAuthors());
            }
        }

        DiscountResult discountResult =
                discountService.calculateBestDiscount(ebook.getId(), ebook.getPrice());

        Map<Integer, DiscountResult> similarDiscounts = new HashMap<>();
        Map<Integer, String> similarThumbnails = new HashMap<>();

        for (Ebook e : similarEbooks) {
            DiscountResult r = discountService.calculateBestDiscount(e.getId(), e.getPrice());
            if (r.hasDiscount()) {
                similarDiscounts.put(e.getId(), r);
            }
            similarThumbnails.put(e.getId(), imageServices.getThumbnailByEbookId(e.getId()));
        }

        request.setAttribute("ebook", ebook);
        request.setAttribute("thumbnail", thumbnail);
        request.setAttribute("wishlist",wishlist);
        request.setAttribute("wishlistIds",wishlistIds);
        request.setAttribute("similarEbooks",similarEbooks);
        request.setAttribute("similarThumbnails",similarThumbnails);
        request.setAttribute("discountResult",discountResult);
        request.setAttribute("discountService",discountService);
        request.setAttribute("similarDiscounts",similarDiscounts);
        request.setAttribute("isOwned", isOwned);
        // ===== 9. FORWARD =====
        request.getRequestDispatcher("/WEB-INF/views/bookdetail.jsp")
                .forward(request, response);
    }
}