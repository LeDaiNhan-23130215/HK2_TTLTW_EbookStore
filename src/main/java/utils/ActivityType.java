package utils;

public enum ActivityType {

    LOGIN(
            "Thông báo đăng nhập tài khoản",
            "Đăng nhập thành công"
    ),
    RESET_PASSWORD(
            "Mật khẩu của bạn vừa được đặt lại",
            "Đặt lại mật khẩu thành công"
    ),
    CHANGE_PASSWORD(
            "Mật khẩu của bạn vừa được thay đổi",
            "Đổi mật khẩu thành công"
    ),
    CHANGE_USERNAME(
            "Tên người dùng của bạn vừa được cập nhật",
            "Đổi tên người dùng thành công"
    ),
    CHANGE_EMAIL(
            "Địa chỉ email của bạn vừa được cập nhật",
            "Đổi địa chỉ email thành công"
    ),
    CHANGE_PHONE(
            "Số điện thoại của bạn vừa được cập nhật",
            "Đổi số điện thoại thành công"
    ),
    LINK_GOOGLE(
            "Tài khoản Google vừa được liên kết",
            "Liên kết tài khoản Google thành công"
    );

    private final String subject;
    private final String activityLabel;

    ActivityType(String subject, String activityLabel) {
        this.subject       = subject;
        this.activityLabel = activityLabel;
    }

    public String getSubject()       { return subject; }
    public String getActivityLabel() { return activityLabel; }
}