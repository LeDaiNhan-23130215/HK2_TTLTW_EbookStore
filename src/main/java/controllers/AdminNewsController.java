package controllers;

import services.AdminServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.News;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminNewsController", value = "/admin-news")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminNewsController extends HttpServlet {

    private AdminServices adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) {
            List<News> newsList = adminServices.getListNews();
            req.setAttribute("newsList", newsList);

            req.getRequestDispatcher("/WEB-INF/views/admin-news.jsp")
                    .forward(req, resp);
            return;
        }

        // DELETE
        if (action.equals("delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            adminServices.deleteNews(id);
            resp.sendRedirect(req.getContextPath() + "/admin-news");
            return;
        }

        // EDIT
        if (action.equals("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            News news = adminServices.getNewsById(id);

            req.setAttribute("news", news);
            req.getRequestDispatcher("/WEB-INF/views/admin-news-edit.jsp")
                    .forward(req, resp);
            return;
        }

        // Default: list
        List<News> newsList = adminServices.getListNews();
        req.setAttribute("newsList", newsList);
        req.getRequestDispatcher("/WEB-INF/views/admin-news.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String mode = req.getParameter("mode");
        
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/admin-news");
            return;
        }

        // UPDATE
        if (action.equals("update")) {
            int id = Integer.parseInt(req.getParameter("id"));
            String title = req.getParameter("title");
            String content = req.getParameter("content");
            String imgURL = req.getParameter("imgURL");
            String author = req.getParameter("author");
            int status = Integer.parseInt(req.getParameter("status"));

            News news = new News(id);
            news.setTitle(title);
            news.setContent(content);
            news.setImgURL(imgURL);
            news.setAuthor(author);
            news.setStatus(status);

            adminServices.updateNews(news);

            resp.sendRedirect(req.getContextPath() + "/admin-news");
            return;
        }

        // ADD
        if (action.equals("add")) {
            if("manual".equals(mode)){
                String title = req.getParameter("title");
                String content = req.getParameter("content");
                String imgURL = req.getParameter("imgURL");
                String author = req.getParameter("author");
                int status = Integer.parseInt(req.getParameter("status"));

                News news = new News(title, content, imgURL, author, status);
                adminServices.addNews(news);

                resp.sendRedirect(req.getContextPath() + "/admin-news");
                return;
            } else if ("import".equals(mode)) {
                Part filePart = req.getPart("file");

                if (filePart != null && filePart.getSize() > 0) {
                    adminServices.importNewsFile(filePart);
                }

                resp.sendRedirect(req.getContextPath() + "/admin-news");
            }
        }
    }
}
