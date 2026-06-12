package utils;

import DTO.CartItem;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import models.Checkout;
import models.PaymentMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class MailUtil {
    private static final Logger logger     = LoggerFactory.getLogger(MailUtil.class);
    private static final String LOG_PREFIX = "[MAIL_UTIL]";
    private static final Properties props  = new Properties();

    static {
        try (InputStream in = MailUtil.class.getClassLoader()
                .getResourceAsStream("mail.properties")) {
            if (in == null) throw new RuntimeException("Không tìm thấy mail.properties");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Không load được mail.properties", e);
        }
    }

    //OTP
    public static void sendOtp(String toEmail, String otp, String subject) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        Properties mailProps = buildSmtpProps();
        Session session = Session.getInstance(mailProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "EbookStore"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[EbookStore] " + subject);
            message.setText(
                    "Mã xác thực của bạn là: " + otp + "\n"
                            + "Mã có hiệu lực trong 5 phút.\n\n"
                            + "Vui lòng không chia sẻ mã này với bất kỳ ai.\n"
                            + "Nếu bạn không yêu cầu mã này, hãy bỏ qua email này.\n\n"
                            + "Trân trọng,\nĐội ngũ EbookStore"
            );
            Transport.send(message);
            logger.info("{} OTP sent to '{}'.", LOG_PREFIX, toEmail);
        } catch (Exception e) {
            logger.error("{} Failed to send OTP to '{}': ", LOG_PREFIX, toEmail, e);
        }
    }

    // Account Activity
    public static void sendAccountActivity(String toEmail, String username,
                                           ActivityType activityType) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        Properties mailProps = buildSmtpProps();
        Session session = Session.getInstance(mailProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            java.time.ZonedDateTime now = java.time.ZonedDateTime.now(
                    java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            String timeStr = now.format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "EbookStore"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[EbookStore] " + activityType.getSubject());
            message.setText(
                    "Xin chào " + username + ",\n\n"
                            + "Chúng tôi ghi nhận một hoạt động vừa diễn ra trên tài khoản của bạn:\n\n"
                            + "  Hoạt động : " + activityType.getActivityLabel() + "\n"
                            + "  Tài khoản : " + username + "\n"
                            + "  Email     : " + toEmail + "\n"
                            + "  Thời gian : " + timeStr + "\n\n"
                            + "Nếu đây là bạn, bạn không cần thực hiện thêm bất kỳ thao tác nào.\n\n"
                            + "Nếu bạn không thực hiện điều này, hãy đổi mật khẩu ngay.\n\n"
                            + "Trân trọng,\nĐội ngũ EbookStore\n\n"
                            + "―――――――――――――――――――――――――――――――――――――\n"
                            + "Email này được gửi tự động từ hệ thống EbookStore.\n"
                            + "Vui lòng không trả lời email này.\n"
            );
            Transport.send(message);
            logger.info("{} Activity mail sent to '{}'.", LOG_PREFIX, toEmail);
        } catch (Exception e) {
            logger.error("{} Failed to send activity mail to '{}': ", LOG_PREFIX, toEmail, e);
        }
    }

    //Order Confirmation
    public static void sendOrderConfirmation(String toEmail, String username,
                                             Checkout checkout,
                                             List<CartItem> items,
                                             PaymentMethod paymentMethod) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        Properties mailProps = buildSmtpProps();
        Session session = Session.getInstance(mailProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        try {
            StringBuilder sb = new StringBuilder();
            for (CartItem item : items) {
                sb.append("  - ").append(item.getEbook().getTitle())
                        .append("  →  ").append(nf.format((long) item.getPriceAtADD())).append(" đ\n");
            }

            java.time.ZonedDateTime now = java.time.ZonedDateTime.now(
                    java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            String timeStr = now.format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));

            String pmName = (paymentMethod != null) ? paymentMethod.getName() : "VNPAY";

            String body =
                    "Xin chào " + username + ",\n\n"
                            + "Đơn hàng của bạn đã được xử lý thành công. Chi tiết đơn hàng:\n\n"
                            + "  Mã đơn hàng: #" + checkout.getId() + "\n"
                            + "  Thời gian: " + timeStr + "\n"
                            + "  Phương thức TT: " + pmName + "\n"
                            + "  Trạng thái: Thành công\n\n"
                            + "Danh sách sản phẩm:\n"
                            + sb
                            + "\n"
                            + "  Tổng tiền thanh toán: " + nf.format((long) checkout.getTotalAmount()) + " đ\n\n"
                            + "Sách đã được thêm vào tủ sách của bạn. Chúc bạn đọc sách vui vẻ!\n\n"
                            + "Nếu có bất kỳ thắc mắc, vui lòng liên hệ:\n"
                            + "  Email  : ebookstorenlu@gmail.com\n\n"
                            + "Trân trọng,\n"
                            + "Đội ngũ EbookStore\n\n"
                            + "―――――――――――――――――――――――――――――――――――――\n"
                            + "Email này được gửi tự động. Vui lòng không trả lời.\n";

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "EbookStore"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[EbookStore] Đơn hàng #" + checkout.getId());
            message.setText(body);
            Transport.send(message);
            logger.info("{} Order confirmation sent to '{}', checkoutId={}.",
                    LOG_PREFIX, toEmail, checkout.getId());
        } catch (Exception e) {
            logger.error("{} Failed to send order confirmation to '{}': ", LOG_PREFIX, toEmail, e);
        }
    }

    private static Properties buildSmtpProps() {
        Properties p = new Properties();
        p.put("mail.smtp.auth",            "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
        p.put("mail.smtp.port", props.getProperty("mail.smtp.port"));
        return p;
    }
}