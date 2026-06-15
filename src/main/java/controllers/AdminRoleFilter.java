package controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = {
        "/admin-dashboard",
        "/admin-ebook",
        "/admin-author",
        "/admin-category",
        "/admin-banner",
        "/admin-discount",
        "/admin-feedback",
        "/admin-logs",
        "/admin-news",
        "/admin-payment",
        "/admin-review",
        "/admin-user",
        "/admin-voucher"
})
public class AdminRoleFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AdminRoleFilter.class);
    private static final String LOG_PREFIX = "[ADMIN_ROLE_FILTER]";
    private static final Set<String> FORM_ONLY_ACTIONS = new HashSet<>(Arrays.asList(
            "edit", "add", "create", "new"
    ));

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String requestURI  = req.getRequestURI();

        HttpSession session   = req.getSession(false);
        boolean     loggedIn  = session != null && session.getAttribute("user") != null;

        // 1. Chưa đăng nhập
        if (!loggedIn) {
            String fullURL = req.getRequestURL().toString();
            if (req.getQueryString() != null) {
                fullURL += "?" + req.getQueryString();
            }

            logger.warn("{} Unauthenticated access to '{}'. Saving redirect target → /login.",
                    LOG_PREFIX, requestURI);

            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("redirectAfterLogin", fullURL);
            newSession.setAttribute("toastWarning", "⚠️ Vui lòng đăng nhập để tiếp tục.");

            resp.sendRedirect(contextPath + "/login");
            return;
        }

        // 2. Đã login – kiểm tra role
        User user = (User) session.getAttribute("user");

        if (!"admin".equalsIgnoreCase(user.getRole())) {
            logger.warn("{} Forbidden: user '{}' (role='{}') attempted to access admin area '{}'.",
                    LOG_PREFIX, user.getUsername(), user.getRole(), requestURI);
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này.");
            return;
        }

        // 3. Là admin – chỉ áp dụng kiểm tra cho GET
        String action = req.getParameter("action");
        if ("GET".equalsIgnoreCase(req.getMethod())
                && action != null
                && FORM_ONLY_ACTIONS.contains(action.toLowerCase())) {

            String referer = req.getHeader("Referer");

            if (!hasValidReferer(req, referer, contextPath)) {
                String listURL = resolveListURL(requestURI, contextPath);
                logger.warn("{} Admin '{}' accessed form-only action='{}' on '{}' without valid Referer. Redirecting to list: '{}'.",
                        LOG_PREFIX, user.getUsername(), action, requestURI, listURL);

                session.setAttribute("toastWarning", "⚠️ Yêu cầu không hợp lệ, vui lòng thao tác lại.");
                resp.sendRedirect(listURL);
                return;
            }
        }

        logger.debug("{} Admin '{}' granted access to '{}' action='{}'.",
                LOG_PREFIX, user.getUsername(), requestURI, action);
        chain.doFilter(request, response);
    }

    private boolean hasValidReferer(HttpServletRequest req, String referer, String contextPath) {
        if (referer == null || referer.isEmpty()) return false;

        try {
            URI refUri = new URI(referer);
            String refPath = refUri.getPath();
            if (refPath == null) return false;
            String basePath = contextPath.isEmpty() ? "/" : contextPath + "/";
            return refPath.startsWith(basePath);

        } catch (URISyntaxException e) {
            logger.warn("{} Invalid Referer URI: {}", LOG_PREFIX, referer);
            return false;
        }
    }

    private String resolveListURL(String requestURI, String contextPath) {
        String path = requestURI.startsWith(contextPath)
                ? requestURI.substring(contextPath.length())
                : requestURI;
        if (path.endsWith("/")) path = path.substring(0, path.length() - 1);

        switch (path) {
            case "/admin-ebook":    return contextPath + "/admin-ebook";
            case "/admin-author":   return contextPath + "/admin-author";
            case "/admin-category": return contextPath + "/admin-category";
            case "/admin-banner":   return contextPath + "/admin-banner";
            case "/admin-discount": return contextPath + "/admin-discount";
            case "/admin-news":     return contextPath + "/admin-news";
            case "/admin-user":     return contextPath + "/admin-user";
            case "/admin-voucher":  return contextPath + "/admin-voucher";
            case "/admin-feedback": return contextPath + "/admin-feedback";
            case "/admin-payment":  return contextPath + "/admin-payment";
            case "/admin-review":   return contextPath + "/admin-review";
            case "/admin-logs":     return contextPath + "/admin-logs";
            default:                return contextPath + "/admin-dashboard";
        }
    }
}