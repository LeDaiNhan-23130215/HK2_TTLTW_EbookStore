package controllers;

import DTO.FeedbackAdminView;
import DTO.PaymentAdminView;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.AdminServices;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminPaymentController", value = "/admin-payment")
public class AdminPaymentController extends HttpServlet{
    private AdminServices adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            List<PaymentAdminView> payments = adminServices.getAllPayments();
            req.setAttribute("payments", payments);
            req.getRequestDispatcher("/WEB-INF/views/admin-payment.jsp").forward(req, resp);
            return;
        }

        //VIEW
        if(action.equals("view")){
            int id = Integer.parseInt(req.getParameter("id"));

            PaymentAdminView payment = adminServices.getPaymentByUserID(id);
            req.setAttribute("payment", payment);

            req.getRequestDispatcher("/WEB-INF/views/admin-payment-view.jsp")
                    .forward(req, resp);
            return;
        }

        // Default: list
        List<PaymentAdminView> payments = adminServices.getAllPayments();
        req.setAttribute("payments", payments);
        req.getRequestDispatcher("/WEB-INF/views/admin-payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/admin-payment");
            return;
        }
    }
}
