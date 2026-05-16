package utils;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/cart",
        "/checkout",
        "/wishlist",
        "/user-information",
        "/your-order",
        "/book-shelf",
        "/change-password",
        "/contact-information"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req =
                (HttpServletRequest) request;

        HttpServletResponse resp =
                (HttpServletResponse) response;

        HttpSession session =
                req.getSession(false);

        boolean loggedIn =
                session != null &&
                        session.getAttribute("user") != null;

        if (!loggedIn) {

            String fullURL =
                    req.getRequestURL().toString();

            if (req.getQueryString() != null) {
                fullURL += "?" + req.getQueryString();
            }

            req.getSession().setAttribute(
                    "redirectAfterLogin",
                    fullURL
            );

            resp.sendRedirect(
                    req.getContextPath() + "/login"
            );

            return;
        }

        chain.doFilter(request, response);
    }
}
