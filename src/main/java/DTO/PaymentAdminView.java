package DTO;

import java.sql.Timestamp;

public class PaymentAdminView {

    private int id;
    private String username;
    private String method;
    private double amount;
    private Timestamp checkoutDate;
    private String status;

    public PaymentAdminView() {
    }

    public PaymentAdminView(int id, String username, String method,
                            double amount, Timestamp checkoutDate, String status) {
        this.id = id;
        this.username = username;
        this.method = method;
        this.amount = amount;
        this.checkoutDate = checkoutDate;
        this.status = status;
    }

    // ===== getters / setters =====

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // === QUAN TRỌNG ===
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Timestamp checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
