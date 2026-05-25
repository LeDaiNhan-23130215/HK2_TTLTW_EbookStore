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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/error-page.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css">

</head>

<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="box">
    <h1>404 - Page Not Found</h1>
    <p>Trang của bạn tìm kiếm không tồn tại.</p>
    <p>Vui lòng thử lại sau.</p>
    <p>
        URL:
        ${pageContext.errorData.requestURI}
    </p>
    <p>
        STATUS CODE:
        ${pageContext.errorData.statusCode}
    </p>
    <a href="${pageContext.request.contextPath}/home">
        Quay về trang chủ
    </a>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>

</body>
</html>
