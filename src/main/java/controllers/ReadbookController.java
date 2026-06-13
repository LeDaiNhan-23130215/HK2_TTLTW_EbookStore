package controllers;

import com.mysql.cj.Session;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Ebook;
import models.File;
import services.AdminServices;
import services.EbookService;
import services.FileServices;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ReadbookController", value = "/readbook")
public class ReadbookController extends HttpServlet {
    private AdminServices adminService;
    private FileServices fileServices;
    @Override
    public void init() {
        adminService = new AdminServices();
        fileServices = new FileServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
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

        File file = fileServices.getFileByFormat(ebookId, format);
        if (file == null) {
            response.sendError(404);
            return;
        }

        Set<Integer> ownedEbookIds = (Set<Integer>) session.getAttribute("ownedEbooksIds");
        boolean isOwned = ownedEbookIds != null && ownedEbookIds.contains(ebookId);

        request.setAttribute("isOwned", isOwned);
        request.setAttribute("ebook", ebook);
        request.setAttribute("file", file);
        request.getRequestDispatcher("/WEB-INF/views/readbook.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}