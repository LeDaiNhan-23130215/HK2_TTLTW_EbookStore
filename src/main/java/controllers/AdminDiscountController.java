package controllers;

import DAO.AuthorDAO;
import DAO.CategoryDAO;
import DAO.DiscountDAO;
import DAO.EbookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Discount;
import services.DiscountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AdminDiscountController", value = "/admin-discount")
public class AdminDiscountController extends HttpServlet {

    private DiscountDAO     discountDAO;
    private EbookDAO        ebookDAO;
    private CategoryDAO     categoryDAO;
    private AuthorDAO       authorDAO;

    private static final DateTimeFormatter DT_LOCAL_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() {
        discountDAO     = new DiscountDAO();
        ebookDAO        = new EbookDAO();
        categoryDAO     = new CategoryDAO();
        authorDAO       = new AuthorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("new".equals(action)) {
            loadFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/admin-discount-form.jsp")
               .forward(req, resp);
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Discount d = discountDAO.getById(id);
            if (d == null) {
                resp.sendRedirect(req.getContextPath() + "/admin-discount");
                return;
            }
            req.setAttribute("discount",            d);
            req.setAttribute("selectedEbooks",     discountDAO.getEbookIds(id));
            req.setAttribute("selectedCategories", discountDAO.getCategoryIds(id));
            req.setAttribute("selectedAuthors",    discountDAO.getAuthorIds(id));
            loadFormData(req);
            req.getRequestDispatcher("/WEB-INF/views/admin-discount-form.jsp")
               .forward(req, resp);
            return;
        }

        if ("delete".equals(action)) {
            discountDAO.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/admin-discount");
            return;
        }

        // Mặc định: hiển thị danh sách, tự động expire trước khi load
        discountDAO.expireEnded();
        req.setAttribute("discounts", discountDAO.getAll());
        req.getRequestDispatcher("/WEB-INF/views/admin-discount.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");

        Discount d = new Discount();

        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            d.setId(Integer.parseInt(idParam));
        }

        d.setName(req.getParameter("name").trim());
        d.setDescription(req.getParameter("description"));
        d.setDiscountType(req.getParameter("discountType"));
        d.setDiscountValue(new BigDecimal(req.getParameter("discountValue")));
        d.setStatus(req.getParameter("status"));

        String startStr = req.getParameter("startDate");
        String endStr   = req.getParameter("endDate");
        if (startStr != null && !startStr.isBlank())
            d.setStartDate(LocalDateTime.parse(startStr, DT_LOCAL_FMT));
        if (endStr != null && !endStr.isBlank())
            d.setEndDate(LocalDateTime.parse(endStr, DT_LOCAL_FMT));

        int discountId;
        if (d.getId() == 0) {
            discountId = discountDAO.create(d);
        } else {
            discountDAO.update(d);
            discountId = d.getId();
        }

        discountDAO.setEbooks(discountId,     parseIds(req.getParameterValues("ebookIds")));
        discountDAO.setCategories(discountId, parseIds(req.getParameterValues("categoryIds")));
        discountDAO.setAuthors(discountId,    parseIds(req.getParameterValues("authorIds")));

        resp.sendRedirect(req.getContextPath() + "/admin-discount");
    }

    private void loadFormData(HttpServletRequest req) {
        req.setAttribute("allEbooks",     ebookDAO.findAll());
        req.setAttribute("allCategories", categoryDAO.getAllCategory());
        req.setAttribute("allAuthors",    authorDAO.findAll());
    }

    private List<Integer> parseIds(String[] values) {
        if (values == null) return Collections.emptyList();
        return Arrays.stream(values)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
