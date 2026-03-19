package controllers;

import services.AdminServices;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Banner;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminBannerController", value = "/admin-banner")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024
)
public class AdminBannerController extends HttpServlet{
    private AdminServices  adminServices;

    @Override
    public void init() throws ServletException {
        adminServices = new AdminServices();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            List<Banner> banners = adminServices.getListBanner();
            req.setAttribute("banners", banners);

            req.getRequestDispatcher("/WEB-INF/views/admin-banner.jsp")
                    .forward(req, resp);
            return;
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            adminServices.deleteBanner(id);
            resp.sendRedirect(req.getContextPath() + "/admin-banner");
            return;
        }

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Banner banner = adminServices.getBannerById(id);
            req.setAttribute("banner", banner);

            req.getRequestDispatcher("/WEB-INF/views/admin-banner-edit.jsp")
                    .forward(req, resp);
            return;
        }

        List<Banner> banners = adminServices.getListBanner();
        req.setAttribute("banners", banners);

        req.getRequestDispatcher("/WEB-INF/views/admin-banner.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String mode = req.getParameter("mode");
        if(action==null){
            req.getRequestDispatcher("/WEB-INF/views/admin-banner.jsp").forward(req,resp);
            return;
        }
        if("update".equals(action)){
            int id = Integer.parseInt(req.getParameter("id"));
            String url = req.getParameter("url");
            String position = req.getParameter("position");
            String startDate = req.getParameter("startDate");
            String endDate = req.getParameter("endDate");
            int isActive = Integer.parseInt(req.getParameter("isActive"));

            Banner banner = new Banner(id);

            banner.setUrl(url);
            banner.setPosition(position);
            banner.setStartDate(startDate);
            banner.setEndDate(endDate);
            banner.setIsActive(isActive);

            adminServices.updateBanner(banner);
            resp.sendRedirect(req.getContextPath()+"/admin-banner");
            return;
        }
        if ("add".equals(action)) {
            if ("manual".equals(mode)) {
                String url = req.getParameter("url");
                String position = req.getParameter("position");
                String startDate = req.getParameter("startDate");
                String endDate = req.getParameter("endDate");
                int isActive = Integer.parseInt(req.getParameter("isActive"));

                Banner banner = new Banner(url, position, startDate, endDate, isActive);
                adminServices.addBanner(banner);

                resp.sendRedirect(req.getContextPath() + "/admin-banner");
            }
            else if ("import".equals(mode)) {
                Part filePart = req.getPart("file");

                if (filePart != null && filePart.getSize() > 0) {
                    adminServices.importBannerFile(filePart);
                }

                resp.sendRedirect(req.getContextPath() + "/admin-banner");
            }
        }
    }
}
