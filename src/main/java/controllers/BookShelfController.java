package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Ebook;
import models.Image;
import services.BookshelfService;
import services.ImageServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BookShelfController", value = "/book-shelf")
public class BookShelfController extends HttpServlet {

    private BookshelfService bookshelfService;

    @Override
    public void init() {
        bookshelfService = new BookshelfService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        var session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userID");

        List<Ebook> books = bookshelfService.getBooksOfUserWithDetails(userId);

        req.setAttribute("books", books);

        req.getRequestDispatcher("/WEB-INF/views/book-shelf.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
