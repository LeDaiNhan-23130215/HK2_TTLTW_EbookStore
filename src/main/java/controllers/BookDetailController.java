package controllers;

import DAO.EbookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Ebook;
import services.WishlistService;

import java.io.IOException;
import java.util.List;

@WebServlet("/bookdetail")
public class BookDetailController extends HttpServlet {

    private WishlistService wishlistService;
    private EbookDAO ebookDAO;

    @Override
    public void init() throws ServletException {
        wishlistService = new WishlistService();
        ebookDAO = new EbookDAO();
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

        // ===== 3. CHECK LOGIN =====
        HttpSession session = request.getSession(false);
        Integer userID = (session != null) ? (Integer) session.getAttribute("userID") : null;

        if (userID == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // ===== 4. LOAD WISHLIST =====
        List<Ebook> wishlist = wishlistService.getWishlistWithDetails(userID);
        List<Integer> wishlistIds = wishlist.stream()
                .map(Ebook::getId)
                .toList();

        // ===== 5. LOAD SIMILAR EBOOKS =====
        List<Ebook> similarEbooks = ebookDAO.getSimilarByCategory(
                ebook.getCategoryID(),
                ebook.getId(),
                4
        );

        // hydrate images + authors
        for (Ebook e : similarEbooks) {
            Ebook full = ebookDAO.getEbookWithDetailsById(e.getId());
            if (full != null) {
                e.setImages(full.getImages());
                e.setAuthors(full.getAuthors());
            }
        }

        // ===== 6. SET ATTRIBUTES =====
        request.setAttribute("ebook", ebook);
        request.setAttribute("wishlist", wishlist);
        request.setAttribute("wishlistIds", wishlistIds);
        request.setAttribute("similarEbooks", similarEbooks);

        // ===== 7. FORWARD =====
        request.getRequestDispatcher("/WEB-INF/views/bookdetail.jsp")
                .forward(request, response);
    }
}
