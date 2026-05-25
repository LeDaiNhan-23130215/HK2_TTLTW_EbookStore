<%--
  Created by IntelliJ IDEA.
  User: Nhan
  Date: 5/25/2026
  Time: 8:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Application Error</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/error-page.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/components.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>
<div class="container">
    <h1>Oops!</h1>
    <h2>Something went wrong</h2>
    <p>
        Hệ thống đã gặp lỗi trong quá trình xử lý.
    </p>
    <div class="message">
        <c:choose>
            <c:when test="${not empty errorMessage}">
                ${errorMessage}
            </c:when>
            <c:otherwise>
                Unexpected system error.
            </c:otherwise>
        </c:choose>
    </div>
    <a href="${pageContext.request.contextPath}/home">
        Quay về trang chủ
    </a>

</div>
<jsp:include page="/WEB-INF/views/footer.jsp"/>
</body>
</html>