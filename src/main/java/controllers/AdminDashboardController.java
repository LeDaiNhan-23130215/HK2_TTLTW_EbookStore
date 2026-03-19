package controllers;

import services.AdminServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AdminDashboardController", value = "/admin-dashboard")
public class AdminDashboardController extends HttpServlet {
    private AdminServices adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalEbooks = adminServices.getTotalEbooks();
        int totalUsers = adminServices.getTotalUsers();
        int totalOrders = adminServices.getTotalSuccessOrders();
        double totalMonthlyRevenue = adminServices.getTotalMonthlyRevenue();

        req.setAttribute("totalEbooks", totalEbooks);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalMonthlyRevenue", totalMonthlyRevenue);

        req.getRequestDispatcher("/WEB-INF/views/admin-dashboard.jsp").forward(req, resp);
    }
}
