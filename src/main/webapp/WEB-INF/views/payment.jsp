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

            <c:if test="${singleMode}">
                <input type="hidden" name="mode" value="single"/>
                <input type="hidden" name="bookId" value="${singleBookId}"/>
            </c:if>

            <p>Phương thức thanh toán</p>

            <div class="payment-container">
                <div class="payment-option">
                    <input type="radio" id="vnpay" name="paymentMethod" value="vnpay" checked>
                    <label for="vnpay">
                        <img src="${pageContext.request.contextPath}/assets/img/vnpay.png"> VNPay
                    </label>
                </div>
            </div>

            <div class="option">
                <a href="${pageContext.request.contextPath}/cart">
                    <i class="fa-solid fa-arrow-left"></i> Giỏ hàng
                </a>
                <button type="submit" class="checkout-btn">
                    Xác nhận
                </button>
            </div>
        </form>
    </div>

    <div class="checkout-right">
        <div class="label-right">
            Đơn hàng (${fn:length(cartItems)} sản phẩm)
        </div>

        <div class="product-list-scroll">
            <c:forEach var="item" items="${cartItems}">
                <div class="product-row">
                    <div class="product-detail">
                        <img src="${item.ebook.images[0].imgLink}" alt="${item.ebook.title}">
                        <div class="product-name">${item.ebook.title}</div>
                    </div>
                    <div class="product-price">
                        <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                    </div>
                </div>
            </c:forEach>
        </div>

        <div class="voucher-section">
            <form action="${pageContext.request.contextPath}/apply-voucher"
                  method="post"
                  class="voucher-form">
                <input
                        type="text"
                        name="voucherCode"
                        placeholder="Nhập mã voucher">
                <button type="submit">
                    Áp dụng
                </button>
            </form>
            <c:if test="${not empty sessionScope.voucherError}">
                <div class="voucher-error">
                        ${sessionScope.voucherError}
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.voucher}">
                <div class="voucher-success">
                    <i class="fa-solid fa-ticket"></i>
                    Voucher:
                    <strong>
                            ${sessionScope.voucher.code}
                    </strong>
                </div>
            </c:if>
        </div>

        <div class="price">
            <div class="sub-price-title">
                Tạm tính
            </div>
            <div class="product-price">
                <fmt:formatNumber
                        value="${totalPrice}"
                        type="number"/>đ
            </div>
        </div>

        <c:if test="${not empty sessionScope.discount}">
            <div class="price">
                <div class="sub-price-title">
                    Giảm giá
                </div>
                <div class="product-price discount-price">
                    -
                    <fmt:formatNumber
                            value="${sessionScope.discount}"
                            type="number"/>đ
                </div>
            </div>
        </c:if>

        <div class="total">
            <div class="total-price-title">
                Tổng tiền
            </div>
            <div class="total-price">
                <c:choose>
                    <c:when test="${not empty sessionScope.finalPrice}">
                        <fmt:formatNumber
                                value="${sessionScope.finalPrice}"
                                type="number"/>đ
                    </c:when>
                    <c:otherwise>
                        <fmt:formatNumber
                                value="${totalPrice}"
                                type="number"/>đ
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
</body>
</html>