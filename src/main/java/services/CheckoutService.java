package services;

import DAO.CheckoutDAO;
import DAO.CheckoutDetailDAO;
import DAO.PaymentMethodDAO;
import DTO.CartItem;
import models.*;
import utils.DBConnection;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutService {

    private CheckoutDAO checkoutDAO = new CheckoutDAO();
    private CheckoutDetailDAO checkoutDetailDAO = new CheckoutDetailDAO();
    private PaymentMethodDAO  paymentMethodDAO = new PaymentMethodDAO();

    public boolean checkout(Checkout checkout, List<CartItem> cart) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            int checkoutID = checkoutDAO.createCheckout(con, checkout);

            for (CartItem item : cart) {
                checkoutDetailDAO.addCheckoutDetail(
                        con,
                        new CheckoutDetail(checkoutID, item.getEbook().getId(), item.getEbook().getPrice())
                );
            }

            checkoutDAO.updateStatus(con, checkoutID, "success");
            checkout.setId(checkoutID);
            checkout.setStatus("Thành công");

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getPMIDByName(String name) {
        return paymentMethodDAO.getPaymentMethodIdByName(name);
    }

    public Map<Integer, PaymentMethod> getAllPMs() {
        Map<Integer, PaymentMethod> pms = paymentMethodDAO.getPMMap();
        return pms != null ? pms : new HashMap<>();
    }
}

