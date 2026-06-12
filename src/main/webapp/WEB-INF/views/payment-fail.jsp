<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // ── Access Control ──────────────────────────────────────────────────────
    boolean isForwarded = (request.getAttribute("vnpResponseCode") != null)
            || (request.getAttribute("errorMessage")    != null);
    if (!isForwarded) {
        response.sendRedirect(request.getContextPath() + "/your-order");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thất bại – EBookStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
        .btn-group { display: flex; gap: 12px; justify-content: center; flex-wrap: wrap; margin-top: 20px; }
        .error-detail { margin-top: 12px; font-size: .85rem; color: #6b7280; }
    </style>
</head>
<body>
<div class="fail-container">
    <i class="fa-solid fa-circle-xmark payment-icon fail-icon"></i>
    <h1>Thanh toán thất bại</h1>
    <p>Rất tiếc, giao dịch của bạn chưa được thực hiện thành công.</p>

    <c:if test="${not empty errorMessage}">
        <p class="error-detail"><i class="fa-solid fa-circle-info"></i> ${errorMessage}</p>
    </c:if>
    <c:if test="${not empty vnpResponseCode && vnpResponseCode != '00'}">
        <p class="error-detail"><i class="fa-solid fa-circle-info"></i> ${vnpDesc}</p>
        <p class="error-detail">Mã lỗi VNPAY: <strong>${vnpResponseCode}</strong></p>
    </c:if>

    <div class="btn-group">
        <a href="${pageContext.request.contextPath}/cart" class="btn btn-primary">
            <i class="fa-solid fa-arrow-rotate-right"></i> Thử lại
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
            <i class="fa-solid fa-house"></i> Quay lại trang chủ
        </a>
    </div>
</div>
</body>
</html>
