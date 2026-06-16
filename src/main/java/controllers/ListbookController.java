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

        int page = parsePage(request);
        EbookFilterView filter = buildFilter(request);
        PageView<EbookProductCardView> pageView = ebookService.getBooks(page, filter);

        enrichWithDiscount(pageView.getItems());

        List<Category> categories = ebookService.getAllCategories();
        List<Category> sidebarCategories = getTopThree(categories);

        String queryString = buildQueryString(filter);
        request.setAttribute("queryString", queryString);

        request.setAttribute("pageView", pageView);
        request.setAttribute("filter", filter);
        request.setAttribute("categories", categories);
        request.setAttribute("sidebarCategories", sidebarCategories);
        request.setAttribute("currentPage", pageView.getCurrentPage());
        request.setAttribute("totalPages", pageView.getTotalPages());

        boolean isAjax = "XMLHttpRequest".equals(
                request.getHeader("X-Requested-With")
                );

        if (isAjax) {
            request.getRequestDispatcher("/WEB-INF/views/list-book-grid.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/list-book.jsp").forward(request, response);
        }
    }

    public List<Category> getTopThree(List<Category> categories) {
        int count = 0;
        List<Category> result = new ArrayList<>();

        for(Category cate : categories) {
            if(count == 3) {
                break;
            }
            result.add(cate);
            count++;
        }
        return result;
    }
    private int parsePage(HttpServletRequest request) {
        try {
            return Integer.parseInt(
                    Optional.ofNullable(
                            request.getParameter("page")
                    ).orElse("1")
            );
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private EbookFilterView buildFilter(HttpServletRequest request) {

        EbookFilterView filter =
                new EbookFilterView();

        String freeParam = request.getParameter("free");

        if (freeParam != null && !freeParam.isBlank()) {
            filter.setFree(Boolean.parseBoolean(freeParam)
            );
        }

        String[] categories = request.getParameterValues("category");

        if (categories != null && categories.length > 0) {
            List<Integer> categoryList = new ArrayList<>();

            for(String cate : categories) {
                categoryList.add(Integer.parseInt(cate));
            }
            filter.setCategoryId(categoryList);
        }

        String[] formats = request.getParameterValues("format");

        if (formats != null && formats.length > 0) {
            filter.setFormats(Arrays.asList(formats));
        }

        String keyword = request.getParameter("keyword");

        if (keyword != null && !keyword.isBlank()) {
            filter.setKeywords(keyword);
        }

        String sortBy = request.getParameter(("sortBy"));
        sortBy = (sortBy != null) ? sortBy : "id";
        filter.setSortBy(sortBy);

        String sortDir = request.getParameter("sortDir");
        sortDir = (sortDir != null) ? sortDir : "desc";

        filter.setSortDir(sortDir);
        return filter;
    }

    private String buildQueryString(EbookFilterView filter) {
        StringBuilder sb = new StringBuilder();
        if (filter.getKeywords() != null && !filter.getKeywords().isBlank())
            sb.append("&keyword=").append(filter.getKeywords());
        if (filter.getFree() != null)
            sb.append("&free=").append(filter.getFree());
        if (filter.getCategoryId() != null)
            for (Integer id : filter.getCategoryId())
                sb.append("&category=").append(id);
        if (filter.getFormats() != null)
            for (String fmt : filter.getFormats())
                sb.append("&format=").append(fmt);
        return sb.toString().replaceFirst("^&", "");
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