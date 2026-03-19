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

        <!-- ===== HIỂN THỊ LỖI ===== -->
        <c:if test="${not empty param.error}">
            <p class="error-msg">
                <c:choose>
                    <c:when test="${param.error == 'emailNotFound'}">
                        Email không tồn tại trong hệ thống.
                    </c:when>
                    <c:when test="${param.error == 'invalidCode'}">
                        Mã xác nhận không hợp lệ hoặc đã hết hạn.
                    </c:when>
                    <c:when test="${param.error == 'passwordMismatch'}">
                        Mật khẩu xác nhận không khớp.
                    </c:when>
                    <c:otherwise>
                        Có lỗi xảy ra, vui lòng thử lại.
                    </c:otherwise>
                </c:choose>
            </p>
        </c:if>

        <div class="input">

            <!-- ===== STEP 1: NHẬP EMAIL ===== -->
            <c:if test="${empty param.step}">
                <div class="email-input">
                    <input type="email"
                           name="email"
                           placeholder="Vui lòng nhập email của bạn"
                           required/>
                    <button type="submit" name="action" value="sendCode" class="code-btn">
                        Gửi mã
                    </button>
                </div>
            </c:if>

            <!-- ===== STEP 2: NHẬP MÃ ===== -->
            <c:if test="${param.step == 'verify'}">
                <div class="code-input">
                    <input type="text"
                           name="confirmCode"
                           placeholder="Nhập mã xác nhận"
                           required/>
                    <button type="submit" name="action" value="verifyCode" class="code-btn">
                        Xác nhận
                    </button>
                </div>
            </c:if>

            <!-- ===== STEP 3: ĐỔI MẬT KHẨU ===== -->
            <c:if test="${param.step == 'reset'}">
                <div class="password-input">
                    <input type="password"
                           name="newPassword"
                           placeholder="Mật khẩu mới"
                           required/>
                    <input type="password"
                           name="confirmPassword"
                           placeholder="Xác nhận mật khẩu mới"
                           required/>
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
</body>
</html>
