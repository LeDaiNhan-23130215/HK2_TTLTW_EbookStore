package DTO;

import java.math.BigDecimal;

public class EbookProductCardView {
    private int id;
    private String title;
    private double price;
    private String imageLink;

    private BigDecimal finalPrice;
    private String discountLabel;
    private boolean hasDiscount;

    public EbookProductCardView(int id, String title, double price, String imageLink) {
        this.id        = id;
        this.title     = title;
        this.price     = price;
        this.imageLink = imageLink;
        this.hasDiscount = false;
    }

    public void applyDiscount(BigDecimal finalPrice, String discountLabel) {
        this.finalPrice    = finalPrice;
        this.discountLabel = discountLabel;
        this.hasDiscount   = true;
    }

    public int getId()              { return id; }
    public String getTitle()        { return title; }
    public double getPrice()        { return price; }
    public String getImageLink()    { return imageLink; }
    public BigDecimal getFinalPrice()  { return finalPrice; }
    public String getDiscountLabel()   { return discountLabel; }
    public boolean isHasDiscount()     { return hasDiscount; }
}