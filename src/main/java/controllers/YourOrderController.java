package controllers;

import DAO.CheckoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Checkout;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "YourOrderController", value = "/your-order")
public class YourOrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // 1. Check login
        if (session == null || session.getAttribute("userID") == null) {
            resp.sendRedirect("login");
            return;
        }

        int userID = (int) session.getAttribute("userID");

        // 2. Lấy đơn hàng theo user
        CheckoutDAO checkoutDAO = new CheckoutDAO();
        List<Checkout> orders = checkoutDAO.getCheckoutsByUser(userID);

        // 3. Gửi sang JSP
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/WEB-INF/views/your-order.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
