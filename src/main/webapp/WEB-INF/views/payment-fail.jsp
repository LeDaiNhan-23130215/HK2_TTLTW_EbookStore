<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thất bại</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/notice.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <style>
    </style>
</head>
<body>
    <div class="fail-container">
        <i class="fa-solid fa-circle-xmark payment-icon fail-icon"></i>
        <h1>Thanh toán thất bại</h1>
        <p>Rất tiếc, giao dịch của bạn chưa được thực hiện thành công.</p>
        <p>Vui lòng kiểm tra lại phương thức thanh toán hoặc thử lại sau.</p>

        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary">
            <i class="fa-solid fa-arrow-rotate-right"></i> Thử lại thanh toán
        </a>
    </div>
</body>
</html>