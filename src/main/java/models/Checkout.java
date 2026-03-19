package models;

import java.sql.Timestamp;

public class Checkout extends Base {
    private int userID;
    private int paymentMethodID;
    private double totalAmount;
    private Timestamp checkoutDate; // DB tự tạo
    private String status;

    // Constructor dùng khi INSERT (KHÔNG có checkoutDate)
    public Checkout(int userID, int paymentMethodID,
                    double totalAmount, String status) {
        super(0); // id tạm, DB auto increment
        this.userID = userID;
        this.paymentMethodID = paymentMethodID;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Constructor dùng khi SELECT
    public Checkout(int id, int userID, int paymentMethodID,
                    double totalAmount, Timestamp checkoutDate, String status) {
        super(id);
        this.userID = userID;
        this.paymentMethodID = paymentMethodID;
        this.totalAmount = totalAmount;
        this.checkoutDate = checkoutDate;
        this.status = status;
    }

    // Getter & Setter
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getPaymentMethodID() { return paymentMethodID; }
    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Timestamp getCheckoutDate() { return checkoutDate; }
    public void setCheckoutDate(Timestamp checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
