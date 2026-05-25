package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


@WebFilter("/*")
public class LogGlobalFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(LogGlobalFilter.class);
    private static final String LOG_PREFIX = "[LOG_GLOBAL_FILTER]";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        try {
            logger.info("{} Request: {} {}", LOG_PREFIX, req.getMethod(), req.getRequestURI());
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("{} Unhandled exception at URI: {}", LOG_PREFIX, req.getRequestURI(), e);

            if (!resp.isCommitted())  {
                request.setAttribute("errorMessage", "Something went wrong ;-;");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/error/general-error.jsp");

                dispatcher.forward(request, response);
            }
        }


    }
}
