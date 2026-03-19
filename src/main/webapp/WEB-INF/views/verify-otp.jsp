<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/verify-otp.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
<form action="${pageContext.request.contextPath}/verify-otp" method="post">
    <div class="container">
        <p class="header">Xác thực OTP</p>

        <div class="input">
            <div class="input-div">
                <input type="text" name="otp" placeholder="OTP" required />
                <span class="error-msg"></span>
            </div>

            <button type="submit">Xác nhận</button>
        </div>

        <c:if test="${not empty error_msg}">
            <p class="info" style="color:red">${error_msg}</p>
        </c:if>
    </div>
</form>
<jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
</body>
</html>
