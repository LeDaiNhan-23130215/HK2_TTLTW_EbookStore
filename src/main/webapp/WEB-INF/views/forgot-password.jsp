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

<form action="${pageContext.request.contextPath}/forgot-password" method="post">
    <div class="container">
        <p class="header">Quên mật khẩu</p>

        <div class="input">

            <%-- ===== STEP 1: NHẬP EMAIL ===== --%>
            <c:if test="${empty param.step}">
                <c:if test="${not empty param.error}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <c:choose>
                            <c:when test="${param.error == 'emailNotFound'}">
                                Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại.
                            </c:when>
                            <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                        </c:choose>
                    </p>
                </c:if>
                <div class="email-input">
                    <input type="text" name="email" placeholder="Vui lòng nhập email của bạn"/>
                    <button type="submit" name="action" value="sendCode" class="code-btn">Gửi mã</button>
                </div>
            </c:if>

            <%-- ===== STEP 2: NHẬP OTP ===== --%>
            <c:if test="${param.step == 'verify'}">
                <c:if test="${not empty param.error}">
                    <p class="fp-error-box">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        Mã OTP không hợp lệ hoặc đã hết hạn. Vui lòng kiểm tra lại email.
                    </p>
                </c:if>
                <p class="fp-sent-to">
                    Mã OTP đã gửi đến: <strong>${sessionScope.resetEmail}</strong>
                </p>
                <div class="code-input">
                    <input type="text" name="confirmCode"
                           placeholder="Nhập mã OTP 6 chữ số" maxlength="6" autofocus/>
                    <button type="submit" name="action" value="verifyCode" class="code-btn">Xác nhận</button>
                </div>
                <div class="fp-back-link">
                    <a href="${pageContext.request.contextPath}/forgot-password">← Quay lại nhập email khác</a>
                </div>
            </c:if>

                <%-- ===== STEP 3: ĐỔI MẬT KHẨU ===== --%>
                <c:if test="${param.step == 'reset'}">
                    <c:if test="${not empty param.error}">
                        <p class="fp-error-box">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <c:choose>
                                <c:when test="${param.error == 'passwordEmpty'}">
                                    Vui lòng nhập mật khẩu mới.
                                </c:when>
                                <c:when test="${param.error == 'passwordMismatch'}">
                                    Mật khẩu xác nhận không khớp. Vui lòng nhập lại.
                                </c:when>
                                <c:when test="${param.error == 'weakPassword'}">
                                    Mật khẩu không đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.
                                </c:when>
                                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                            </c:choose>
                        </p>
                    </c:if>

                    <div class="password-input">
                            <%-- Mật khẩu mới + toggle --%>
                        <div class="fp-pw-wrap">
                            <input type="password" name="newPassword" id="fpNewPw"
                                   placeholder="Mật khẩu mới"/>
                            <i class="fa-regular fa-eye fp-eye" data-target="fpNewPw"></i>
                        </div>

                            <%-- Xác nhận mật khẩu + toggle --%>
                        <div class="fp-pw-wrap">
                            <input type="password" name="confirmPassword" id="fpConfirmPw"
                                   placeholder="Xác nhận mật khẩu mới"/>
                            <i class="fa-regular fa-eye fp-eye" data-target="fpConfirmPw"></i>
                        </div>

                            <%-- Checklist yêu cầu --%>
                        <ul class="pw-checklist">
                            <li id="ck-len">  <i class="fa-solid fa-circle-xmark"></i> Ít nhất 10 ký tự</li>
                            <li id="ck-upper"><i class="fa-solid fa-circle-xmark"></i> Có chữ hoa (A–Z)</li>
                            <li id="ck-lower"><i class="fa-solid fa-circle-xmark"></i> Có chữ thường (a–z)</li>
                            <li id="ck-digit"><i class="fa-solid fa-circle-xmark"></i> Có chữ số (0–9)</li>
                            <li id="ck-spec"> <i class="fa-solid fa-circle-xmark"></i> Có ký tự đặc biệt (!@#$%^&*...)</li>
                        </ul>

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
<script src="${pageContext.request.contextPath}/assets/js/forgot-password.js" defer></script>
</body>
</html>