package controllers;

import DAO.UserDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cart;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.CartService;
import utils.ActivityType;
import utils.MailUtil;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

@WebServlet("/oauth/google/callback")
public class GoogleOAuthController extends HttpServlet {

    private static final Logger logger     = LoggerFactory.getLogger(GoogleOAuthController.class);
    private static final String LOG_PREFIX = "[GOOGLE_OAUTH]";

    private static final String CLIENT_ID     = "1077978751095-rlg4b4itubfrkejho04nrvn6dtspsu9j.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";

    private UserDAO     userDAO;
    private CartService cartService;

    @Override
    public void init() {
        userDAO     = new UserDAO();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String error = req.getParameter("error");
        if (error != null) {
            logger.warn("{} Google returned error: {}", LOG_PREFIX, error);
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String code = req.getParameter("code");
        String state = req.getParameter("state");
        String redirectAfter = null;

        if (state != null && !state.isEmpty()) {
            try {
                redirectAfter = new String(
                        java.util.Base64.getUrlDecoder().decode(state),
                        StandardCharsets.UTF_8
                );
            } catch (Exception ignored) {
                logger.warn("{} Invalid OAuth state", LOG_PREFIX);
            }
        }

        if (code == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
            return;
        }

        String redirectUri = req.getScheme() + "://" + req.getServerName()
                + ":" + req.getServerPort()
                + req.getContextPath() + "/oauth/google/callback";

        try {
            String tokenJson   = exchangeCodeForToken(code, redirectUri);
            String accessToken = extractJson(tokenJson, "access_token");

            if (accessToken == null) {
                logger.error("{} Failed to get access_token. Response: {}", LOG_PREFIX, tokenJson);
                resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                return;
            }

            String userJson    = fetchGoogleUserInfo(accessToken);
            String googleId    = extractJson(userJson, "sub");
            String email       = extractJson(userJson, "email");
            String displayName  = extractJson(userJson, "name");

            if (googleId == null || email == null) {
                logger.error("{} Failed to get user info. Response: {}", LOG_PREFIX, userJson);
                resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                return;
            }

            User user = userDAO.findByProvider("google", googleId);

            if (user == null) {
                user = userDAO.findUserByEmail(email);

                if (user != null) {
                    HttpSession session = req.getSession();

                    String returnUrl = redirectAfter;
                    if (returnUrl == null || returnUrl.isEmpty()) {
                        returnUrl = (String) session.getAttribute("redirectAfterLogin");
                    }
                    if (returnUrl == null || returnUrl.isEmpty()) {
                        returnUrl = req.getContextPath() + "/home";
                    }

                    session.setAttribute("pendingProvider",  "google");
                    session.setAttribute("pendingProviderId", googleId);
                    session.setAttribute("pendingEmail",      email);
                    session.setAttribute("pendingReturnUrl",  returnUrl);

                    resp.sendRedirect(req.getContextPath() + "/oauth/link-confirm");
                    return;
                }

                user = userDAO.createOAuthUser(displayName, email, "google", googleId);

                if (user == null) {
                    logger.error("{} createOAuthUser returned null for email={}", LOG_PREFIX, email);
                    resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                    return;
                }

                logger.info("{} New OAuth user created: id={}, username='{}'",
                        LOG_PREFIX, user.getId(), user.getUsername());

                try {
                    MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.REGISTER);
                    logger.info("{} Welcome registration mail sent to {}", LOG_PREFIX, user.getEmail());
                } catch (Exception mailEx) {
                    logger.error("{} Failed to send registration welcome mail to {}: ",
                            LOG_PREFIX, user.getEmail(), mailEx);
                }

                loginUser(req, resp, user, true, redirectAfter);
                return;
            }

            loginUser(req, resp, user, false, redirectAfter);

        } catch (Exception e) {
            logger.error("{} OAuth error: ", LOG_PREFIX, e);
            resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
        }
    }

    private String exchangeCodeForToken(String code, String redirectUri) throws IOException {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String body = "code="           + URLEncoder.encode(code,          "UTF-8")
                + "&client_id="        + URLEncoder.encode(CLIENT_ID,     "UTF-8")
                + "&client_secret="     + URLEncoder.encode(CLIENT_SECRET, "UTF-8")
                + "&redirect_uri="      + URLEncoder.encode(redirectUri,   "UTF-8")
                + "&grant_type=authorization_code";

        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));

        int status = conn.getResponseCode();
        InputStream stream = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        return readStream(stream);
    }

    private String fetchGoogleUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int status = conn.getResponseCode();
        InputStream stream = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        return readStream(stream);
    }

    private String readStream(InputStream stream) throws IOException {
        if (stream == null) return "";
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private String extractJson(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return null;

        int colon = json.indexOf(':', idx + search.length());
        if (colon == -1) return null;

        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;

        if (start < json.length() && json.charAt(start) == '"') {
            start++;
            int end = json.indexOf('"', start);
            return end == -1 ? null : json.substring(start, end);
        }

        return null;
    }

    private void loginUser(HttpServletRequest req,
                           HttpServletResponse resp,
                           User user,
                           boolean isNewUser,
                           String redirectAfter) throws IOException {

        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        session.setAttribute("userID", user.getId());
        session.setAttribute("userName", user.getUsername());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("phoneNum", user.getPhoneNum());
        session.setAttribute("role", user.getRole());

        if (isNewUser) {
            session.setAttribute(
                    "toastSuccess",
                    "✅ Chào mừng bạn đến với EbookStore! Tài khoản Google đã được tạo thành công."
            );
        } else {
            session.setAttribute(
                    "toastSuccess",
                    "✅ Đăng nhập bằng Google thành công!"
            );
        }


        CartController.mergeGuestCartToUser(session, cartService,
                new DAO.BookshelfDAO(), user.getId());
        Cart cart = cartService.getCartByUserID(user.getId());
        int total = (cart != null) ? cartService.getTotalCartDetails(cart.getId()) : 0;
        session.setAttribute("totalCartDetails", total);

        try {
            ActivityType activityType = isNewUser
                    ? ActivityType.REGISTER
                    : ActivityType.LOGIN;

            MailUtil.sendAccountActivity(
                    user.getEmail(),
                    user.getUsername(),
                    activityType
            );
        } catch (Exception e) {
            logger.warn("{} Failed to send activity mail", LOG_PREFIX);
        }

        if (redirectAfter != null && !redirectAfter.isEmpty()) {
            resp.sendRedirect(redirectAfter);
            return;
        }

        String sessionRedirect = (String) session.getAttribute("redirectAfterLogin");
        if (sessionRedirect != null && !sessionRedirect.isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            resp.sendRedirect(sessionRedirect);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}