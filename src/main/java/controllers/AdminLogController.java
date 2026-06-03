package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "admin-logs", value = "/admin-logs")
public class AdminLogController extends HttpServlet {
    private int MAX_LINES = 200;
    private String LOG_PATH = System.getProperty("catalina.base") + File.separator
            + "logs" + File.separator
            + "ebookstore" + File.separator
            + "system.log";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> logList = new ArrayList<String>();

        Path path = Paths.get(LOG_PATH);

        if(Files.exists(path)) {
            List<String> allLogs = Files.readAllLines(path);

            int fromIndex = Math.max(0, allLogs.size() - MAX_LINES);

            logList = allLogs.subList(fromIndex, allLogs.size());
        }

        request.setAttribute("logs", logList);

        request.getRequestDispatcher("/WEB-INF/views/admin-logs.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}