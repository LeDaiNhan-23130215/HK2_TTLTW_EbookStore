package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Author;
import services.AdminServices;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminAuthorController", value = "/admin-author")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminAuthorController extends HttpServlet {

    private AdminServices adminService;

    @Override
    public void init() {
        adminService = new AdminServices();
    }

    /* ================= GET ================= */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("edit".equals(action)) {
            showEditForm(req, resp);
            return;
        }

        if ("delete".equals(action)) {
            deleteAuthor(req, resp);
            return;
        }

        showAuthorList(req, resp);
    }

    /* ================= POST ================= */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addAuthor(req, resp);
            return;
        }

        if ("update".equals(action)) {
            updateAuthor(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin-author");
    }

    /* ================= HANDLERS ================= */

    private void showAuthorList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Author> authors = adminService.getListAuthors();
        req.setAttribute("authors", authors);

        forward(req, resp, "/WEB-INF/views/admin-author.jsp");
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = getIntParam(req, "id");
        Author author = adminService.getAuthorById(id);

        if (author == null) {
            redirect(resp, req);
            return;
        }

        req.setAttribute("author", author);
        forward(req, resp, "/WEB-INF/views/admin-author-edit.jsp");
    }

    private void addAuthor(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String mode = req.getParameter("mode");

        if ("import".equals(mode)) {
            Part file = req.getPart("file");
            if (file != null && file.getSize() > 0) {
                adminService.importAuthorFile(file);
            }
        } else {
            Author author = buildAuthor(req);
            adminService.addAuthor(author);
        }

        redirect(resp, req);
    }

    private void updateAuthor(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = getIntParam(req, "id");
        Author author = buildAuthor(req);
        author.setId(id);

        adminService.updateAuthor(author);
        redirect(resp, req);
    }

    private void deleteAuthor(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        int id = getIntParam(req, "id");
        adminService.deleteAuthor(id);
        redirect(resp, req);
    }

    /* ================= HELPERS ================= */

    private Author buildAuthor(HttpServletRequest req) {

        return new Author(
                req.getParameter("authorName"),
                req.getParameter("authorDetail")
        );
    }

    private int getIntParam(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(req.getParameter(name));
        } catch (Exception e) {
            return 0;
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    private void redirect(HttpServletResponse resp, HttpServletRequest req)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin-author");
    }
}
