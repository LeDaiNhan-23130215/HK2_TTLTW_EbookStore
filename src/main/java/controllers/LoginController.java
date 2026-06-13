package controllers;

import DAO.UserDAO;
import DAO.BookshelfDAO;
import DTO.LoginOutcome;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Cart;
import models.Ebook;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.BookshelfService;
import services.CartService;
import utils.ActivityType;
import utils.MailUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {

    private UserDAO     userDAO;
    private CartService cartService;
    private BookshelfService bookshelfService;
    private static final Logger logger     = LoggerFactory.getLogger(LoginController.class);
    private static final String LOG_PREFIX = "[LOGIN_CONTROLLER]";

    @Override
    public void init() throws ServletException {
        userDAO     = new UserDAO();
        cartService = new CartService();
        bookshelfService = new BookshelfService();
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
            Scanner sc       = new Scanner(conn.getInputStream());
            String response  = sc.useDelimiter("\\A").next();
            boolean isSuccess = response.contains("\"success\": true");
            if (!isSuccess) logger.warn("{} reCAPTCHA failed. Google: {}", LOG_PREFIX, response);
            return isSuccess;
        } catch (Exception e) {
            logger.error("{} reCAPTCHA API error: ", LOG_PREFIX, e);
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String recaptchaToken = req.getParameter("g-recaptcha-response");
        if (recaptchaToken == null || !verifyRecaptcha(recaptchaToken)) {
            logger.warn("{} Login blocked: invalid reCAPTCHA", LOG_PREFIX);
            req.setAttribute("error_msg", "Vui lòng xác nhận bạn không phải robot.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        String input    = req.getParameter("userAndEmail");
        String password = req.getParameter("password");

        if (input == null || input.isEmpty() || password == null || password.isEmpty()) {
            logger.warn("{} Login failed: empty fields", LOG_PREFIX);
            req.setAttribute("error_msg", "Vui lòng nhập email/tên người dùng và mật khẩu.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        LoginOutcome outcome = userDAO.attemptLogin(input, password);

        switch (outcome.getResult()) {

            case SUCCESS:
                loginSuccess(req, resp, outcome.getUser());
                return;

            case OAUTH_ACCOUNT:
                // Phát hiện sớm: account tồn tại nhưng là OAuth — hướng dẫn cụ thể
                logger.warn("{} OAuth account attempted form-login: '{}'", LOG_PREFIX, input);
                req.setAttribute("error_msg", "oauth_account");
                req.setAttribute("oauthInput", input);  // giữ lại email để pre-fill nếu cần
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;

            case USER_NOT_FOUND:
            case WRONG_PASSWORD:
                logger.warn("{} Auth failed ({}) for: '{}'", LOG_PREFIX, outcome.getResult(), input);
                req.setAttribute("error_msg", "Tên đăng nhập hoặc mật khẩu không đúng.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;

            default:
                req.setAttribute("error_msg", "Đã xảy ra lỗi, vui lòng thử lại.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    private void loginSuccess(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute("user",     user);
        session.setAttribute("userID",   user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("email",    user.getEmail());
        session.setAttribute("phoneNum", user.getPhoneNum());
        session.setAttribute("role",     user.getRole());

        List<Integer> ownedEbookIds = bookshelfService.getBookIdsOfUserId(user.getId());
        Set<Integer> ownedEbooks = new HashSet<>(ownedEbookIds);
        session.setAttribute("ownedEbooks", ownedEbooks);

        Cart cart = cartService.getCartByUserID(user.getId());
        CartController.mergeGuestCartToUser(session, cartService,
                new BookshelfDAO(), user.getId());
        int totalCartDetails = (cart != null) ? cartService.getTotalCartDetails(cart.getId()) : 0;
        session.setAttribute("totalCartDetails", totalCartDetails);

        try {
            MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.LOGIN);
            logger.info("{} Activity mail sent to '{}'", LOG_PREFIX, user.getEmail());
        } catch (Exception e) {
            logger.error("{} Failed to send login activity mail to '{}': ", LOG_PREFIX, user.getEmail(), e);
        }

        session.setAttribute("toastSuccess", "✅ Đăng nhập thành công!");

        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            logger.info("{} User '{}' logged in, redirect to: {}", LOG_PREFIX, user.getUsername(), redirectUrl);
            resp.sendRedirect(redirectUrl);
        } else {
            logger.info("{} User '{}' logged in, redirect to home", LOG_PREFIX, user.getUsername());
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}