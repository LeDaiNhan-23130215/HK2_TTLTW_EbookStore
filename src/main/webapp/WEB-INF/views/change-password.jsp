<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user-infor.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/change-password.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="container">
    <%-- ===== SIDEBAR TRÁI ===== --%>
    <div class="box-left">
        <div class="subTitle">
            <h5>TRANG TÀI KHOẢN</h5>
            <p><b>Xin chào, </b>
                <b style="color:hsl(0,100%,60%);font-weight:550">${sessionScope.userName}</b> !
            </p>
        </div>
        <ul class="option-list">
            <li><a href="${pageContext.request.contextPath}/userInformation">Thông tin tài khoản</a></li>
            <li><a href="${pageContext.request.contextPath}/your-order">Đơn hàng của bạn</a></li>
            <li><a href="${pageContext.request.contextPath}/book-shelf">Tủ sách của bạn</a></li>
            <li><a href="${pageContext.request.contextPath}/wishlist">Danh mục yêu thích</a></li>
            <li class="selected"><a href="#">${isOAuthOnly ? 'Tạo mật khẩu' : 'Đổi mật khẩu'}</a></li>
        </ul>
    </div>

    <%-- ===== NỘI DUNG PHẢI ===== --%>
    <div class="box-right">
        <div class="subTitle">
            <h5>${isOAuthOnly ? 'TẠO MẬT KHẨU' : 'ĐỔI MẬT KHẨU'}</h5>
        </div>

        <%-- Wrapper dùng lại layout của forgot-password --%>
        <form action="${pageContext.request.contextPath}/change-password" method="post" id="cpForm">
            <div class="cp-inner">

                <%-- ========================================================
                     Hiện thông báo OAuth (nếu cần) + nút "Gửi mã xác nhận"
                     ======================================================== --%>
                <c:if test="${empty param.step}">

                    <%-- OAuth info banner --%>
                    <c:if test="${isOAuthOnly}">
                        <div class="cp-oauth-notice">
                            <i class="fa-brands fa-google"></i>
                            <span>Tài khoản của bạn hiện chỉ đăng nhập được bằng Google.
                                  Bạn có thể tạo thêm mật khẩu để đăng nhập trực tiếp.</span>
                        </div>
                    </c:if>

                    <%-- Lỗi locked --%>
                    <c:if test="${param.error == 'locked'}">
                        <p class="fp-error-box">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <span>Tài khoản tạm khoá do nhập sai quá nhiều lần.
                                  Vui lòng thử lại sau <strong>${param.remaining}</strong>.</span>
                        </p>
                    </c:if>

                    <p class="cp-send-hint">
                        Nhấn <strong>Gửi mã xác nhận</strong> — chúng tôi sẽ gửi mã OTP đến
                        email <strong>${sessionScope.user.email}</strong> của bạn.
                    </p>

                    <button type="submit" name="action" value="sendOtp" class="cp-send-btn">
                        <i class="fa-solid fa-envelope"></i> Gửi mã xác nhận
                    </button>

                </c:if>

                <%-- ========================================================
                     BƯỚC VERIFY — Nhập OTP
                     ======================================================== --%>
                <c:if test="${param.step == 'verify'}">
                    <input type="hidden" name="step" value="verify"/>

                    <%-- Lỗi OTP --%>
                    <c:if test="${not empty param.error}">
                        <p class="fp-error-box">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <span>
                                <c:choose>
                                    <c:when test="${param.error == 'expiredCode'}">Mã OTP đã hết hạn. Vui lòng gửi lại để nhận mã mới.</c:when>
                                    <c:when test="${param.error == 'invalidCode'}">Mã OTP không đúng. Vui lòng kiểm tra lại email.</c:when>
                                    <c:when test="${param.error == 'locked'}">Tài khoản tạm khoá do nhập sai quá nhiều lần. Thử lại sau <strong>${param.remaining}</strong>.</c:when>
                                    <c:when test="${param.error == 'tooManyAttempts'}">Bạn đã nhập sai quá 5 lần. Vui lòng gửi lại mã mới.</c:when>
                                    <c:when test="${param.error == 'alreadyUsed'}">Mã OTP này đã được sử dụng. Vui lòng gửi lại để nhận mã mới.</c:when>
                                    <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                                </c:choose>
                            </span>
                        </p>
                    </c:if>

                    <%-- Cooldown gửi lại --%>
                    <c:if test="${not empty param.resendCooldown}">
                        <p class="fp-error-box">
                            <i class="fa-solid fa-clock"></i>
                            <span>Vui lòng chờ thêm <strong>${param.resendCooldown} giây</strong> trước khi gửi lại.</span>
                        </p>
                    </c:if>

                    <%-- Gửi lại thành công --%>
                    <c:if test="${param.resent == 'true'}">
                        <p class="fp-success-box">
                            <i class="fa-solid fa-circle-check"></i>
                            <span>Đã gửi lại mã OTP mới. Hiệu lực 5 phút.</span>
                        </p>
                    </c:if>

                    <p class="fp-sent-to">
                        Mã OTP đã gửi đến: <strong>${sessionScope.user.email}</strong>
                    </p>

                    <div class="code-input">
                        <input type="text" name="confirmCode" id="cpOtpInput"
                               placeholder="Nhập mã OTP 6 chữ số"
                               maxlength="6" autofocus autocomplete="one-time-code"/>
                        <button type="submit" name="action" value="verifyOtp"
                                id="cpSubmitBtn" class="code-btn">
                            Xác nhận
                        </button>
                    </div>

                    <div class="fp-countdown" id="cpCountdownWrap"
                         data-seconds="${not empty otpSecondsRemaining ? otpSecondsRemaining : (not empty param.t ? param.t : 300)}"
                         data-email="${sessionScope.user.email}">
                        Mã hết hạn sau: <span id="cpCountdown">05:00</span>
                    </div>

                    <div class="fp-resend">
                        <button type="submit" name="action" value="resendOtp"
                                id="cpResendBtn" class="fp-resend-btn" disabled>
                            <i class="fa-solid fa-rotate-right"></i>
                            Gửi lại mã OTP
                            <span id="cpResendCooldown">(30s)</span>
                        </button>
                    </div>

                    <div class="fp-back-link">
                        <a href="${pageContext.request.contextPath}/change-password">← Quay lại</a>
                    </div>
                </c:if>

                <%-- ========================================================
                     BƯỚC RESET — Nhập mật khẩu mới
                     ======================================================== --%>
                <c:if test="${param.step == 'reset'}">

                    <%-- Lỗi --%>
                    <c:if test="${not empty param.error}">
                        <p class="fp-error-box">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <span>
                                <c:choose>
                                    <c:when test="${param.error == 'passwordEmpty'}">Vui lòng nhập mật khẩu mới.</c:when>
                                    <c:when test="${param.error == 'passwordMismatch'}">Mật khẩu xác nhận không khớp. Vui lòng nhập lại.</c:when>
                                    <c:when test="${param.error == 'weakPassword'}">Mật khẩu phải có ít nhất 10 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.</c:when>
                                    <c:when test="${param.error == 'oldPasswordEmpty'}">Vui lòng nhập mật khẩu cũ.</c:when>
                                    <c:when test="${param.error == 'oldPasswordWrong'}">Mật khẩu cũ không đúng. Vui lòng kiểm tra lại.</c:when>
                                    <c:when test="${param.error == 'dbError'}">Đổi mật khẩu thất bại. Vui lòng thử lại sau.</c:when>
                                    <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                                </c:choose>
                            </span>
                        </p>
                    </c:if>

                    <div class="password-input">

                            <%-- Mật khẩu cũ — ẨN nếu OAuth thuần --%>
                        <c:if test="${!isOAuthOnly}">
                            <div class="fp-pw-wrap">
                                <input type="password" name="oldPassword" id="cpOldPw"
                                       placeholder="Mật khẩu cũ" autocomplete="current-password"/>
                                <i class="fa-regular fa-eye fp-eye" data-target="cpOldPw"></i>
                            </div>
                        </c:if>

                            <%-- Mật khẩu mới --%>
                        <div class="fp-pw-wrap">
                            <input type="password" name="newPassword" id="cpNewPw"
                                   placeholder="${isOAuthOnly ? 'Mật khẩu mới' : 'Mật khẩu mới'}"
                                   autocomplete="new-password"/>
                            <i class="fa-regular fa-eye fp-eye" data-target="cpNewPw"></i>
                        </div>

                            <%-- Xác nhận mật khẩu --%>
                        <div class="fp-pw-wrap">
                            <input type="password" name="confirmPassword" id="cpConfirmPw"
                                   placeholder="Xác nhận mật khẩu mới" autocomplete="new-password"/>
                            <i class="fa-regular fa-eye fp-eye" data-target="cpConfirmPw"></i>
                        </div>

                            <%-- Checklist yêu cầu--%>
                        <ul class="pw-checklist">
                            <li id="ck-len">  <i class="fa-solid fa-circle-xmark"></i> Ít nhất 10 ký tự</li>
                            <li id="ck-upper"><i class="fa-solid fa-circle-xmark"></i> Có chữ hoa (A–Z)</li>
                            <li id="ck-lower"><i class="fa-solid fa-circle-xmark"></i> Có chữ thường (a–z)</li>
                            <li id="ck-digit"><i class="fa-solid fa-circle-xmark"></i> Có chữ số (0–9)</li>
                            <li id="ck-spec"> <i class="fa-solid fa-circle-xmark"></i> Có ký tự đặc biệt (!@#$%...)</li>
                        </ul>

                            <%-- Thanh độ mạnh --%>
                        <div class="fp-strength-wrap">
                            <div id="cpStrengthBar"></div>
                            <small id="cpStrengthTxt"></small>
                        </div>

                        <button type="submit" name="action" value="changePassword" class="confirm-btn">
                                ${isOAuthOnly ? 'Tạo mật khẩu' : 'Đổi mật khẩu'}
                        </button>

                    </div>
                </c:if>

            </div>
        </form>
    </div>
</div><

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/change-password.js"></script>
</body>
</html>
