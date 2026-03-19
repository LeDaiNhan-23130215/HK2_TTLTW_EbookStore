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

@WebServlet("/admin-chart-orders")
public class AdminDashboardOrders extends HttpServlet {

    private final AdminServices adminServices = new AdminServices();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Map<Integer, Integer> data = adminServices.getCheckoutPerMonth();

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            labels.add("Tháng " + i);
            values.add(data.getOrDefault(i, 0));
        }

        resp.getWriter().write(
                gson.toJson(Map.of("labels", labels, "values", values))
        );
    }
}
