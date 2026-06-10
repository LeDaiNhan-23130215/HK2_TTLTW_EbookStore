package services;

import models.Discount;
import java.math.BigDecimal;

public class DiscountResult {

    private final BigDecimal finalPrice;
    private final Discount   bestDiscount;

    public DiscountResult(BigDecimal finalPrice, Discount bestDiscount) {
        this.finalPrice   = finalPrice;
        this.bestDiscount = bestDiscount;
    }

    public BigDecimal getFinalPrice(){
        return finalPrice;
    }

    public Discount  getBestDiscount(){
        return bestDiscount;
    }

    public boolean   hasDiscount(){
        return bestDiscount != null;
    }
}
