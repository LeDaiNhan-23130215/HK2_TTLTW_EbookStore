package controllers;

import DAO.PasswordResetDAO;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.PasswordReset;
import models.User;
import utils.HashUtil;
import utils.MailUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "ForgotPasswordController", value = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private UserDAO userDAO;
    private PasswordResetDAO passwordResetDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        passwordResetDAO = new PasswordResetDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        switch (action) {
            case "sendCode":
                sendResetCode(req, resp);
                break;

            case "verifyCode":
                verifyCode(req, resp);
                break;

            case "resetPassword":
                resetPassword(req, resp);
                break;

            default:
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
        }
    }

    private void sendResetCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        User user = userDAO.findUserByEmail(email);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=emailNotFound");
            return;
        }

        passwordResetDAO.deleteByUser(user.getId());

        String otp = generateOtp();
        String otpHash = HashUtil.sha256(otp);

        Timestamp expiresAt =
                Timestamp.from(Instant.now().plus(15, ChronoUnit.MINUTES));

        passwordResetDAO.createToken(user.getId(), otpHash, expiresAt);

        MailUtil.sendOtp(email, otp);

        HttpSession session = req.getSession();
        session.setAttribute("resetEmail", email);

        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=verify");
    }

    private void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("confirmCode");
        String tokenHash = HashUtil.sha256(code);

        Optional<PasswordReset> opt = passwordResetDAO.findValidToken(tokenHash);
        if (opt.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=invalidCode");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("resetTokenID", opt.get().getId());
        session.setAttribute("resetUserID", opt.get().getUserID());

        resp.sendRedirect(req.getContextPath() + "/forgot-password?step=reset");
    }

    private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("resetUserID") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password?error=passwordMismatch");
            return;
        }

        int userID = (Integer) session.getAttribute("resetUserID");
        int tokenId = (Integer) session.getAttribute("resetTokenID");

        userDAO.updatePassword(userID, newPassword);
        passwordResetDAO.markTokenUsed(tokenId);
        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login?reset=success");
    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
}
