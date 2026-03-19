<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="assets/css/base.css"/>
    <link rel="stylesheet" href="assets/css/components.css"/>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png"/>
    <link rel="stylesheet" href="assets/css/cart.css"/>
</head>
<body>
<!-- #Header -->
<jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

<div class="container">
    <div class="inner-container">
        <div class="cart-container">
                <div class="cart-header">
                    <div class="header">Thông tin sản phẩm</div>
                    <div class="header">Đơn giá</div>
                    <div class="header">Thành tiền</div>
                </div>
                <div class="cart-inner">

                    <c:if test="${empty cartItems}">
                        <div class="cart-row">
                            <p>Giỏ hàng của bạn đang trống.</p>
                        </div>
                    </c:if>

                    <c:forEach var="item" items="${cartItems}">
                        <div class="cart-row">

                            <div class="cart-product">
                                <a href="#" class="cart-img">
                                    <img src="${item.ebook.images[0].imgLink}" alt="${item.ebook.images[0].imgLink}">
                                </a>

                                <div class="cart-infor">
                                    <div class="cart-name">
                                        <a href="#" class="product_name">
                                            ${item.ebook.title}
                                        </a>

                                        <form action="cart" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="bookId" value="${item.ebook.id}">
                                            <button type="submit" class="remove-item-cart">
                                                Xóa
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="cart-price">
                                <fmt:formatNumber value="${item.priceAtADD}" type="number"/> VND
                            </div>

                            <div class="cart-price">
                                <fmt:formatNumber value="${item.priceAtADD}" type="number"/> VND
                            </div>

                        </div>
                    </c:forEach>

                </div>
                <div class="cart-footer">
                    <div class="footer-row">
                        <div class="footer-container">
                            <div class="total-price">
                                <strong>Tổng tiền: </strong>
                                <fmt:formatNumber value="${totalPrice}" type="number"/> VND
                            </div>
                            <div class="cart-btn">
                                <c:if test="${not empty cartItems}">
                                    <a href="contact-information">
                                        <button type="button">Thanh toán</button>
                                    </a>
                                </c:if>

                                <div class="term">
                                    Bằng việc mua hàng, bạn đồng ý với
                                    <br>
                                    <a href="#">Điều khoản & Điều kiện EBOOK</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>
</div>

<!-- #Footer -->
<jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

<script src="assets/js/component.js"></script>
</body>
</html>