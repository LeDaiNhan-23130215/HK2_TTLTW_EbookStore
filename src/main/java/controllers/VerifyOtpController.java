package controllers;
import DAO.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "Verify-Otp", value = "/verify-otp")
public class VerifyOtpController extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("otp") == null) {
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

        if(expire==null || System.currentTimeMillis()>expire){
            backToSignup(req, resp, session, "OTP đã hết hạn");
            return;
        }

        if (inputOtp == null || !inputOtp.equals(sessionOtp)) {
            backToSignup(req, resp, session, "OTP không đúng");
            return;
        }

        userDAO.signUp((String) session.getAttribute("signup_username"),
                        (String) session.getAttribute("signup_email"),
                        (String) session.getAttribute("signup_phoneNumber"),
                        (String) session.getAttribute("signup_password")
                        );

        session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login");
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
