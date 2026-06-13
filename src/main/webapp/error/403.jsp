<%--
  403 - Access Forbidden page (theo mẫu 404.jsp)
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Access Forbidden</title>
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
    <h1>403 - Truy cập bị từ chối</h1>
    <p>Bạn không có quyền truy cập trang này.</p>
    <p>Vui lòng thử lại sau hoặc đăng nhập bằng tài khoản phù hợp.</p>
    <p>
        URL:
        ${pageContext.errorData.requestURI}
    </p>
    <p>
        STATUS CODE:
        ${pageContext.errorData.statusCode}
    </p>
    <a href="${pageContext.request.contextPath}/home"
       class="back-to-home-error"
        target = "_top">
        Quay về trang chủ
    </a>
</div>


</body>
</html>
