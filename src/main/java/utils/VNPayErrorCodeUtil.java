package utils;

import java.util.HashMap;
import java.util.Map;

public class VNPayErrorCodeUtil {

    private static final Map<String, String> MESSAGES = new HashMap<>();

    static {
        MESSAGES.put("00", "Giao dịch thành công.");
        MESSAGES.put("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).");
        MESSAGES.put("09", "Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.");
        MESSAGES.put("10", "Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.");
        MESSAGES.put("11", "Đã hết hạn chờ thanh toán. Vui lòng thực hiện lại giao dịch.");
        MESSAGES.put("12", "Thẻ/Tài khoản của khách hàng bị khóa.");
        MESSAGES.put("13", "Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Vui lòng thực hiện lại giao dịch.");
        MESSAGES.put("24", "Khách hàng đã hủy giao dịch.");
        MESSAGES.put("51", "Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
        MESSAGES.put("65", "Tài khoản của quý khách đã vượt quá hạn mức giao dịch trong ngày.");
        MESSAGES.put("75", "Ngân hàng thanh toán đang bảo trì.");
        MESSAGES.put("79", "Khách hàng nhập sai mật khẩu thanh toán quá số lần quy định. Vui lòng thực hiện lại giao dịch.");
        MESSAGES.put("99", "Có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại.");
    }

    public static String getDescription(String responseCode) {
        if (responseCode == null) return null;
        return MESSAGES.getOrDefault(responseCode, MESSAGES.get("99"));
    }
}