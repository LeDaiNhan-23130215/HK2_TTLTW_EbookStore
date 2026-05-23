<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Forgot Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/forgot-password.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>
<body>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<form action="${pageContext.request.contextPath}/forgot-password" method="post" id="fpForm">
    <div class="container">
        <p class="header">Quên mật khẩu</p>

        <div class="input">

            <%-- ===== STEP 1: NHẬP EMAIL ===== --%>
            <c:if test="${empty param.step}">

                <%-- Lỗi từ server --%>
                <c:if test="${not empty param.error}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span>
                            <c:choose>
                                <c:when test="${param.error == 'emailNotFound'}">
                                    Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại.
                                </c:when>
                                <c:when test="${param.error == 'locked'}">
                                    Email này đã bị khoá do nhập sai quá 5 lần.
                                    Vui lòng thử lại sau <strong>${param.remaining}</strong>.
                                </c:when>
                                <c:otherwise>
                                    Có lỗi xảy ra, vui lòng thử lại.
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </p>
                </c:if>

                <%-- Lỗi từ JS (validate email phía client) --%>
                <p class="fp-error-box" id="fpEmailErr" style="display:none;">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span id="fpEmailErrTxt"></span>
                </p>

                <div class="email-input">
                    <input type="text" name="email" id="fpEmailInput"
                           placeholder="Vui lòng nhập email của bạn"
                           autocomplete="email"/>
                    <button type="submit" name="action" value="sendCode" class="code-btn">
                        Gửi mã
                    </button>
                </div>

                <div class="fp-back-link">
                    <a href="${pageContext.request.contextPath}/login">← Quay lại đăng nhập</a>
                </div>

            </c:if>

            <%-- ===== STEP 2: NHẬP OTP ===== --%>
            <c:if test="${param.step == 'verify'}">

                <input type="hidden" name="step" value="verify"/>

                <%-- Lỗi OTP --%>
                <c:if test="${not empty param.error}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span>
                            <c:choose>
                                <c:when test="${param.error == 'expiredCode'}">
                                    Mã OTP đã hết hạn. Vui lòng nhấn gửi lại để nhận mã mới.
                                </c:when>
                                <c:when test="${param.error == 'invalidCode'}">
                                    Mã OTP không đúng. Vui lòng kiểm tra lại email.
                                </c:when>
                                <c:when test="${param.error == 'locked'}">
                                    Email này đã bị khoá do nhập sai quá 5 lần.
                                    Vui lòng thử lại sau <strong>${param.remaining}</strong>.
                                </c:when>
                                <c:when test="${param.error == 'tooManyAttempts'}">
                                    Bạn đã nhập sai quá 5 lần. Vui lòng gửi lại mã mới.
                                </c:when>
                                <c:when test="${param.error == 'alreadyUsed'}">
                                    Mã OTP này đã được sử dụng. Vui lòng gửi lại để nhận mã mới.
                                </c:when>
                                <c:otherwise>
                                    Có lỗi xảy ra, vui lòng thử lại.
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </p>
                </c:if>

                <%-- Cooldown gửi lại --%>
                <c:if test="${not empty param.resendCooldown}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-clock"></i>
                        <span>
                            Vui lòng chờ thêm <strong>${param.resendCooldown} giây</strong>
                            trước khi gửi lại.
                        </span>
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
                    Mã OTP đã gửi đến: <strong>${sessionScope.resetEmail}</strong>
                </p>

                <div class="code-input">
                    <input type="text" name="confirmCode" id="fpOtpInput"
                           placeholder="Nhập mã OTP 6 chữ số"
                           maxlength="6" autofocus autocomplete="one-time-code"/>
                    <button type="submit" name="action" value="verifyCode"
                            id="fpSubmitBtn" class="code-btn">
                        Xác nhận
                    </button>
                </div>

                <%-- ✅ SỬA BUG 2: dùng otpSecondsRemaining từ server, fallback param.t, cuối cùng mới dùng 300 (5 phút) --%>
                <div class="fp-countdown" id="fpCountdownWrap"
                     data-seconds="${not empty otpSecondsRemaining ? otpSecondsRemaining : (not empty param.t ? param.t : 300)}"
                     data-email="${sessionScope.resetEmail}">
                    Mã hết hạn sau: <span id="fpCountdown">05:00</span>
                </div>

                <div class="fp-resend">
                    <button type="submit" name="action" value="resendCode"
                            id="fpResendBtn" class="fp-resend-btn" disabled>
                        <i class="fa-solid fa-rotate-right"></i>
                        Gửi lại mã OTP
                        <span id="fpResendCooldown">(30s)</span>
                    </button>
                </div>

                <div class="fp-back-link">
                    <a href="${pageContext.request.contextPath}/forgot-password">
                        ← Quay lại nhập email khác
                    </a>
                </div>

            </c:if>

            <%-- ===== STEP 3: ĐỔI MẬT KHẨU ===== --%>
            <c:if test="${param.step == 'reset'}">

                <%-- Lỗi đổi mật khẩu --%>
                <c:if test="${not empty param.error}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span>
                            <c:choose>
                                <c:when test="${param.error == 'passwordEmpty'}">
                                    Vui lòng nhập mật khẩu mới.
                                </c:when>
                                <c:when test="${param.error == 'passwordMismatch'}">
                                    Mật khẩu xác nhận không khớp. Vui lòng nhập lại.
                                </c:when>
                                <c:when test="${param.error == 'weakPassword'}">
                                    Mật khẩu phải có ít nhất 10 ký tự, gồm chữ hoa,
                                    chữ thường, số và ký tự đặc biệt.
                                </c:when>
                                <c:otherwise>
                                    Có lỗi xảy ra, vui lòng thử lại.
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </p>
                </c:if>

                <div class="password-input">

                        <%-- Mật khẩu mới --%>
                    <div class="fp-pw-wrap">
                        <input type="password" name="newPassword" id="fpNewPw"
                               placeholder="Mật khẩu mới"/>
                        <i class="fa-regular fa-eye fp-eye" data-target="fpNewPw"></i>
                    </div>

                        <%-- Xác nhận mật khẩu --%>
                    <div class="fp-pw-wrap">
                        <input type="password" name="confirmPassword" id="fpConfirmPw"
                               placeholder="Xác nhận mật khẩu mới"/>
                        <i class="fa-regular fa-eye fp-eye" data-target="fpConfirmPw"></i>
                    </div>

                        <%-- Checklist yêu cầu mật khẩu --%>
                    <ul class="pw-checklist">
                        <li id="ck-len">  <i class="fa-solid fa-circle-xmark"></i> Ít nhất 10 ký tự</li>
                        <li id="ck-upper"><i class="fa-solid fa-circle-xmark"></i> Có chữ hoa (A–Z)</li>
                        <li id="ck-lower"><i class="fa-solid fa-circle-xmark"></i> Có chữ thường (a–z)</li>
                        <li id="ck-digit"><i class="fa-solid fa-circle-xmark"></i> Có chữ số (0–9)</li>
                        <li id="ck-spec"> <i class="fa-solid fa-circle-xmark"></i> Có ký tự đặc biệt (!@#$%^&*...)</li>
                    </ul>

                        <%-- Thanh đo độ mạnh --%>
                    <div class="fp-strength-wrap">
                        <div id="fpStrengthBar"></div>
                        <small id="fpStrengthTxt"></small>
                    </div>

                    <button type="submit" name="action" value="resetPassword" class="confirm-btn">
                        Đổi mật khẩu
                    </button>

                </div>

            </c:if>

        </div>
    </div>
</form>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/forgot-password.js"></script>
</body>
</html>