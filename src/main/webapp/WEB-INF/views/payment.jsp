<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment.css"/>

    <link rel="icon" type="image/png"
          href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
</head>

<body>
<div class="checkout-container">

    <!-- LEFT -->
    <div class="checkout-left">
        <div class="image">
            <img src="${pageContext.request.contextPath}/assets/img/ebook-logo2.png" alt="logo">
        </div>
        <div class="label-img">EBookStore</div>

        <div class="label-infor">
            <p>Thông tin nhận hàng</p>
            <a href="${pageContext.request.contextPath}/contact-information">Thay đổi</a>
        </div>

        <!-- USER INFO -->
        <div class="user-information">
            <p>${user.username}</p>
            <p>${user.email}</p>
            <p>${user.phoneNum}</p>
        </div>

        <!-- PAYMENT FORM -->
        <form action="${pageContext.request.contextPath}/checkout"
              method="post"
              class="payment-method">

            <input type="hidden" name="step" value="payment"/>

            <p>Phương thức thanh toán</p>

            <div class="payment-container">
                <div class="payment-option">
                    <input type="radio" id="momo" name="paymentMethod" value="momo" checked>
                    <label for="momo">
                        <img src="${pageContext.request.contextPath}/assets/img/momo-logo.png"> Momo
                    </label>
                </div>

                <div class="payment-option">
                    <input type="radio" id="vnpay" name="paymentMethod" value="vnpay">
                    <label for="vnpay">
                        <img src="${pageContext.request.contextPath}/assets/img/vnpay.png"> VNPay
                    </label>
                </div>

                <div class="payment-option">
                    <input type="radio" id="zalopay" name="paymentMethod" value="zalopay">
                    <label for="zalopay">
                        <img src="${pageContext.request.contextPath}/assets/img/zalo-pay.png"> ZaloPay
                    </label>
                </div>

                <div class="payment-option">
                    <input type="radio" id="qrcode" name="paymentMethod" value="qrcode">
                    <label for="qrcode">
                        <i class="fa-solid fa-qrcode"></i> Quét mã QR
                    </label>
                </div>

                <div class="qr-container hidden">
                    <p>Quét mã để thanh toán</p>
                    <img src="https://cdn.britannica.com/17/155017-050-9AC96FC8/Example-QR-code.jpg">
                </div>
            </div>

            <div class="option">
                <a href="${pageContext.request.contextPath}/cart">
                    <i class="fa-solid fa-arrow-left"></i> Giỏ hàng
                </a>
                <button type="submit" class="checkout-btn">
                    Thanh toán
                </button>
            </div>
        </form>
    </div>

    <!-- RIGHT -->
    <div class="checkout-right">
        <div class="label-right">
            Đơn hàng (${fn:length(cartItems)} sản phẩm)
        </div>

        <c:forEach var="item" items="${cartItems}">
            <div class="product-row">
                <div class="product-detail">
                    <img src="${item.ebook.images[0].imgLink}" alt="${item.ebook.images[0].imgLink}">
                    <div class="product-name">${item.ebook.title}</div>
                </div>
                <div class="product-price">
                    <fmt:formatNumber value="${item.priceAtADD}" type="number"/> VND
                </div>
            </div>
        </c:forEach>

        <div class="price">
            <div class="sub-price-title">Tạm tính</div>
            <div class="product-price">
                <fmt:formatNumber value="${totalPrice}" type="number"/> VND
            </div>
        </div>

        <div class="total">
            <div class="total-price-title">Tổng tiền</div>
            <div class="total-price">
                <fmt:formatNumber value="${totalPrice}" type="number"/> VND
            </div>
        </div>
    </div>

</div>
</body>
</html>
