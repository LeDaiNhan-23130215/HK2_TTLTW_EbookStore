<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    Object lastId = session.getAttribute("lastCheckoutId");
    if (lastId == null) {
        response.sendRedirect(request.getContextPath() + "/your-order");
        return;
    }
    session.removeAttribute("lastCheckoutId");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công – EBookStore</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
        .btn-group { display: flex; gap: 12px; justify-content: center; flex-wrap: wrap; margin-top: 20px; }
        .countdown-bar {
            width: 100%; height: 4px; background: #e5e7eb; border-radius: 2px;
            margin: 16px 0 4px; overflow: hidden;
        }
        .countdown-bar-fill {
            height: 100%; background: #0396c7; border-radius: 2px;
            width: 100%; transition: width 1s linear;
        }
        .countdown-text { font-size: .85rem; color: #6b7280; text-align: center; }
    </style>
</head>
<body data-home-url="${pageContext.request.contextPath}/">
<div class="success-container">
    <i class="fa-solid fa-circle-check payment-icon success-icon"></i>
    <h1>Thanh toán thành công!</h1>
    <p>Cảm ơn bạn đã mua hàng. Đơn hàng của bạn đã được ghi nhận.</p>

    <div class="order-info">
        <p><b>Mã đơn hàng:</b> #${checkout.id}</p>
        <c:if test="${not empty paymentMethod}">
            <p><b>Phương thức:</b> ${paymentMethod.name}</p>
        </c:if>
        <p><b>Tổng tiền:</b>
            <fmt:formatNumber value="${checkout.totalAmount}" type="number"/>đ
        </p>
        <p><b>Trạng thái:</b> Thành công</p>
        <c:if test="${not empty vnpTransNo}">
            <p><b>Mã GD VNPAY:</b> ${vnpTransNo}</p>
        </c:if>
    </div>

    <div class="countdown-bar"><div class="countdown-bar-fill" id="bar"></div></div>
    <p class="countdown-text">Tự động về trang chủ sau <span id="sec">30</span> giây</p>

    <div class="btn-group">
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary" id="homeBtn">
            <i class="fa-solid fa-house"></i> Trang chủ
        </a>
        <a href="${pageContext.request.contextPath}/book-shelf" class="btn btn-secondary">
            <i class="fa-solid fa-book"></i> Tủ sách
        </a>
        <a href="${pageContext.request.contextPath}/your-order" class="btn btn-secondary">
            <i class="fa-solid fa-list"></i> Đơn hàng
        </a>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/payment.js"></script>
</body>
</html>