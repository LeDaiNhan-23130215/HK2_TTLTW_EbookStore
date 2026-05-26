<%--
  Created by IntelliJ IDEA.
  User: Nhan
  Date: 5/25/2026
  Time: 8:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Server Error</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/error-page.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/components.css">
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>

<body>

<div class="box">
    <h1>500 - Internal Server Error</h1>
    <p>Hệ thống đang gặp sự cố.</p>
    <p>Vui lòng thử lại sau.</p>
    <p>
        URI:
        ${pageContext.errorData.requestURI}
    </p>

    <p>
        STATUS CODE:
        ${pageContext.errorData.statusCode}
    </p>
    <a href="${pageContext.request.contextPath}/home" class="back-to-home-error">
        Quay về trang chủ
    </a>
</div>


</body>
</html>
