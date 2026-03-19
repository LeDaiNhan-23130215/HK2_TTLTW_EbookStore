package controllers;

import DAO.ReviewDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Review;
import services.AdminServices;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/admin-review")
public class AdminReviewController extends HttpServlet {

    private ReviewDAO reviewDAO;
    private AdminServices adminServices;

    @Override
    public void init() {
        reviewDAO = new ReviewDAO();
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "list";

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            reviewDAO.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin-review");
            return;
        }

        List<Review> reviews = reviewDAO.findAll();

        // Map User
        Map<Integer, String> userMap = adminServices.getAllUsers()
                .stream()
                .collect(Collectors.toMap(
                        u -> u.getId(),
                        u -> u.getUsername()
                ));

        // Map Ebook
        Map<Integer, String> ebookMap = adminServices.findAll()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getId(),
                        e -> e.getTitle()
                ));

        req.setAttribute("reviews", reviews);
        req.setAttribute("userMap", userMap);
        req.setAttribute("ebookMap", ebookMap);

        req.getRequestDispatcher("/WEB-INF/views/admin-review.jsp")
                .forward(req, resp);
    }
}
