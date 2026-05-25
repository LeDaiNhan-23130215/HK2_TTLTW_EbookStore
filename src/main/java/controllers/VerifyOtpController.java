package controllers;

import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(name = "Verify-Otp", value = "/verify-otp")
public class VerifyOtpController extends HttpServlet {
    private UserDAO userDAO;
    private static final Logger logger = LoggerFactory.getLogger(VerifyOtpController.class);
    private static final String LOG_PREFIX = "[VERIFY_OTP_CONTROLLER]";

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("otp") == null) {
            logger.warn("{} Direct page access blocked: Missing registration context session token.", LOG_PREFIX);
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String inputOtp = req.getParameter("otp");

        String sessionOtp = (String) session.getAttribute("otp");
        Long expire = (Long) session.getAttribute("otp_expire");
        String trackingEmail = (String) session.getAttribute("signup_email");

        if (expire == null || System.currentTimeMillis() > expire) {
            logger.info("{} Verification timeout: OTP token expired for pending email '{}'. Redirecting back to signup.", LOG_PREFIX, trackingEmail);
            backToSignup(req, resp, session, "OTP đã hết hạn");
            return;
        }

        if (inputOtp == null || !inputOtp.equals(sessionOtp)) {
            logger.warn("{} Verification mismatch: Incorrect OTP value provided for pending email '{}'.", LOG_PREFIX, trackingEmail);
            backToSignup(req, resp, session, "OTP không đúng");
            return;
        }

        String username = (String) session.getAttribute("signup_username");
        String phoneNumber = (String) session.getAttribute("signup_phoneNumber");
        String password = (String) session.getAttribute("signup_password");

        logger.info("{} Verification success: OTP confirmed for target account profile '{}'. Attempting database storage writing routing.", LOG_PREFIX, trackingEmail);

        boolean isCreated = userDAO.signUp(username, trackingEmail, phoneNumber, password);

        if (isCreated) {
            logger.info("{} Account creation successful: User '{}' (Email: '{}') successfully registered. Invalidating pending verification session setup.", LOG_PREFIX, username, trackingEmail);
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            logger.error("{} Structural failure: Verified user context data matching identity '{}' could not be safely appended to database.", LOG_PREFIX, trackingEmail);
            backToSignup(req, resp, session, "Đăng ký không thành công do lỗi hệ thống.");
        }
    }

    private void backToSignup(HttpServletRequest req, HttpServletResponse resp,
                              HttpSession session, String msg)
            throws ServletException, IOException {

        req.setAttribute("error_msg", msg);
        req.setAttribute("fname", session.getAttribute("signup_username"));
        req.setAttribute("userAndEmail", session.getAttribute("signup_email"));
        req.setAttribute("phoneNumber", session.getAttribute("signup_phoneNumber"));

        req.getRequestDispatcher("/WEB-INF/views/sign-up.jsp").forward(req, resp);
    }
}