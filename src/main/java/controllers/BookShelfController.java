package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Ebook;
import models.Image;
import models.User;
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

        HttpSession session = req.getSession();
        User user =
                (User) session.getAttribute("user");
        int userId = user.getId();

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
