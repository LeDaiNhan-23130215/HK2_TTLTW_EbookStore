<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/login.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

    <form action="${pageContext.request.contextPath}/login" method="post">
      <div class="container">
        <p class="header">Đăng nhập</p>
          <c:if test="${not empty error_msg}">
              <p style="color:red; margin-bottom: 10px">${error_msg}</p>
          </c:if>
        <div class="input">
          <div class="input-div">
            <input
              type="text"
              name="userAndEmail"
              id="userAndEmail"
              placeholder="Email hoặc Tên người dùng"
              value="${param.userAndEmail}"
            />
              <span class="error-msg"></span>
          </div>
          <div class="input-div">
            <input
              type="password"
              name="password"
              id="password"
              placeholder="Mật khẩu"
            />
              <span class="error-msg"></span>
          </div>
          <button type="submit" class="signIn-btn">Đăng nhập</button>
        </div>
        <a href="${pageContext.request.contextPath}/forgot-password"><p class="forgetPassword">Quên mật khẩu</p></a>
        <p class="anotherOption">Hoặc đăng nhập bằng</p>
        <div class="logoContainer">
          <ul class="logoList">
            <li><a href="#"></a><i class="fa-brands fa-facebook-f"></i></li>
            <li><a href="#"></a><i class="fa-brands fa-google"></i></li>
          </ul>
        </div>
          <a href="${pageContext.request.contextPath}/sign-up"><p class="signUp">Đăng ký</p></a>
      </div>
    </form>

  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
    
    <script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>
  </body>
</html>
