package services;


import DAO.VoucherDAO;
import models.Voucher;

import java.sql.Timestamp;

public class VoucherService {

    private final VoucherDAO voucherDAO = new VoucherDAO();

    public String validateVoucher(Voucher voucher, double orderTotal, int userId) {
        if (orderTotal <= 0) {
            return "Không áp dụng mã giảm giá cho đơn hàng 0đ";
        }
        if (voucher == null) {
            return "Mã giảm giá không tồn tại";
        }
        if (!voucher.isActive()) {
            return "Mã giảm giá đã bị tạm ngưng";
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (voucher.getStartedAt() != null && now.before(voucher.getStartedAt())) {
            return "Mã giảm giá chưa đến thời gian áp dụng";
        }
        if (voucher.getExpiredAt() != null && now.after(voucher.getExpiredAt())) {
            return "Mã giảm giá đã hết hạn";
        }
        if (voucher.getQuantity() != -1 && voucher.getUsedCount() >= voucher.getQuantity()) {
            return "Mã giảm giá đã hết lượt sử dụng";
        }
        if (voucher.getMinOrderValue() > 0 && orderTotal < voucher.getMinOrderValue()) {
            return "Đơn hàng chưa đủ điều kiện để áp dụng mã giảm giá này";
        }
        if (voucher.getMaxUsesPerUser() != null) {
            int used = voucherDAO.countUsageByUser(voucher.getId(), userId);
            if (used >= voucher.getMaxUsesPerUser()) {
                return "Bạn đã sử dụng vượt số lần cho phép của mã giảm giá này";
            }
        }
        return null;
    }

    public String validateVoucher(Voucher voucher, double orderTotal) {
        return validateVoucher(voucher, orderTotal, -1);
    }

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS  = "0123456789";
    private static final String ALNUM  = LETTERS + DIGITS;

    public String generateUniqueCode() {
        String code;
        int attempts = 0;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(ALNUM.charAt((int) (Math.random() * ALNUM.length())));
            }
            code = sb.toString();
            attempts++;
        } while (voucherDAO.existsByCode(code) && attempts < 50);
        return code;
    }

    public double calculateDiscount(Voucher voucher, double total) {
        double discount;
        if (voucher.getDiscountType().equals("PERCENT")) {
            discount = total * (voucher.getDiscountValue() / 100);
            if (voucher.getMaxDiscount() > 0) {
                discount = Math.min(discount, voucher.getMaxDiscount());
            }
        } else {
            discount = voucher.getDiscountValue();
        }
        return Math.min(discount, total);
    }
}
