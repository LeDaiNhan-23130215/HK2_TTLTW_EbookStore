package models;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Voucher extends Base implements Serializable {
    private String code;
    private String description;
    private String discountType;        // "PERCENT" | "FIXED"
    private double discountValue;
    private double minOrderValue;
    private double maxDiscount;
    private int quantity;            // -1 = vô hạn
    private int usedCount;
    private Timestamp startedAt;
    private Timestamp expiredAt;
    private Integer maxUsesPerUser;      // null = vô hạn
    private boolean isActive;

    public Voucher(int id) {
        super(id);
    }

    @Override public int  getId(){ return this.id; }
    @Override public void setId(int id){ this.id = id;   }

    public String getCode(){ return code; }
    public void setCode(String code){ this.code = code; }

    public String getDescription(){ return description; }
    public void setDescription(String description){ this.description = description; }

    public String getDiscountType(){ return discountType; }
    public void setDiscountType(String discountType){ this.discountType = discountType; }

    public double getDiscountValue(){ return discountValue; }
    public void setDiscountValue(double discountValue){ this.discountValue = discountValue; }

    public double getMinOrderValue(){ return minOrderValue; }
    public void setMinOrderValue(double minOrderValue){ this.minOrderValue = minOrderValue; }

    public double getMaxDiscount(){ return maxDiscount; }
    public void setMaxDiscount(double maxDiscount){ this.maxDiscount = maxDiscount; }

    public int getQuantity(){ return quantity; }
    public void setQuantity(int quantity){ this.quantity = quantity; }

    public int getUsedCount(){ return usedCount; }
    public void setUsedCount(int usedCount){ this.usedCount = usedCount; }

    public Timestamp getStartedAt(){ return startedAt; }
    public void setStartedAt(Timestamp startedAt){ this.startedAt = startedAt; }

    public Timestamp getExpiredAt(){ return expiredAt; }
    public void setExpiredAt(Timestamp expiredAt){ this.expiredAt = expiredAt; }

    public Integer getMaxUsesPerUser(){ return maxUsesPerUser; }
    public void setMaxUsesPerUser(Integer maxUsesPerUser){ this.maxUsesPerUser = maxUsesPerUser; }

    public boolean isActive(){ return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    public String getStatus() {
        if (!isActive) return "INACTIVE";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (startedAt != null && now.before(startedAt)) return "PENDING";
        if (expiredAt != null && now.after(expiredAt))  return "EXPIRED";
        if (quantity != -1 && usedCount >= quantity)    return "USED_UP";
        return "ACTIVE";
    }

    private final SimpleDateFormat DISPLAY_FMT = new SimpleDateFormat("HH:mm dd/MM/yyyy");
    private final SimpleDateFormat INPUT_FMT    = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    public String getFormattedStartedAt() {
        return startedAt != null ? DISPLAY_FMT.format(startedAt) : "—";
    }

    public String getFormattedExpiredAt() {
        return expiredAt != null ? DISPLAY_FMT.format(expiredAt) : "—";
    }

    public String getStartedAtForInput() {
        return startedAt != null ? INPUT_FMT.format(startedAt) : "";
    }

    public String getExpiredAtForInput() {
        return expiredAt != null ? INPUT_FMT.format(expiredAt) : "";
    }

    public String getStatusLabel() {
        switch (getStatus()) {
            case "ACTIVE":   return "Hoạt động";
            case "INACTIVE": return "Tạm ngưng";
            case "PENDING":  return "Chưa bắt đầu";
            case "EXPIRED":  return "Hết hạn";
            case "USED_UP":  return "Hết lượt";
            default:         return getStatus();
        }
    }
}