package controllers;

import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import models.Cart;
import models.User;
import services.CartService;
import utils.ActivityType;
import utils.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/oauth/link-confirm")
public class OAuthLinkConfirmController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(OAuthLinkConfirmController.class);
    private static final String LOG_PREFIX = "[OAUTH_LINK]";

    private UserDAO userDAO;
    private CartService cartService;

    @Override
    public void init() {
        userDAO     = new UserDAO();
        cartService = new CartService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("pendingProvider") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/oauth-link-confirm.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action     = req.getParameter("action");
        String provider   = (String) session.getAttribute("pendingProvider");
        String providerId = (String) session.getAttribute("pendingProviderId");
        String email      = (String) session.getAttribute("pendingEmail");
        String returnUrl  = (String) session.getAttribute("pendingReturnUrl");

        session.removeAttribute("pendingProvider");
        session.removeAttribute("pendingProviderId");
        session.removeAttribute("pendingEmail");
        session.removeAttribute("pendingReturnUrl");

        if ("confirm".equals(action)) {
            User user = userDAO.findUserByEmail(email);
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login?error=oauth_failed");
                return;
            }

            userDAO.linkOAuthAccount(user.getId(), provider, providerId);
            User freshUser = userDAO.getUserByID(user.getId()); // reload
            if (freshUser == null) freshUser = user;
            session.setAttribute("user",     freshUser);
            session.setAttribute("userID",   freshUser.getId());
            session.setAttribute("userName", freshUser.getUsername());
            session.setAttribute("email",    freshUser.getEmail());
            session.setAttribute("phoneNum", freshUser.getPhoneNum());
            session.setAttribute("role",     freshUser.getRole());
            session.setAttribute("toastSuccess",     "✅ Đã liên kết tài khoản Google thành công!");

            try {
                Cart cart = cartService.getCartByUserID(user.getId());
                int total = cart != null ? cartService.getTotalCartDetails(cart.getId()) : 0;
                session.setAttribute("totalCartDetails", total);
            } catch (Exception e) {
                logger.warn("{} Failed to load cart after link", LOG_PREFIX);
                session.setAttribute("totalCartDetails", 0);
            }

            try {
                MailUtil.sendAccountActivity(user.getEmail(), user.getUsername(),
                        ActivityType.LINK_GOOGLE);
            } catch (Exception e) {
                logger.warn("{} Failed to send link-account notification mail", LOG_PREFIX);
            }

            if (returnUrl != null && !returnUrl.isEmpty()) {
                resp.sendRedirect(returnUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }

        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}