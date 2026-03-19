package controllers;

import DAO.BannerDAO;
import DAO.CategoryDAO;
import DAO.EbookDAO;
import DTO.EbookProductCardView;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Banner;
import models.Category;
import models.Ebook;
import services.EbookService;
import services.WishlistService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "HomeController", value = "/home")
public class  HomeController extends HttpServlet {
    private EbookService ebookService;
    private WishlistService wishlistService;
    private CategoryDAO ctDAO = new CategoryDAO();
    private BannerDAO bannerDAO = new BannerDAO();
    @Override
    public void init() throws ServletException {
        ebookService = new EbookService();
        wishlistService = new WishlistService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        List<Category> randomCategory = ctDAO.getRandom5Categories();
        request.setAttribute("randomCategory", randomCategory);

        List<EbookProductCardView> newEBooks = ebookService.getNewEbookProductCards();
        request.setAttribute("newEBooks", newEBooks);

        List<EbookProductCardView> topSaleEbooks = ebookService.getTopSaleEbookProductCards();
        request.setAttribute("topSaleEBooks", topSaleEbooks);

        List<EbookProductCardView> randomEbook = ebookService.getRandomEbook();
        request.setAttribute("randomEbook", randomEbook);

        String imgRandom1 = randomEbook.get(0).getImageLink();
        request.setAttribute("rdBookimg1",imgRandom1);

        String imgRandom2 = randomEbook.get(1).getImageLink();
        request.setAttribute("rdBookimg2",imgRandom2);

        Banner imgHomeTop = bannerDAO.getBannerByLocation("HomeTop");
        request.setAttribute("homeTop", imgHomeTop);

        Banner imgHomeMiddleLeft = bannerDAO.getBannerByLocation("HomeMiddleLeft");
        request.setAttribute("HomeMiddleLeft", imgHomeMiddleLeft);

        Banner imgHomeMiddleRight = bannerDAO.getBannerByLocation("HomeMiddleRight");
        request.setAttribute("HomeMiddleRight", imgHomeMiddleRight);

        // Chỉ lấy wishlist nếu đã login
        if (session != null && session.getAttribute("userID") != null) {
            int userID = (Integer) session.getAttribute("userID");
            List<Integer> wishlistIds = wishlistService.getWishlist(userID)
                    .stream()
                    .map(Ebook::getId)
                    .toList();
            request.setAttribute("wishlistIds", wishlistIds);
        }

        request.getRequestDispatcher("/WEB-INF/views/home.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}