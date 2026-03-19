package controllers;

import models.Category;
import services.AdminServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminCategoryController", value = "/admin-category")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminCategoryController extends HttpServlet {

    private AdminServices adminServices;

    @Override
    public void init() {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) {
            loadCategoryPage(req, resp);
            return;
        }

        switch (action) {
            case "delete":
                int idDel = Integer.parseInt(req.getParameter("id"));
                adminServices.deleteCategory(idDel);
                resp.sendRedirect(req.getContextPath() + "/admin-category");
                break;

            case "edit":
                int idEdit = Integer.parseInt(req.getParameter("id"));
                Category category = adminServices.getCategoryById(idEdit);
                req.setAttribute("category", category);
                req.setAttribute("iconList", getIconList());
                req.getRequestDispatcher("/WEB-INF/views/admin-category-edit.jsp")
                        .forward(req, resp);
                break;

            default:
                loadCategoryPage(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String mode = req.getParameter("mode"); // manual | import

        if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("categoryName");
            String desc = req.getParameter("description");
            String icon = req.getParameter("icon");
            if (icon == null || icon.isBlank()) {
                icon = "fa-solid fa-folder";
            }

            adminServices.updateCategory(new Category(id, name, desc, icon));
            resp.sendRedirect(req.getContextPath() + "/admin-category");
            return;
        }

        if ("add".equals(action)) {

            if ("manual".equals(mode)) {
                String name = req.getParameter("categoryName");
                String desc = req.getParameter("description");
                String icon = req.getParameter("icon");

                if (icon == null || icon.isBlank()) {
                    icon = "fa-solid fa-folder";
                }

                adminServices.addCategory(new Category(name.trim(), desc, icon));

                resp.sendRedirect(req.getContextPath() + "/admin-category");
                return;
            }

            if ("import".equals(mode)) {
                Part filePart = req.getPart("file");

                if (filePart != null && filePart.getSize() > 0) {
                    adminServices.importCategoryFile(filePart);
                }

                resp.sendRedirect(req.getContextPath() + "/admin-category");
                return;
            }
        }

        loadCategoryPage(req, resp);
    }

    private void loadCategoryPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Category> categories = adminServices.getListCategory();
        req.setAttribute("categories", categories);
        req.setAttribute("iconList", getIconList());

        req.getRequestDispatcher("/WEB-INF/views/admin-category.jsp")
                .forward(req, resp);
    }

    private List<String> getIconList() {
        return List.of(
                "fa-solid fa-book",
                "fa-solid fa-book-open",
                "fa-solid fa-robot",
                "fa-solid fa-brain",
                "fa-solid fa-code",
                "fa-solid fa-database",
                "fa-solid fa-laptop",
                "fa-solid fa-graduation-cap",
                "fa-solid fa-chart-line",
                "fa-solid fa-globe",
                "fa-solid fa-layer-group",
                "fa-solid fa-lightbulb",
                "fa-solid fa-flask",
                "fa-solid fa-pen",
                "fa-solid fa-folder"
        );
    }
}