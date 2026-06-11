package services;


import models.Voucher;

import java.sql.Timestamp;

public class VoucherService {
    public String validateVoucher(Voucher voucher, double orderTotal) {
        if(voucher == null) {
            return "Voucher không tồn tại";
        }
        if(!voucher.isActive()) {
            return "Voucher đã bị khóa";
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(now.after(voucher.getExpiredAt())) {
            return "Voucher đã hết hạn";
        }
        if(voucher.getUsedCount() >= voucher.getQuantity()) {
            return "Voucher đã hết lượt sử dụng";
        }
        if(orderTotal < voucher.getMinOrderValue()) {
            return "Đơn hàng chưa đủ điều kiện";
        }
        return null;
    }

    public double calculateDiscount(Voucher voucher, double total) {
        double discount = 0;
        if(voucher.getDiscountType().equals("PERCENT")) {
            discount = total * (voucher.getDiscountValue() / 100);
            if(voucher.getMaxDiscount() > 0) {
                discount = Math.min(discount, voucher.getMaxDiscount());
            }
        } else {
            discount = voucher.getDiscountValue();
        }
        return discount;
    }
}