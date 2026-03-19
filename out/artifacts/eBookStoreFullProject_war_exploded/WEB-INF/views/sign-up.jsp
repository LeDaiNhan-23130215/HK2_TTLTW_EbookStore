<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Đăng ký tài khoản</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link rel="stylesheet" href="assets/css/sign-up.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

    <form action="${pageContext.request.contextPath}/sign-up" method="post">
      <div class="container">
        <p class="header">Đăng ký</p>
          <c:if test="${not empty error_msg}">
              <p style="color:red; margin-bottom: 10px">${error_msg}</p>
          </c:if>

          <c:if test="${not empty success_msg}">
              <p style="color:green">${success_msg}</p>
              <script>
                  setTimeout(function() {
                      window.location.href = "${pageContext.request.contextPath}/home";
                  }, 2000);
              </script>
          </c:if>
        <div class="input">
          <div class="top-input">
            <div class="input-div">
              <input
                type="text"
                name="fname"
                id="fname"
                placeholder="Tên người dùng"
                value="${not empty fname ? fname : param.fname}"
                required
              />
                <span class="error-msg"></span>
            </div>
          </div>
          <div class="mid-input">
            <div class="input-div">
              <input
                type="email"
                name="userAndEmail"
                id="userAndEmail"
                placeholder="Email"
                value="${not empty userAndEmail ? userAndEmail : param.userAndEmail}"
                required
              />
                <span class="error-msg"></span>
            </div>
            <div class="input-div">
              <input
                type="tel"
                name="phoneNumber"
                id="phoneNumber"
                placeholder="Số điện thoại"
                value="${not empty phoneNumber ? phoneNumber : param.phoneNumber}"
                pattern="[0-9]{10,11}"
                required
              />
                <span class="error-msg"></span>
            </div>
          </div>
          <div class="bottom-input">
            <div class="input-div">
              <input
                type="password"
                name="password"
                id="password"
                placeholder="Mật khẩu"
                autocomplete="new-password"
                required
              />
                <span class="error-msg"></span>
            </div>
            <div class="input-div">
              <input
                type="password"
                name="confirmPassword"
                id="confirmPassword"
                placeholder="Nhập lại mật khẩu"
                autocomplete="new-password"
                required
              />
                <span class="error-msg"></span>
            </div>
          </div>
          <button type="submit" class="signUp-btn">Đăng ký</button>
        </div>
        <p class="anotherOption">Hoặc đăng nhập bằng</p>
        <div class="logoContainer">
          <ul class="logoList">
            <li><a href="#"></a><i class="fa-brands fa-facebook-f"></i></li>
            <li><a href="#"></a><i class="fa-brands fa-google"></i></li>
          </ul>
        </div>
      </div>
    </form>

  <jsp:include page="footer.jsp"></jsp:include>
    <script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/sign-up.js"></script>
  </body>
</html>
