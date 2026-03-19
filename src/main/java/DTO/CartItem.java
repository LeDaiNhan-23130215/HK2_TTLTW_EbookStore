package DTO;

import models.Ebook;
import models.Image;

public class CartItem {
    private int cartDetailID;
    private Ebook ebook;
    private double priceAtADD;

    public CartItem() {
    }

    public CartItem(int cartDetailID, Ebook ebook, double priceAtADD) {
        this.cartDetailID = cartDetailID;
        this.ebook = ebook;
        this.priceAtADD = priceAtADD;
    }

    public int getCartDetailID() {
        return cartDetailID;
    }

    public void setCartDetailID(int cartDetailID) {
        this.cartDetailID = cartDetailID;
    }

    public Ebook getEbook() {
        return ebook;
    }

    public void setEbook(Ebook ebook) {
        this.ebook = ebook;
    }

    public double getPriceAtADD() {
        return priceAtADD;
    }

    public void setPriceAtADD(double priceAtADD) {
        this.priceAtADD = priceAtADD;
    }
}
