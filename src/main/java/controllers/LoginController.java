package controllers;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CartService;
import utils.ActivityType;
import utils.MailUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private UserDAO userDAO;
    private CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String LOG_PREFIX = "[LOGIN_CONTROLLER]";


    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        cartService = new CartService();
    }

    private boolean verifyRecaptcha(String token) {
        try {
            String secretKey = "6LfYFe0sAAAAAFSdsvjBiXQlIOpNQnHieX_UOfF1";
            URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String params = "secret=" + secretKey + "&response=" + token;
            conn.getOutputStream().write(params.getBytes());
            Scanner sc = new Scanner(conn.getInputStream());
            String response = sc.useDelimiter("\\A").next();

            boolean isSuccess = response.contains("\"success\": true");
            if(!isSuccess) {
                logger.warn("{} Recaptcha fail. Response from Google: {}", LOG_PREFIX, response);
            }
            return isSuccess;
        } catch (Exception e) {
            logger.error("{} Critical error when connecting to Google Recapcha API: ", LOG_PREFIX);
            return false;
            }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String recaptchaToken = req.getParameter("g-recaptcha-response");
        if (recaptchaToken == null || !verifyRecaptcha(recaptchaToken)) {
            logger.warn("{} Login blocked: Missing or invalid reCaptcha token", LOG_PREFIX);
            req.setAttribute("error_msg", "Vui lòng xác nhận bạn không phải robot.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }
        String input = req.getParameter("userAndEmail");
        String password = req.getParameter("password");

        if (input == null || input.isEmpty() ||
                password == null || password.isEmpty()) {
            logger.warn("{} Login failed: Empty username or password field submitted", LOG_PREFIX);
            req.setAttribute("error_msg", "Vui lòng nhập email/tên người dùng và mật khẩu.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.login(input, password);

        if (user == null) {
            logger.warn("{} Authentification failed for identifier: {}", LOG_PREFIX, input);
            req.setAttribute("error_msg", "Tên đăng nhập hoặc mật khẩu không đúng.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userID", user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("phoneNum", user.getPhoneNum());
        session.setAttribute("role", user.getRole());

        Cart cart = cartService.getCartByUserID(user.getId());
        int totalCartDetails = 0;
        if (cart != null) {
            totalCartDetails = cartService.getTotalCartDetails(cart.getId());
        }
        session.setAttribute("totalCartDetails", totalCartDetails);
        try {
        MailUtil.sendAccountActivity(
                user.getEmail(),
                user.getUsername(),
                ActivityType.LOGIN
        );
        logger.info("{} Activity mail sent successfully to '{}'", LOG_PREFIX, user.getEmail());
    } catch (Exception e) {
            logger.error("{} Failed to send login activity mail to '{}' ", LOG_PREFIX, user.getEmail());
        }

        session.setAttribute(
                "toastSuccess",
                "✅Đăng nhập thành công!"
        );

        String redirectUrl =
                (String) session.getAttribute("redirectAfterLogin");
        if (redirectUrl != null &&
                !redirectUrl.isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            logger.info("{} User '{}' [ID:{}][Role: {}] logged in successfully. Redirect to requested URL: {}",
                    LOG_PREFIX, user.getUsername(), user.getId(), user.getRole(), redirectUrl);
            resp.sendRedirect(redirectUrl);
        } else {
            logger.info("{} User '{}' [ID:{}][Role: {}] logged in successfully. Redirect to home page",
                    LOG_PREFIX, user.getUsername(), user.getId(), user.getRole());
            resp.sendRedirect(
                    req.getContextPath() + "/home"
            );
        }
    }
}
