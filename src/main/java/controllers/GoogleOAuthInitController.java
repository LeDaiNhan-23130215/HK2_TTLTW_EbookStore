package controllers;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet("/oauth/google/init")
public class GoogleOAuthInitController extends HttpServlet {

    private static final String CLIENT_ID = "1077978751095-rlg4b4itubfrkejho04nrvn6dtspsu9j.apps.googleusercontent.com";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        String redirectAfter = session != null
                ? (String) session.getAttribute("redirectAfterLogin") : null;

        String state = (redirectAfter != null && !redirectAfter.isEmpty())
                ? java.util.Base64.getUrlEncoder().encodeToString(
                redirectAfter.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                : "";

        String redirectUri = req.getScheme() + "://" + req.getServerName()
                + ":" + req.getServerPort()
                + req.getContextPath() + "/oauth/google/callback";

        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id="     + URLEncoder.encode(CLIENT_ID, "UTF-8")
                + "&redirect_uri="  + URLEncoder.encode(redirectUri, "UTF-8")
                + "&response_type=code"
                + "&scope="         + URLEncoder.encode("openid email profile", "UTF-8")
                + "&access_type=online"
                + "&state="         + URLEncoder.encode(state, "UTF-8");

        resp.sendRedirect(googleAuthUrl);
    }
}