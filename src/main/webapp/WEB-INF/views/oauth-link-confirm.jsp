<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Liên kết tài khoản</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/oauth-link-confirm.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="link-confirm-wrap">
    <div class="link-confirm-card">
        <p class="title">Liên kết tài khoản</p>
        <p class="desc">
            Email <strong>${sessionScope.pendingEmail}</strong> đã được đăng ký trước đó.<br/>
            Bạn có muốn liên kết tài khoản này với Google không?
        </p>
        <form action="${pageContext.request.contextPath}/oauth/link-confirm" method="post">
            <div class="link-confirm-actions">
                <button type="submit" name="action" value="confirm" class="btn-confirm">
                    Liên kết tài khoản
                </button>
                <button type="submit" name="action" value="cancel" class="btn-cancel">
                    Hủy
                </button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
</body>
</html>