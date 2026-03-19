package controllers;

import DTO.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Category;
import services.EbookService;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "ListbookController", value = "/list-book")
public class ListbookController extends HttpServlet {

    private final EbookService ebookService = new EbookService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TẠO MỚI filter cho mỗi request
        EbookFilterView filter = new EbookFilterView();

        // ===== PAGE =====
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // ===== FREE / PAID =====
        String freeParam = request.getParameter("free");
        if (freeParam != null && !freeParam.isEmpty()) {
            if (freeParam.equals("true")) {
                filter.setFree(true);
            } else if (freeParam.equals("false")) {
                filter.setFree(false);
            }
        } else {
            filter.setFree(null);
        }

        // ===== CATEGORY (checkbox) =====
        String[] categories = request.getParameterValues("category");
        if (categories != null && categories.length > 0) {
            filter.setCategoryId(
                    Arrays.stream(categories)
                            .map(Integer::parseInt)
                            .toList()
            );
        }

        // ===== FORMAT (checkbox) =====
        String[] formats = request.getParameterValues("format");
        if (formats != null && formats.length > 0) {
            filter.setFormats(Arrays.asList(formats));
        }

        // ===== SEARCH =====
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            filter.setKeywords(keyword);
        }

        // ===== SORTING =====
        String sortBy = request.getParameter("sortBy");
        String sortDir = request.getParameter("sortDir");

        if (sortBy != null && !sortBy.isEmpty()) {
            filter.setSortBy(sortBy);
        } else {
            filter.setSortBy("created_at"); // default sort
        }

        if (sortDir != null && !sortDir.isEmpty()) {
            filter.setSortDir(sortDir);
        } else {
            filter.setSortDir("desc"); // default direction
        }

        // ===== SIDEBAR CATEGORIES =====
        List<Category> allCategories = ebookService.getAllCategories();
        int limit = Math.min(3, allCategories.size());

        // Dùng Set<Integer> để lưu ID, tránh trùng lặp
        Set<Integer> categoryIds = new LinkedHashSet<>(); // LinkedHashSet để giữ thứ tự

        // Thêm 3 category đầu tiên
        for (int i = 0; i < limit; i++) {
            categoryIds.add(allCategories.get(i).getId());
        }

        // Thêm các category được filter (nếu có)
        if (filter.getCategoryId() != null) {
            categoryIds.addAll(filter.getCategoryId());
        }

        // Chuyển từ Set<Integer> sang List<Category>
        List<Category> sidebarCats = new ArrayList<>();
        for (Integer id : categoryIds) {
            Category c = ebookService.getCategoryById(id);
            if (c != null) {
                sidebarCats.add(c);
            }
        }

        request.setAttribute("categories", sidebarCats);

        request.setAttribute("categories", sidebarCats);
        request.setAttribute("filter", filter);

        // ===== BUILD QUERY STRINGS =====
        String queryStringForPagination = buildQueryString(request, filter, true, true);
        request.setAttribute("queryString", queryStringForPagination);

        String queryStringForSort = buildQueryString(request, filter, false, true);
        request.setAttribute("queryStringForSort", queryStringForSort);

        // ===== GET DATA =====
        PageView<EbookProductCardView> pageView = ebookService.getBooks(page, filter);

        // ===== SET ATTRIBUTES =====
        request.setAttribute("pageView", pageView);
        request.setAttribute("newEBooks", pageView.getItems());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", pageView.getTotalPages());

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

    private String buildQueryString(HttpServletRequest request,
                                    EbookFilterView filter,
                                    boolean includeSorting,
                                    boolean includeFilters) {
        StringBuilder sb = new StringBuilder();

        if (includeFilters) {
            // Free/Paid
            String freeParam = request.getParameter("free");
            if (freeParam != null && !freeParam.isEmpty()) {
                sb.append("free=").append(freeParam).append("&");
            }

            // Categories
            if (filter != null && filter.getCategoryId() != null) {
                for (Integer catId : filter.getCategoryId()) {
                    sb.append("category=").append(catId).append("&");
                }
            }

            // Formats
            String[] formats = request.getParameterValues("format");
            if (formats != null) {
                for (String fmt : formats) {
                    sb.append("format=").append(fmt).append("&");
                }
            }
        }

        if (includeSorting) {
            String sortBy = request.getParameter("sortBy");
            String sortDir = request.getParameter("sortDir");

            if (sortBy != null && !sortBy.isEmpty()) {
                sb.append("sortBy=").append(sortBy).append("&");
            }
            if (sortDir != null && !sortDir.isEmpty()) {
                sb.append("sortDir=").append(sortDir).append("&");
            }
        }

        // Search keyword
        String keyword = request.getParameter("keyword");
        if (keyword != null && !keyword.isEmpty()) {
            sb.append("keyword=").append(keyword).append("&");
        }

        // Remove trailing &
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}