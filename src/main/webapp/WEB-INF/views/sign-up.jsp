<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Đăng ký tài khoản</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/sign-up.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<form action="${pageContext.request.contextPath}/sign-up" method="post" id="suForm" novalidate>
  <div class="container">

    <p class="header">Đăng ký</p>

    <div class="input">

      <%-- ===== STEP 1: NHẬP THÔNG TIN ===== --%>
      <c:if test="${empty param.step}">

        <%-- Lỗi từ server --%>
        <c:if test="${not empty error_msg}">
          <p class="su-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>${error_msg}</span>
          </p>
        </c:if>

        <%-- Lỗi từ JS --%>
        <p class="su-error-box" id="suStep1Err" style="display:none;">
          <i class="fa-solid fa-circle-exclamation"></i>
          <span id="suStep1ErrTxt"></span>
        </p>

        <%-- Tên người dùng --%>
        <div class="su-field-wrap su-wide">
          <input type="text" name="fname" id="fname"
                 placeholder="Tên người dùng *"
                 value="${not empty fname ? fname : param.fname}"
                 autocomplete="username"/>
          <span class="su-field-err" id="err-fname"></span>
        </div>

        <%-- Email --%>
        <div class="su-field-wrap su-wide">
          <input type="text" name="userAndEmail" id="userAndEmail"
                 placeholder="Email *"
                 value="${not empty userAndEmail ? userAndEmail : param.userAndEmail}"
                 autocomplete="email"/>
          <span class="su-field-err" id="err-email"></span>
        </div>

        <%-- Số điện thoại (không bắt buộc) --%>
        <div class="su-field-wrap su-wide">
          <input type="tel" name="phoneNumber" id="phoneNumber"
                 placeholder="Số điện thoại"
                 value="${not empty phoneNumber ? phoneNumber : param.phoneNumber}"
                 autocomplete="tel"/>
          <span class="su-field-err" id="err-phone"></span>
        </div>

        <button type="submit" name="action" value="sendInfo" class="su-btn">
          Tiếp tục
        </button>

        <p class="su-divider">Hoặc đăng ký bằng</p>

        <div class="su-social-wrap">
          <a href="${pageContext.request.contextPath}/oauth/google/init" class="google-btn">
            <span class="google-icon-wrap">
              <svg class="google-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
                <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
                <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z" fill="#FBBC05"/>
                <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
              </svg>
            </span>
            <span class="google-btn-text">Đăng ký bằng Google</span>
          </a>
        </div>

        <div class="su-back-link">
          <a href="${pageContext.request.contextPath}/login">← Đã có tài khoản? Đăng nhập</a>
        </div>

      </c:if>

      <%-- ===== STEP 2: NHẬP OTP ===== --%>
      <c:if test="${param.step == 'verify'}">

        <input type="hidden" name="step" value="verify"/>

        <%-- Lỗi OTP --%>
        <c:if test="${not empty param.error}">
          <p class="su-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>
              <c:choose>
                <c:when test="${param.error == 'expiredCode'}">Mã OTP đã hết hạn. Vui lòng nhấn gửi lại để nhận mã mới.</c:when>
                <c:when test="${param.error == 'invalidCode'}">Mã OTP không đúng. Vui lòng kiểm tra lại email.</c:when>
                <c:when test="${param.error == 'alreadyUsed'}">Mã OTP này đã được sử dụng. Vui lòng gửi lại để nhận mã mới.</c:when>
                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
              </c:choose>
            </span>
          </p>
        </c:if>

        <%-- Cooldown gửi lại --%>
        <c:if test="${not empty param.resendCooldown}">
          <p class="su-error-box">
            <i class="fa-solid fa-clock"></i>
            <span>Vui lòng chờ thêm <strong>${param.resendCooldown} giây</strong> trước khi gửi lại.</span>
          </p>
        </c:if>

        <%-- Gửi lại thành công --%>
        <c:if test="${param.resent == 'true'}">
          <p class="su-success-box">
            <i class="fa-solid fa-circle-check"></i>
            <span>Đã gửi lại mã OTP mới. Hiệu lực 5 phút.</span>
          </p>
        </c:if>

        <p class="su-sent-to">
          Mã OTP đã gửi đến: <strong>${sessionScope.signup_email}</strong>
        </p>

        <div class="su-code-input">
          <input type="text" name="confirmCode" id="suOtpInput"
                 placeholder="Nhập mã OTP 6 chữ số"
                 maxlength="6" autofocus autocomplete="one-time-code"/>
          <button type="submit" name="action" value="verifyCode"
                  id="suSubmitBtn" class="su-code-btn">
            Xác nhận
          </button>
        </div>

        <div class="su-countdown" id="suCountdownWrap"
             data-seconds="${not empty otpSecondsRemaining ? otpSecondsRemaining : (not empty param.t ? param.t : 300)}"
             data-email="${sessionScope.signup_email}">
          Mã hết hạn sau: <span id="suCountdown">05:00</span>
        </div>

        <div class="su-resend">
          <button type="submit" name="action" value="resendCode"
                  id="suResendBtn" class="su-resend-btn" disabled>
            <i class="fa-solid fa-rotate-right"></i>
            Gửi lại mã OTP
            <span id="suResendCooldown">(30s)</span>
          </button>
        </div>

        <div class="su-back-link">
          <a href="${pageContext.request.contextPath}/sign-up">← Quay lại nhập thông tin</a>
        </div>

      </c:if>

      <%-- ===== STEP 3: TẠO MẬT KHẨU ===== --%>
      <c:if test="${param.step == 'password'}">

        <input type="hidden" name="step" value="password"/>

        <c:if test="${not empty param.error}">
          <p class="su-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>
              <c:choose>
                <c:when test="${param.error == 'passwordEmpty'}">Vui lòng nhập mật khẩu mới.</c:when>
                <c:when test="${param.error == 'passwordMismatch'}">Mật khẩu xác nhận không khớp. Vui lòng nhập lại.</c:when>
                <c:when test="${param.error == 'weakPassword'}">Mật khẩu phải có ít nhất 10 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.</c:when>
                <c:when test="${param.error == 'dbError'}">Có lỗi hệ thống, vui lòng thử lại.</c:when>
                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
              </c:choose>
            </span>
          </p>
        </c:if>

        <div class="su-password-input">

            <%-- Mật khẩu mới --%>
          <div class="su-pw-wrap">
            <input type="password" name="newPassword" id="suNewPw"
                   placeholder="Mật khẩu mới"/>
            <i class="fa-regular fa-eye su-eye" data-target="suNewPw"></i>
          </div>

            <%-- Xác nhận mật khẩu --%>
          <div class="su-pw-wrap">
            <input type="password" name="confirmPassword" id="suConfirmPw"
                   placeholder="Xác nhận mật khẩu mới"/>
            <i class="fa-regular fa-eye su-eye" data-target="suConfirmPw"></i>
          </div>

            <%-- Checklist yêu cầu mật khẩu --%>
          <ul class="su-pw-checklist">
            <li id="ck-len">  <i class="fa-solid fa-circle-xmark"></i> Ít nhất 10 ký tự</li>
            <li id="ck-upper"><i class="fa-solid fa-circle-xmark"></i> Có chữ hoa (A–Z)</li>
            <li id="ck-lower"><i class="fa-solid fa-circle-xmark"></i> Có chữ thường (a–z)</li>
            <li id="ck-digit"><i class="fa-solid fa-circle-xmark"></i> Có chữ số (0–9)</li>
            <li id="ck-spec"> <i class="fa-solid fa-circle-xmark"></i> Có ký tự đặc biệt (!@#$%^&*...)</li>
          </ul>

            <%-- Thanh đo độ mạnh --%>
          <div class="su-strength-wrap">
            <div id="suStrengthBar"></div>
            <small id="suStrengthTxt"></small>
          </div>

          <button type="submit" name="action" value="createPassword" class="su-btn">
            Đăng ký
          </button>

        </div>

      </c:if>

    </div>
  </div>
</form>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/sign-up.js"></script>
</body>
</html>