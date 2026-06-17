package controllers;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AdminServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/admin-chart-category")
public class AdminDashboardOrdersByCate extends HttpServlet {
    private final Gson gson = new Gson();
    private AdminServices adminServices;

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
        Map<String, Double> data = adminServices.getRevenuePerCategory(year);

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            labels.add(entry.getKey());
            values.add(entry.getValue());
        }

        resp.getWriter().write(
                gson.toJson(Map.of(
                        "labels", labels,
                        "values", values
                ))
        );
}
}
