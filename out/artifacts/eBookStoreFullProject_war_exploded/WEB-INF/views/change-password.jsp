<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin người dùng</title>
    <link rel="stylesheet" href="assets/css/base.css">
    <link rel="stylesheet" href="assets/css/components.css">
    <link rel="stylesheet" href="assets/css/user-infor.css">
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png">

</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
<div class="container">
    <div class="box-left">
        <div class="subTitle">
            <h5>TRANG TÀI KHOẢN</h5>
            <p><b>Xin chào, </b>
                <b style="color: hsl(0, 100%, 60%); font-weight: 550">
                ${sessionScope.userName}
                </b> !
            </p>
        </div>
        <ul class="option-list">
            <li><a href="userInformation">Thông tin tài khoản</a></li>
            <li><a href="your-order">Đơn hàng của bạn</a></li>
            <li><a href="book-shelf">Tủ sách của bạn</a></li>
            <li><a href="wishlist">Danh mục yêu thích</a></li>
            <li class="selected"><a href="#">Đổi mật khẩu</a></li>
        </ul>
    </div>
    <div class="box-right">
        <div class="subTitle">
            <h5>ĐỔI MẬT KHẨU</h5>
            <form class="changePass"
                  action="${pageContext.request.contextPath}/change-password"
                  method="post">
                <c:if test="${not empty error_msg}">
                    <p style="color:red; margin-bottom: 10px">${error_msg}</p>
                </c:if>
                <div class="input-div">
                    <input type="password"
                           name="oldPassword"
                           id="password"
                           placeholder="Mật khẩu cũ"
                           autocomplete="current-password">
                </div>

                <div class="input-div">
                    <input type="password"
                           name="newPassword"
                           id="newPassword"
                           placeholder="Mật khẩu mới"
                           autocomplete="new-password">
                </div>

                <div class="input-div">
                    <input type="password"
                           name="confirmPassword"
                           id="confirmPassword"
                           placeholder="Xác nhận lại mật khẩu"
                           autocomplete="new-password">
                </div>

                <button type="submit" class="changePass-btn">
                    Đổi mật khẩu
                </button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
<script src="assets/js/component.js"></script>
</body>
</html>