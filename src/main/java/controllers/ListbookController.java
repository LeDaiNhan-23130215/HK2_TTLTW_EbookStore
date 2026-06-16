package controllers;

import DTO.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Category;
import services.DiscountResult;
import services.DiscountService;
import services.EbookService;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "ListbookController", value = "/list-book")
public class ListbookController extends HttpServlet {

    private final EbookService ebookService = new EbookService();
    private final DiscountService discountService = new DiscountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ==== AJAX ====
        boolean isAjax = "XMLHttpRequest".equals(
                request.getHeader("X-Requested-With")
        );

        if (isAjax) {
            request.getRequestDispatcher(
                    "/WEB-INF/views/list-book-grid.jsp"
            ).forward(request, response);
        } else {
            request.getRequestDispatcher(
                    "/WEB-INF/views/list-book.jsp"
            ).forward(request, response);
        }

    }
    private void enrichWithDiscount(List<EbookProductCardView> list) {
        if (list == null) return;
        for (EbookProductCardView eb : list) {
            DiscountResult r = discountService.calculateBestDiscount(eb.getId(), eb.getPrice());
            if (r.hasDiscount()) {
                eb.applyDiscount(r.getFinalPrice(),
                        discountService.getDiscountLabel(r.getBestDiscount()));
            }
        }
    }
}