package utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/cart",
        "/checkout",
        "/wishlist",
        "/userInformation",
        "/your-order",
        "/book-shelf",
        "/change-password",
})
public class AuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final String LOG_PREFIX = "[AUTH_FILTER]";

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        String requestURI = req.getRequestURI();

        if (!loggedIn) {
            String fullURL = req.getRequestURL().toString();
            if (req.getQueryString() != null) {
                fullURL += "?" + req.getQueryString();
            }

            logger.warn("{} Unauthorized access blocked for path: '{}'. Storing return checkpoint landing target and routing back to login.",
                    LOG_PREFIX, requestURI);

            HttpSession newSession = req.getSession();
            newSession.setAttribute("redirectAfterLogin", fullURL);
            newSession.setAttribute("toastWarning", "⚠️ Vui lòng đăng nhập để sử dụng chức năng này.");

            resp.sendRedirect(
                    req.getContextPath() + "/login"
            );
            return;
        }

        if (logger.isDebugEnabled()) {
            models.User user = (models.User) session.getAttribute("user");
            int userId = (user != null) ? user.getId() : -1;
            logger.debug("{} Authorized pass-through granted for User ID {} to access route: '{}'.",
                    LOG_PREFIX, userId, requestURI);
        }

        chain.doFilter(request, response);
    }
}