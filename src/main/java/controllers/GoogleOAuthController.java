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

            String userJson     = fetchGoogleUserInfo(accessToken);
            String googleId     = extractJson(userJson, "sub");
            String email        = extractJson(userJson, "email");
            String displayName  = extractJson(userJson, "name");   // tên đầy đủ, chỉ dùng để log

            if (googleId == null || email == null) {
                logger.error("{} Failed to get user info. Response: {}", LOG_PREFIX, userJson);
                resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                return;
            }

            // 1) Tìm theo provider_id
            User user = userDAO.findByProvider("google", googleId);

            if (user == null) {
                // 2) Tìm theo email — account đã tồn tại, hỏi có muốn link không
                user = userDAO.findUserByEmail(email);

                if (user != null) {
                    HttpSession session  = req.getSession();
                    String returnUrl = (String) session.getAttribute("redirectAfterLogin");
                    if (returnUrl == null || returnUrl.isEmpty()) returnUrl = req.getContextPath() + "/home";
                    session.setAttribute("pendingProvider",   "google");
                    session.setAttribute("pendingProviderId", googleId);
                    session.setAttribute("pendingEmail",      email);
                    session.setAttribute("pendingReturnUrl",  returnUrl);
                    resp.sendRedirect(req.getContextPath() + "/oauth/link-confirm");
                    return;
                }

                // 3) Account chưa tồn tại — tạo mới.
                user = userDAO.createOAuthUser(displayName, email, "google", googleId);

                if (user == null) {
                    logger.error("{} createOAuthUser returned null for email={}", LOG_PREFIX, email);
                    resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                    return;
                }

                logger.info("{} New OAuth user created: id={}, username='{}'",
                        LOG_PREFIX, user.getId(), user.getUsername());
            }

            loginUser(req, resp, user);

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
        String body = "code="          + URLEncoder.encode(code,          "UTF-8")
                + "&client_id="    + URLEncoder.encode(CLIENT_ID,     "UTF-8")
                + "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri,   "UTF-8")
                + "&grant_type=authorization_code";
        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        int status = conn.getResponseCode();
        InputStream stream = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
        return readStream(stream);
    }

    private String fetchGoogleUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        int status = conn.getResponseCode();
        InputStream stream = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();
        return readStream(stream);
    }

    private String readStream(InputStream stream) throws IOException {
        if (stream == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
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

    private void loginUser(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession();
        session.setAttribute("user",             user);
        session.setAttribute("userID",           user.getId());
        session.setAttribute("userName",         user.getUsername());
        session.setAttribute("email",            user.getEmail());
        session.setAttribute("phoneNum",         user.getPhoneNum());
        session.setAttribute("role",             user.getRole());
        session.setAttribute("toastSuccess",     "✅ Đăng nhập bằng Google thành công!");

        try {
            Cart cart = cartService.getCartByUserID(user.getId());
            int total = cart != null ? cartService.getTotalCartDetails(cart.getId()) : 0;
            session.setAttribute("totalCartDetails", total);
        } catch (Exception e) {
            session.setAttribute("totalCartDetails", 0);
        }

        try {
            MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(), ActivityType.LOGIN);
        } catch (Exception e) {
            logger.warn("{} Failed to send login activity mail for userId={}", LOG_PREFIX, user.getId());
        }

        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            session.removeAttribute("redirectAfterLogin");
            resp.sendRedirect(redirectUrl);
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
