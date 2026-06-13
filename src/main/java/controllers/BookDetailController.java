package controllers;

import DAO.EbookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Ebook;
import services.DiscountResult;
import services.DiscountService;
import services.WishlistService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/bookdetail")
public class BookDetailController extends HttpServlet {

    private WishlistService wishlistService;
    private EbookDAO ebookDAO;
    private DiscountService discountService;

    @Override
    public void init() throws ServletException {
        wishlistService  = new WishlistService();
        ebookDAO         = new EbookDAO();
        discountService  = new DiscountService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ===== 1. VALIDATE ID =====
        String idParam = request.getParameter("id");
        int ebookId;
        try {
            ebookId = Integer.parseInt(idParam);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // ===== 2. LOAD EBOOK DETAIL =====
        Ebook ebook = ebookDAO.getEbookById(ebookId);
        if (ebook == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // ===== 3. GET USER =====
        HttpSession session = request.getSession(false);
        int userID = ((models.User) session.getAttribute("user")).getId();

        // ===== 4. LOAD WISHLIST =====
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

        // ===== 6. DISCOUNT: ebook chính =====
        DiscountResult discountResult =
                discountService.calculateBestDiscount(ebook.getId(), ebook.getPrice());

        // ===== 7. DISCOUNT: similar ebooks =====
        Map<Integer, DiscountResult> similarDiscounts = new HashMap<>();
        for (Ebook e : similarEbooks) {
            DiscountResult r = discountService.calculateBestDiscount(e.getId(), e.getPrice());
            if (r.hasDiscount()) {
                similarDiscounts.put(e.getId(), r);
            }
        }

        // ===== 8. SET ATTRIBUTES =====
        request.setAttribute("ebook",ebook);
        request.setAttribute("wishlist",wishlist);
        request.setAttribute("wishlistIds",wishlistIds);
        request.setAttribute("similarEbooks",similarEbooks);
        request.setAttribute("discountResult",discountResult);
        request.setAttribute("discountService",discountService);
        request.setAttribute("similarDiscounts",similarDiscounts);

        // ===== 9. FORWARD =====
        request.getRequestDispatcher("/WEB-INF/views/bookdetail.jsp")
                .forward(request, response);
    }
}