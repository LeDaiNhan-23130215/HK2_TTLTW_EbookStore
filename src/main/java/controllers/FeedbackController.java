package controllers;

import DAO.FeedbackDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.User;

import java.io.IOException;

@WebServlet(name = "FeedbackController", value = "/feedback")
public class FeedbackController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/feedback.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String message = request.getParameter("feedback_message");

        if (message == null || message.trim().isEmpty()) {
            request.setAttribute("msg", "Nội dung không được để trống!");
        } else {
            FeedbackDAO dao = new FeedbackDAO();
            dao.insertFeedback(user.getId(), message);
            request.setAttribute("msg", "Gửi phản hồi thành công!");
        }

        request.getRequestDispatcher("/WEB-INF/views/feedback.jsp")
                .forward(request, response);
    }
}
