package utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class VNPayUtil {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = VNPayUtil.class.getClassLoader()
                .getResourceAsStream("vnpay.properties")) {
            if (in == null) throw new RuntimeException("Không tìm thấy vnpay.properties");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi load vnpay.properties", e);
        }
    }

    public static String getTmnCode()    { return props.getProperty("vnp.tmnCode"); }
    public static String getHashSecret() { return props.getProperty("vnp.hashSecret"); }
    public static String getPayUrl()     { return props.getProperty("vnp.payUrl"); }
    public static String getReturnUrl()  { return props.getProperty("vnp.returnUrl"); }

    /**
     * Tạo URL thanh toán VNPAY
     * @param amountVnd  Số tiền VND (chưa nhân 100)
     * @param txnRef     Mã giao dịch duy nhất
     * @param orderInfo  Mô tả không dấu, không ký tự đặc biệt
     * @param clientIp   IP khách hàng
     */
    public static String buildPaymentUrl(long amountVnd, String txnRef,
                                         String orderInfo, String clientIp) {
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version",   "2.1.0");
        params.put("vnp_Command",   "pay");
        params.put("vnp_TmnCode",   getTmnCode());
        params.put("vnp_Amount",    String.valueOf(amountVnd * 100));
        params.put("vnp_CurrCode",  "VND");
        params.put("vnp_TxnRef",    txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale",    "vn");
        params.put("vnp_ReturnUrl", getReturnUrl());
        params.put("vnp_IpAddr",    clientIp);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        params.put("vnp_CreateDate", sdf.format(cal.getTime()));
        cal.add(Calendar.MINUTE, 15);
        params.put("vnp_ExpireDate", sdf.format(cal.getTime()));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query    = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            String k = e.getKey();
            String v = e.getValue();
            hashData.append(k).append('=')
                    .append(URLEncoder.encode(v, StandardCharsets.US_ASCII));
            query.append(URLEncoder.encode(k, StandardCharsets.US_ASCII))
                 .append('=')
                 .append(URLEncoder.encode(v, StandardCharsets.US_ASCII));
            if (it.hasNext()) { hashData.append('&'); query.append('&'); }
        }

        String secureHash = hmacSHA512(getHashSecret(), hashData.toString());
        return getPayUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    // Kiểm tra chữ ký callback từ VNPAY
    public static boolean verifyHash(Map<String, String[]> paramMap) {
        String receivedHash = paramMap.containsKey("vnp_SecureHash")
                ? paramMap.get("vnp_SecureHash")[0] : "";

        Map<String, String> fields = new TreeMap<>();
        for (Map.Entry<String, String[]> e : paramMap.entrySet()) {
            String k = e.getKey();
            if (!"vnp_SecureHash".equals(k) && !"vnp_SecureHashType".equals(k)) {
                fields.put(k, e.getValue()[0]);
            }
        }

        StringBuilder hashData = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = fields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            hashData.append(e.getKey()).append('=')
                    .append(URLEncoder.encode(e.getValue(), StandardCharsets.US_ASCII));
            if (it.hasNext()) hashData.append('&');
        }

        String computed = hmacSHA512(getHashSecret(), hashData.toString());
        return computed.equalsIgnoreCase(receivedHash);
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi HMAC-SHA512", e);
        }
    }

    //Lấy IP thực của client (qua proxy/nginx)
    public static String getClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getRemoteAddr();
        if (ip != null && ip.contains(",")) ip = ip.split(",")[0].trim();
        return (ip == null || ip.isBlank()) ? "127.0.0.1" : ip;
    }

    // Tạo mã txnRef duy nhất: 12 chữ số
    public static String generateTxnRef() {
        long ts = System.currentTimeMillis() % 100_000_000L;
        int  r  = (int)(Math.random() * 9000) + 1000;
        return ts + "" + r;
    }
}
