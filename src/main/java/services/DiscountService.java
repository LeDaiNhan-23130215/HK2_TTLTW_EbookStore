package services;

import DAO.DiscountDAO;
import models.Discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;


public class DiscountService {

    private final DiscountDAO dao = new DiscountDAO();

    public DiscountResult calculateBestDiscount(int ebookId, double originalPrice) {
        if (originalPrice <= 0) {
            return new DiscountResult(BigDecimal.ZERO, null);
        }
        dao.expireEnded();

        List<Discount> discounts = dao.findActiveForEbook(ebookId);
        if (discounts.isEmpty()) {
            return new DiscountResult(BigDecimal.valueOf(originalPrice), null);
        }

        BigDecimal base      = BigDecimal.valueOf(originalPrice);
        BigDecimal bestPrice = base;
        Discount   best      = null;

        for (Discount d : discounts) {
            BigDecimal after;
            if ("PERCENT".equalsIgnoreCase(d.getDiscountType())) {
                BigDecimal reduction = base
                        .multiply(d.getDiscountValue())
                        .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
                after = base.subtract(reduction);
            } else { // FIXED
                after = base.subtract(d.getDiscountValue());
            }
            if (after.compareTo(BigDecimal.ZERO) < 0) after = BigDecimal.ZERO;

            if (after.compareTo(bestPrice) < 0) {
                bestPrice = after;
                best      = d;
            }
        }

        return new DiscountResult(bestPrice, best);
    }
    public String formatVND(BigDecimal value) {
        if (value == null) return "0₫";
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.getDefault());
        sym.setGroupingSeparator('.');
        return new DecimalFormat("#,###", sym).format(value) + "₫";
    }

    public String getDiscountLabel(Discount d) {
        if (d == null) return "";
        if ("PERCENT".equalsIgnoreCase(d.getDiscountType())) {
            return "-" + d.getDiscountValue().stripTrailingZeros().toPlainString() + "%";
        }
        return "-" + formatVND(d.getDiscountValue());
    }

}
