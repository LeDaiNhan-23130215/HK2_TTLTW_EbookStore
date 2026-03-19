<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Login Page</title>
    <link rel="stylesheet" href="assets/css/admin-login.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <body>
        <form action="${pageContext.request.contextPath}/admin-login" method="post">
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
                    placeholder="Email"
                    value="${param.userAndEmail}"
                    />
                </div>
                <div class="input-div">
                    <input
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Mật khẩu"
                    autocomplete=""
                    />
                </div>
                <button type="submit" class="signIn-btn">Đăng nhập</button>
                </div>
            </div>
        </form>
  </body>
</html>
