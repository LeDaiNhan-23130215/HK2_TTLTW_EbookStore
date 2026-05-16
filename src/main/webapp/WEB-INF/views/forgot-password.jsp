<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
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
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png"
          href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
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
                    <p style="color:#c0392b;font-size:13px;background:#fdf0f0;
                              border-left:4px solid #e74c3c;padding:8px 12px;
                              border-radius:4px;width:330px;box-sizing:border-box;">
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
                    <input type="text" name="email"
                           placeholder="Vui lòng nhập email của bạn"/>
                    <button type="submit" name="action" value="sendCode" class="code-btn">
                        Gửi mã
                    </button>
                </div>
            </c:if>

            <%-- ===== STEP 2: NHẬP OTP ===== --%>
            <c:if test="${param.step == 'verify'}">
                <c:if test="${not empty param.error}">
                    <p style="color:#c0392b;font-size:13px;background:#fdf0f0;
                              border-left:4px solid #e74c3c;padding:8px 12px;
                              border-radius:4px;width:330px;box-sizing:border-box;">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        Mã OTP không hợp lệ hoặc đã hết hạn. Vui lòng kiểm tra lại email.
                    </p>
                </c:if>
                <%-- Chữ nhỏ hơn: font-size 13px, font-weight normal --%>
                <p style="color:#555;font-size:13px;font-weight:normal;margin-bottom:8px;">
                    Mã OTP đã gửi đến: <strong style="font-size:13px;">${sessionScope.resetEmail}</strong>
                </p>
                <div class="code-input">
                    <input type="text" name="confirmCode"
                           placeholder="Nhập mã OTP 6 chữ số"
                           maxlength="6" autofocus/>
                    <button type="submit" name="action" value="verifyCode" class="code-btn">
                        Xác nhận
                    </button>
                </div>
                <div style="margin-top:14px;margin-bottom:8px;">
                    <a href="${pageContext.request.contextPath}/forgot-password"
                       style="color:#666;font-size:13px;text-decoration:none;">
                        ← Quay lại nhập email khác
                    </a>
                </div>
            </c:if>

            <%-- ===== STEP 3: ĐỔI MẬT KHẨU ===== --%>
            <c:if test="${param.step == 'reset'}">
                <div class="password-input">
                    <c:if test="${not empty param.error}">
                        <p style="color:#c0392b;font-size:13px;background:#fdf0f0;
                                  border-left:4px solid #e74c3c;padding:8px 12px;
                                  border-radius:4px;width:320px;box-sizing:border-box;">
                            <i class="fa-solid fa-circle-exclamation"></i>
                            <c:choose>
                                <c:when test="${param.error == 'passwordMismatch'}">
                                    Mật khẩu xác nhận không khớp. Vui lòng nhập lại.
                                </c:when>
                                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                            </c:choose>
                        </p>
                    </c:if>
                    <input type="password" name="newPassword"
                           placeholder="Mật khẩu mới" required/>
                    <input type="password" name="confirmPassword"
                           placeholder="Xác nhận mật khẩu mới" required/>
                    <button type="submit" name="action" value="resetPassword"
                            class="confirm-btn">
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