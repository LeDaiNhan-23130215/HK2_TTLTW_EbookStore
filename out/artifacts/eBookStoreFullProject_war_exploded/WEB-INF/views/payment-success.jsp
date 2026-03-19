<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
</head>
<body>
    <div class="success-container">
        <i class="fa-solid fa-circle-check payment-icon success-icon"></i>
        <h1>Thanh toán thành công!</h1>
        <p>Cảm ơn bạn đã mua hàng. Đơn hàng của bạn đã được ghi nhận.</p>

        <div class="order-info">
            <p><b>Mã đơn hàng:</b> ${checkout.id}</p>
            <p><b>Phương thức thanh toán:</b> ${paymentMethod.name}</p>
            <p><b>Tổng tiền:</b> ${checkout.totalAmount} VND</p>
            <p><b>Trạng thái:</b> ${checkout.status}</p>
        </div>

        <a href="${pageContext.request.contextPath}/book-shelf" class="btn btn-primary">
            <i class="fa-solid fa-book"></i> Xem tủ sách
        </a>
        <a href="${pageContext.request.contextPath}/your-order" class="btn btn-secondary">
            <i class="fa-solid fa-list"></i> Xem đơn hàng
        </a>
    </div>
</body>
</html>