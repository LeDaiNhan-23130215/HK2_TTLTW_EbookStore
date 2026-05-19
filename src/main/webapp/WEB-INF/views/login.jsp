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
        <li><a href="#"></a><i class="fa-brands fa-google"></i></li>
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