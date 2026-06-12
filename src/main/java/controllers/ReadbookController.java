package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Ebook;
import models.File;
import services.AdminServices;
import services.EbookService;
import services.FileServices;

import java.io.IOException;

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
        int ebookId = Integer.parseInt(request.getParameter("id"));
        Ebook ebook = adminService.getEbookByID(ebookId);

        if(ebook == null) {
            response.sendError(404);
            return;
        }

        File pdfFile = fileServices.getFileByID(301);
        System.out.println(pdfFile.getFileLink());
        request.setAttribute("ebook", ebook);
        request.setAttribute("pdfFile", pdfFile);

        request.getRequestDispatcher("/WEB-INF/views/readbook.jsp")
                .forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}