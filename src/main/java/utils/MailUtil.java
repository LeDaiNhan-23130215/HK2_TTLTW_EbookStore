package utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class MailUtil {
    public static void send(String to, String subject, String content) {
        System.out.println("Send mail to: " + to);
        System.out.println("Content: " + content);
    }

    private static final Properties props = new Properties();

    static {
        try (InputStream in =
                     MailUtil.class.getClassLoader()
                             .getResourceAsStream("mail.properties")) {

            props.load(in);

        } catch (Exception e) {
            throw new RuntimeException("Không load được mail.properties", e);
        }
    }

    public static void sendOtp(String toEmail, String otp) {

        final String fromEmail = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
        mailProps.put("mail.smtp.port", props.getProperty("mail.smtp.port"));

        Session session = Session.getInstance(mailProps,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Xác thực đăng ký");
            message.setText(
                    "Mã OTP của bạn là: " + otp + "\nCó hiệu lực trong 2 phút."
            );

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendAccountActivity(String toEmail, String username,
                                           ActivityType activityType) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.auth",            "true");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
        mailProps.put("mail.smtp.port", props.getProperty("mail.smtp.port"));

        Session session = Session.getInstance(mailProps,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, appPassword);
                    }
                });

        try {
            java.time.ZonedDateTime now = java.time.ZonedDateTime
                    .now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
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
                            + "Nếu bạn không thực hiện điều này, tài khoản của bạn có thể đang bị\n"
                            + "truy cập trái phép. Hãy đổi mật khẩu ngay và liên hệ với chúng tôi\n"
                            + "để được hỗ trợ kịp thời:\n\n"
                            + "  Email  : 23130023@st.hcmuaf.edu.vn\n"
                            + "  Hotline: 0332.53.63.86\n\n"
                            + "Trân trọng,\n"
                            + "Đội ngũ EbookStore\n\n"
                            + "―――――――――――――――――――――――――――――――――――――\n"
                            + "Email này được gửi tự động từ hệ thống EbookStore.\n"
                            + "Vui lòng không trả lời email này.\n"
            );

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
