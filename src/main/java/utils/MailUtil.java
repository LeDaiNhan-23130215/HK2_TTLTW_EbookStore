package utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    private static final String LOG_PREFIX = "[MAIL_UTIL]";
    private static final Properties props = new Properties();

    static {
        try (InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties")) {
            if (in == null) {
                logger.error("{} Critical Error: Unable to locate 'mail.properties' configuration file in classpath resources.", LOG_PREFIX);
                throw new RuntimeException("Could not find mail.properties resource file");
            }
            props.load(in);
            logger.info("{} SMTP configuration parameters loaded successfully from mail.properties.", LOG_PREFIX);
        } catch (Exception e) {
            logger.error("{} Fatal Application Startup Failure: Configuration loading failed. Trace context: ", LOG_PREFIX, e);
            throw new RuntimeException("Không load được mail.properties", e);
        }
    }

    public static void sendOtp(String toEmail, String otp, String subject) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        logger.info("{} Initializing registration OTP dispatch sequence targeting recipient address: '{}'.", LOG_PREFIX, toEmail);

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

        long startTime = System.currentTimeMillis();
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
                            + "Trân trọng,\n"
                            + "Đội ngũ EbookStore"
            );
            
            Transport.send(message);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("{} OTP mail successfully delivered to '{}'. Dispatch operation duration: {}ms.", LOG_PREFIX, toEmail, duration);
        } catch (Exception e) {
            logger.error("{} SMTP Transport Transmission Failure: Failed to dispatch OTP token package to recipient '{}': ", LOG_PREFIX, toEmail, e);
        }
    }

    public static void sendAccountActivity(String toEmail, String username, ActivityType activityType) {
        final String fromEmail   = props.getProperty("mail.username");
        final String appPassword = props.getProperty("mail.app.password");

        logger.info("{} Triggering security notification mail dispatch for Activity: '{}', Account: '{}'.", 
                LOG_PREFIX, activityType.name(), username);

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

        long startTime = System.currentTimeMillis();
        try {
            java.time.ZonedDateTime now = java.time.ZonedDateTime.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            String timeStr = now.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy"));

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
            long duration = System.currentTimeMillis() - startTime;
            logger.info("{} Security audit notification packet dispatched successfully to '{}'. Execution latency: {}ms.", LOG_PREFIX, toEmail, duration);
        } catch (Exception e) {
            logger.error("{} SMTP Transport Security Alert Failure: Unable to deliver account activity updates to target destination '{}': ", LOG_PREFIX, toEmail, e);
        }
    }
}