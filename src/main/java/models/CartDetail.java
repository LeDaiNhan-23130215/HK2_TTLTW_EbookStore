package models;

public class CartDetail extends Base{
    private int cartID;
    private int bookID;
    private double price;

    public CartDetail(int id) {
        super(id);
    }

    public CartDetail(int id, int cartID, int bookID, double price) {
        super(id);
        this.cartID = cartID;
        this.bookID = bookID;
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

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
