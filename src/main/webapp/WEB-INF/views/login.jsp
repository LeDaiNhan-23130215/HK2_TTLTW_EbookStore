<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<form action="${pageContext.request.contextPath}/login" method="post">
  <div class="container">
    <p class="header">Đăng nhập</p>

    <%-- Thông báo server (lỗi đăng nhập / thành công đổi mật khẩu) --%>
    <div class="error-server">
      <c:if test="${not empty error_msg}">${error_msg}</c:if>
    </div>

    <%-- Toast đổi mật khẩu thành công --%>
    <c:if test="${param.msg == 'reset_success'}">
      <div id="toastResetOk" style="position:fixed;top:20px;right:20px;z-index:9999;
                 background:#27ae60;color:#fff;padding:14px 22px;border-radius:8px;
                 font-size:14px;box-shadow:0 4px 12px rgba(0,0,0,.25);">
        ✅ Đổi mật khẩu thành công! Vui lòng đăng nhập lại.
      </div>
      <script>setTimeout(function(){document.getElementById('toastResetOk').remove();},4000);</script>
    </c:if>

    <div class="input">
      <%-- Ô email / tên người dùng --%>
      <div class="input-div">
        <input type="text" name="userAndEmail" id="userAndEmail"
               placeholder="Email hoặc Tên người dùng"
               value="${param.userAndEmail}"/>
        <span class="error-msg"></span>
      </div>

        <%-- Ô mật khẩu + icon eye --%>
        <div class="input-div login-pw-wrap">
          <input type="password" name="password" id="loginPassword"
                 placeholder="Mật khẩu"/>
          <i class="fa-regular fa-eye login-eye" data-target="loginPassword"></i>
          <span class="error-msg"></span>
        </div>

      <div class="g-recaptcha" data-sitekey="6LfYFe0sAAAAAEgQiE3PmUlBnov4MPz_aQIEcZZi"></div>
      <button type="submit" class="signIn-btn">Đăng nhập</button>
    </div>

    <a href="${pageContext.request.contextPath}/forgot-password">
      <p class="forgetPassword">Quên mật khẩu</p>
    </a>
    <p class="anotherOption">Hoặc đăng nhập bằng</p>
    <div class="logoContainer">
      <ul class="logoList">
        <li><a href="#"></a><i class="fa-brands fa-facebook-f"></i></li>
        <li style="background:none; box-shadow:none; padding:0; border-radius:0;">
          <a href="${pageContext.request.contextPath}/oauth/google/init"
             class="google-btn" title="Đăng nhập bằng Google">
            <svg class="google-icon" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"/>
              <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"/>
              <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z" fill="#FBBC05"/>
              <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"/>
            </svg>
          </a>
        </li>
      </ul>
    </div>
    <a href="${pageContext.request.contextPath}/sign-up">
      <p class="signUp">Đăng ký</p>
    </a>
  </div>
</form>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>
</body>
</html>