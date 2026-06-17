package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import services.AdminServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminChartYear", value = "/admin-chart-year")
public class AdminChartYear extends HttpServlet {
    private AdminServices adminServices;

    @Override
    public void init() {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Integer> yearsFromDB = adminServices.getAvailableYear();

        if (yearsFromDB == null) {
            yearsFromDB = new ArrayList<>();
        }

        int currentYear = java.time.Year.now().getValue();

        if(!yearsFromDB.contains(currentYear)) {
            yearsFromDB.add(currentYear);
        }

        yearsFromDB.sort(Integer::compareTo);

        response.getWriter().write(new com.google.gson.Gson().toJson(yearsFromDB));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}