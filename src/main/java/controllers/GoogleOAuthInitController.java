package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.OAuthConfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/oauth/google/init")
public class GoogleOAuthInitController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuthInitController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        String redirectAfter = session != null
                ? (String) session.getAttribute("redirectAfterLogin") : null;

        String state = (redirectAfter != null && !redirectAfter.isEmpty())
                ? java.util.Base64.getUrlEncoder().encodeToString(
                redirectAfter.getBytes(StandardCharsets.UTF_8))
                : "";

        String host = req.getHeader("Host");
        boolean isLocal = host != null && host.startsWith("localhost");
        String redirectUri = isLocal
                ? OAuthConfig.getRedirectUriLocal()
                : OAuthConfig.getRedirectUriProd();

        logger.info("[OAUTH_INIT] host={} redirectUri={}", host, redirectUri);

        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id="    + URLEncoder.encode(OAuthConfig.getClientId(), "UTF-8")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8")
                + "&response_type=code"
                + "&scope="        + URLEncoder.encode("openid email profile", "UTF-8")
                + "&access_type=online"
                + "&state="        + URLEncoder.encode(state, "UTF-8");

        resp.sendRedirect(googleAuthUrl);
    }
}