package controllers;

import services.AdminServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import DTO.FeedbackAdminView;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "AdminFeedbackController", value = "/admin-feedback")
public class AdminFeedbackController extends HttpServlet {
    private AdminServices adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            List<FeedbackAdminView> feedbacks = adminServices.getFeedbackWithUser();
            req.setAttribute("feedbacks", feedbacks);
            req.getRequestDispatcher("/WEB-INF/views/admin-feedback.jsp").forward(req, resp);
            return;
        }

        // DELETE
        if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            adminServices.deleteFeedback(id);
            resp.sendRedirect(req.getContextPath() + "/admin-feedback");
            return;
        }

        //VIEW
        if(action.equals("view")){
            int id = Integer.parseInt(req.getParameter("id"));

            adminServices.markFeedbackAsRead(id);

            FeedbackAdminView feedback = adminServices.getFeedbackWithUserById(id);
            req.setAttribute("feedback", feedback);

            req.getRequestDispatcher("/WEB-INF/views/admin-feedback-view.jsp")
                    .forward(req, resp);
            return;
        }

        // Default: list
        List<FeedbackAdminView> feedbacks = adminServices.getFeedbackWithUser();
        req.setAttribute("feedbacks", feedbacks);
        req.getRequestDispatcher("/WEB-INF/views/admin-feedback.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/admin-feedback");
            return;
        }
    }
}
