package controllers;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AdminServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/admin-dashboard-revenue")
public class AdminDashboardRevenue extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        AdminServices adminServices = new AdminServices();

        Map<Integer, Double> data = adminServices.monthlyRevenueData(2026);

        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        for(int i = 1; i <= 12; i++){
            labels.add("Tháng: " + i);
            values.add(data.getOrDefault(i, 0.0));
        }

        String json = new Gson().toJson(
                Map.of("labels", labels, "values", values)
        );

        resp.getWriter().write(json);
    }
}
