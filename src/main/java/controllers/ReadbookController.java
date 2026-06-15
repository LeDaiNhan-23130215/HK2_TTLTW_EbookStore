package controllers;

import com.mysql.cj.Session;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Ebook;
import models.File;
import models.User;
import services.AdminServices;
import services.BookshelfService;
import services.EbookService;
import services.FileServices;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ReadbookController", value = "/readbook")
public class ReadbookController extends HttpServlet {
    private AdminServices adminService;
    private BookshelfService bookshelfService;
    private FileServices fileServices;
    @Override
    public void init() {
        adminService = new AdminServices();
        bookshelfService = new BookshelfService();
        fileServices = new FileServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute("user")
                : null;
        if (user == null) {
            response.sendRedirect("/login");
            return;
        }

        int ebookId = Integer.parseInt(request.getParameter("id"));
        Ebook ebook = adminService.getEbookByID(ebookId);
                if (ebook == null) {
            response.sendError(404);
            return;
        }

        String format = request.getParameter("format");
        if (format == null || format.isBlank()) {
            response.sendError(400);
            return;
        }

        Set<Integer> ownedEbookIds = (Set<Integer>) session.getAttribute("ownedEbooksIds");
        boolean isOwned = false;

        if (ownedEbookIds != null &&
                ownedEbookIds.contains(ebookId)) {
            isOwned = bookshelfService.userOwnsBook(user.getId(), ebookId);
        }

        if (format.equalsIgnoreCase("epub")) {
            File file = fileServices.getFileByFormat(ebookId, "epub");
            request.setAttribute("file",file);
        }
        request.setAttribute("isOwned", isOwned);
        request.setAttribute("ebook", ebook);
        request.setAttribute("format", format);
        request.setAttribute("userEmail", user.getEmail());
        request.getRequestDispatcher("/WEB-INF/views/readbook.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}