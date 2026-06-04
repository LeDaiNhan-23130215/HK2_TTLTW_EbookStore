package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "ErrorPage", value = "/ErrorPage")
public class ErrorPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            Object errorMessage = session.getAttribute("errorMessage");
            request.setAttribute("errorMessage", errorMessage);

            session.removeAttribute("errorMessage");
        }

        request.getRequestDispatcher("/error/general-error.jsp")
                .forward(request, response);
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}