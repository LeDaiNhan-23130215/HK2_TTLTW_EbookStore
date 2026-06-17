package controllers;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AdminServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/admin-chart-top-ebooks")
public class AdminDashboardTop5Ebook extends HttpServlet {
    private AdminServices adminServices;
    private final Gson gson = new Gson();

    @Override
    public void init() {
        adminServices = new AdminServices();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String yearParam = req.getParameter("year");

        int year = java.time.Year.now().getValue();

        if (yearParam != null && !yearParam.isBlank()) {
            year = Integer.parseInt(yearParam);
        }

        Map<String, Double> data = adminServices.getTop5Ebook(year);

        resp.getWriter().write(
                gson.toJson(Map.of(
                        "labels", new ArrayList<>(data.keySet()),
                        "values", new ArrayList<>(data.values())
                ))
        );
    }
}
