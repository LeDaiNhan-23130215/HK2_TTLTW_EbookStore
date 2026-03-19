package controllers;

import DAO.FileDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.*;
import services.AdminServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "AdminEbookController", value = "/admin-ebook")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminEbookController extends HttpServlet {

    private AdminServices adminServices;

    @Override
    public void init() {
        adminServices = new AdminServices();
    }

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = getAction(req);

        switch (action) {
            case "edit":
                showEditForm(req, resp);
                break;
            case "delete":
                deleteEbook(req, resp);
                break;
            default:
                showList(req, resp);
        }
    }

    // ===================== POST =====================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = getAction(req);

        switch (action) {
            case "add":
                insertEbook(req, resp);
                break;
            case "update":
                updateEbook(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin-ebook");
        }
    }

    // ===================== VIEW =====================

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int page = Integer.parseInt(
                req.getParameter("page") != null ? req.getParameter("page") : "1"
        );

        List<Ebook> ebooks = adminServices.listBooksForAdmin(page, 20);

        req.setAttribute("ebooks", ebooks);
        req.setAttribute("page", page);

        req.setAttribute("categoryMap", buildCategoryMap());

        req.getRequestDispatcher("/WEB-INF/views/admin-ebook.jsp")
                .forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = parseInt(req.getParameter("id"));

        Ebook ebook = adminServices.getEbookByID(id);
        if (ebook == null) {
            resp.sendRedirect(req.getContextPath() + "/admin-ebook");
            return;
        }

        req.setAttribute("ebook", ebook);
        req.setAttribute("authors", adminServices.getListAuthors());
        req.setAttribute("categories", adminServices.getListCategory());

        req.getRequestDispatcher("/WEB-INF/views/admin-ebook-edit.jsp")
                .forward(req, resp);
    }

    // ===================== ACTION =====================

    private void insertEbook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Ebook ebook = buildEbook(req);

        List<Integer> authorIds = parseIds(req.getParameterValues("authorIds"));
        List<String> imageUrls = parseStrings(req.getParameterValues("coverUrls"));

        adminServices.createEbook(ebook, authorIds, imageUrls);

        resp.sendRedirect(req.getContextPath() + "/admin-ebook");
    }

    private List<String> parseStrings(String[] coverUrls) {
        if (coverUrls == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(coverUrls));
    }

    private List<Integer> parseIds(String[] authorIds) {
        List<Integer> ids = new ArrayList<>();
        if (authorIds == null) return ids;

        for (String id : authorIds) {
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    private void updateEbook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = parseInt(req.getParameter("id"));
        Ebook old = adminServices.getEbookByID(id);

        if (old == null) {
            resp.sendRedirect(req.getContextPath() + "/admin-ebook");
            return;
        }

        Ebook ebook = buildUpdatedEbook(req, old);
        adminServices.updateEbook(ebook);

        resp.sendRedirect(req.getContextPath() + "/admin-ebook");
    }

    private void deleteEbook(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = parseInt(req.getParameter("id"));
        adminServices.deleteEbook(id);

        resp.sendRedirect(req.getContextPath() + "/admin-ebook");
    }

    // ===================== BUILDER =====================

    private Ebook buildEbook(HttpServletRequest req) {
        return new Ebook(
                adminServices.generateEbookCode(
                        parseInt(req.getParameter("categoryId"))
                ),
                req.getParameter("title"),
                parseDouble(req.getParameter("price")),
                req.getParameter("description"),
                parseInt(req.getParameter("categoryId")),
                0,
                "ACTIVE"
        );
    }

    private Ebook buildUpdatedEbook(HttpServletRequest req, Ebook old) {

        return new Ebook(
                old.getId(),
                old.getEBookCode(),
                req.getParameter("title"),
                parseDouble(req.getParameter("price")),
                req.getParameter("description"),
                parseInt(req.getParameter("categoryId")),
                old.getFileID(),
                old.getStatus()
        );
    }

    // ===================== FILE / IMAGE =====================

    private int insertFile(String title, String url) {
        File file = new File(
                title, getFileFormat(url), 0, url, "ACTIVE"
        );
        return new FileDAO().insertAndReturnId(file);
    }

    // ===================== MAP =====================

    private Map<Integer, String> buildAuthorMap() {
        return adminServices.getListAuthors()
                .stream()
                .collect(Collectors.toMap(Author::getId, Author::getAuthorName));
    }

    private Map<Integer, String> buildCategoryMap() {
        return adminServices.getListCategory()
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    // ===================== UTIL =====================

    private String getAction(HttpServletRequest req) {
        return req.getParameter("action") == null ? "list" : req.getParameter("action");
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return 0; }
    }

    private double parseDouble(String value) {
        try { return Double.parseDouble(value); }
        catch (Exception e) { return 0; }
    }

    private String getFileFormat(String url) {
        if (url == null) return "UNKNOWN";
        int dot = url.lastIndexOf(".");
        return dot != -1 ? url.substring(dot + 1).toUpperCase() : "UNKNOWN";
    }
}
