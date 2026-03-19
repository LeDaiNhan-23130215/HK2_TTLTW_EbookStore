package models;

public class CheckoutDetail extends Base {
    private int checkoutID;
    private int bookID;
    private double price;

    public CheckoutDetail(int id) {
        super(id);
    }

    public CheckoutDetail(int checkoutID, int bookID, double price) {
        super(-1);
        this.checkoutID = checkoutID;
        this.bookID = bookID;
        this.price = price;
    }

    public CheckoutDetail(int id, int checkoutID, int bookID, double price) {
        super(id);
        this.checkoutID = checkoutID;
        this.bookID = bookID;
        this.price = price;
    }


    public int getCheckoutID() {
        return checkoutID;
    }

    public void setCheckoutID(int checkoutID) {
        this.checkoutID = checkoutID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
