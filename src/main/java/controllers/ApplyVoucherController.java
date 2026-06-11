package controllers;

import DAO.VoucherDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Voucher;
import services.VoucherService;

import java.io.IOException;

@WebServlet(name = "ApplyVoucherController", value = "/apply-voucher")
public class ApplyVoucherController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("voucherCode");
        VoucherDAO vDAO = new VoucherDAO();
        VoucherService vService = new VoucherService();
        HttpSession session = request.getSession();
        Voucher voucher = vDAO.findByCode(code);

        double total = (double) session.getAttribute("checkoutTotal");

        String error = vService.validateVoucher(voucher, total);

        if(error != null) {
            request.setAttribute("voucherError", error);
            response.sendRedirect(request.getContextPath()+"/checkout");
            return;
        }

        double discount = vService.calculateDiscount(voucher, total);
        double finalPrice = total - discount;

        session.setAttribute("voucher", voucher);
        session.setAttribute("discount", discount);
        session.setAttribute("finalPrice", finalPrice);
        response.sendRedirect(request.getContextPath() + "/checkout");
    }
}