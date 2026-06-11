package DTO;

import models.Ebook;

public class CartItem {
    private int cartDetailID;
    private Ebook ebook;
    private double priceAtADD;

    private Double originalPrice;
    private String discountLabel;

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

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountLabel() {
        return discountLabel;
    }

    public void setDiscountLabel(String discountLabel) {
        this.discountLabel = discountLabel;
    }

    public boolean isDiscounted() {
        return originalPrice != null
                && discountLabel != null
                && priceAtADD < originalPrice;
    }
}
