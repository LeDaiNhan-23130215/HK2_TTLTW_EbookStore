package controllers;

import DAO.VoucherDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Voucher;
import services.VoucherService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "AdminVoucherController", value = "/admin-voucher")
public class AdminVoucherController extends HttpServlet {

    private VoucherDAO     voucherDAO;
    private VoucherService voucherService;

    private static final DateTimeFormatter DT_LOCAL_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    public void init() {
        voucherDAO     = new VoucherDAO();
        voucherService = new VoucherService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        // ── Sinh mã 6 chữ số ngẫu nhiên (AJAX) ──
        if ("generateCode".equals(action)) {
            resp.setContentType("text/plain; charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.write(voucherService.generateUniqueCode());
            }
            return;
        }

        if ("new".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/views/admin-voucher-form.jsp")
                    .forward(req, resp);
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Voucher v = voucherDAO.findById(id);
            if (v == null) {
                resp.sendRedirect(req.getContextPath() + "/admin-voucher");
                return;
            }
            req.setAttribute("voucher", v);
            req.getRequestDispatcher("/WEB-INF/views/admin-voucher-form.jsp")
                    .forward(req, resp);
            return;
        }

        if ("delete".equals(action)) {
            voucherDAO.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect(req.getContextPath() + "/admin-voucher");
            return;
        }

        if ("toggle".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Voucher v = voucherDAO.findById(id);
            if (v != null) {
                v.setActive(!v.isActive());
                voucherDAO.update(v);
            }
            resp.sendRedirect(req.getContextPath() + "/admin-voucher");
            return;
        }

        // Mặc định: hiển thị danh sách
        req.setAttribute("vouchers", voucherDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/admin-voucher.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String idParam = req.getParameter("id");
        boolean isEdit = idParam != null && !idParam.isBlank();

        String code = req.getParameter("code");
        if (code == null || code.isBlank()) {
            code = voucherService.generateUniqueCode();
        } else {
            code = code.trim().toUpperCase();
        }

        // ── Kiểm tra trùng mã ──
        boolean duplicate = isEdit
                ? voucherDAO.existsByCodeExcluding(code, Integer.parseInt(idParam))
                : voucherDAO.existsByCode(code);

        String endStr = req.getParameter("expiredAt");
        if (duplicate || endStr == null || endStr.isBlank()) {
            String error = duplicate
                    ? "Mã giảm giá \"" + code + "\" đã tồn tại. Vui lòng chọn mã khác."
                    : "Vui lòng chọn thời điểm kết thúc của mã giảm giá.";
            req.setAttribute("errorMessage", error);
            Voucher v = new Voucher(isEdit ? Integer.parseInt(idParam) : 0);
            populateFromRequest(req, v, code);
            req.setAttribute("voucher", v);
            req.getRequestDispatcher("/WEB-INF/views/admin-voucher-form.jsp")
                    .forward(req, resp);
            return;
        }

        Voucher v = new Voucher(isEdit ? Integer.parseInt(idParam) : 0);
        populateFromRequest(req, v, code);

        if (isEdit) {
            // Giữ nguyên used_count khi sửa
            Voucher old = voucherDAO.findById(v.getId());
            if (old != null) v.setUsedCount(old.getUsedCount());
            voucherDAO.update(v);
        } else {
            v.setUsedCount(0);
            voucherDAO.insert(v);
        }

        resp.sendRedirect(req.getContextPath() + "/admin-voucher");
    }

    private void populateFromRequest(HttpServletRequest req, Voucher v, String code) {
        v.setCode(code);
        v.setDescription(req.getParameter("description"));
        v.setDiscountType(req.getParameter("discountType"));
        v.setDiscountValue(parseDouble(req.getParameter("discountValue"), 0));
        v.setMinOrderValue(parseDouble(req.getParameter("minOrderValue"), 0));
        v.setMaxDiscount(parseDouble(req.getParameter("maxDiscount"), 0));

        // Số lượng tối đa
        String quantityParam = req.getParameter("quantity");
        if ("unlimited".equalsIgnoreCase(quantityParam) || quantityParam == null || quantityParam.isBlank()) {
            v.setQuantity(-1);
        } else {
            v.setQuantity((int) parseDouble(quantityParam, -1));
        }

        // Số lần sử dụng tối đa cho mỗi user
        String maxUsesParam = req.getParameter("maxUsesPerUser");
        if ("unlimited".equalsIgnoreCase(maxUsesParam) || maxUsesParam == null || maxUsesParam.isBlank()) {
            v.setMaxUsesPerUser(null);
        } else {
            v.setMaxUsesPerUser((int) parseDouble(maxUsesParam, 1));
        }

        // Thời gian
        String startStr = req.getParameter("startedAt");
        String endStr   = req.getParameter("expiredAt");
        if (startStr != null && !startStr.isBlank()) {
            v.setStartedAt(Timestamp.valueOf(LocalDateTime.parse(startStr, DT_LOCAL_FMT)));
        } else {
            v.setStartedAt(null);
        }
        if (endStr != null && !endStr.isBlank()) {
            v.setExpiredAt(Timestamp.valueOf(LocalDateTime.parse(endStr, DT_LOCAL_FMT)));
        }

        v.setActive("ACTIVE".equals(req.getParameter("status")));
    }

    private double parseDouble(String s, double def) {
        if (s == null || s.isBlank()) return def;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
