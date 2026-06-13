<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="container">
    <div class="inner-container">
        <div class="cart-container">

            <%-- ── HEADER ── --%>
            <div class="cart-header">
                <div class="header">Thông tin sản phẩm</div>
                <div class="header">Đơn giá</div>
                <div class="header">Thành tiền</div>
            </div>

            <%-- ── BODY ── --%>
            <div class="cart-inner">
                <c:choose>

                    <%-- ===== GUEST CART ===== --%>
                    <c:when test="${isGuest}">
                        <c:choose>
                            <c:when test="${empty guestItems}">
                                <div class="cart-row cart-empty">
                                    <i class="fa-solid fa-cart-shopping"></i>
                                    <p>Giỏ hàng của bạn đang trống.</p>
                                    <a href="${pageContext.request.contextPath}/home"
                                       class="cart-continue-btn">Tiếp tục mua sắm</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${guestItems}">
                                    <div class="cart-row">

                                        <div class="cart-product">
                                            <a href="${pageContext.request.contextPath}/bookdetail?id=${item.ebook.id}"
                                               class="cart-img">
                                                <img src="${ebookThumbnails[item.ebook.id]}"
                                                     alt="${item.ebook.title}"
                                                class="thumbnail"/>
                                            </a>
                                            <div class="cart-infor">
                                                <div class="cart-name">
                                                    <div class="cart-name-row">
                                                        <a href="${pageContext.request.contextPath}/bookdetail?id=${item.ebook.id}"
                                                           class="product_name">${item.ebook.title}</a>
                                                        <c:if test="${item.discounted}">
                                                            <span class="cart-badge-discount">${item.discountLabel}</span>
                                                        </c:if>
                                                    </div>
                                                    <form action="${pageContext.request.contextPath}/cart"
                                                          method="post">
                                                        <input type="hidden" name="action" value="buyOne"/>
                                                        <input type="hidden" name="bookId" value="${item.ebook.id}"/>
                                                        <button type="submit" class="buyone-btn">
                                                            <i class="fa-solid fa-bolt"></i> Mua ngay
                                                        </button>
                                                    </form>
                                                    <form action="${pageContext.request.contextPath}/cart"
                                                          method="post" class="cart-remove-form"
                                                          data-title="Xoá khỏi giỏ hàng?"
                                                          data-desc="Bạn có chắc muốn xoá «${item.ebook.title}» khỏi giỏ hàng?">
                                                        <input type="hidden" name="action" value="remove"/>
                                                        <input type="hidden" name="bookId" value="${item.ebook.id}"/>
                                                        <button type="button" class="remove-item-cart cart-delete-trigger">
                                                            <i class="fa-solid fa-trash-can"></i> Xoá
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                            <%-- Cột Đơn giá --%>
                                        <div class="cart-price">
                                            <c:choose>
                                                <c:when test="${item.discounted}">
                                                    <span class="cart-price-final">
                                                        <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                                    </span>
                                                    <span class="cart-price-original">
                                                        <del><fmt:formatNumber value="${item.originalPrice}" type="number"/>đ</del>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                            <%-- Cột Thành tiền --%>
                                        <div class="cart-price cart-subtotal">
                                            <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                        </div>

                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:when>

                    <%-- ===== LOGGED-IN CART ===== --%>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${empty cartItems}">
                                <div class="cart-row cart-empty">
                                    <i class="fa-solid fa-cart-shopping"></i>
                                    <p>Giỏ hàng của bạn đang trống.</p>
                                    <a href="${pageContext.request.contextPath}/home"
                                       class="cart-continue-btn">Tiếp tục mua sắm</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="item" items="${cartItems}">
                                    <div class="cart-row">

                                        <div class="cart-product">
                                            <a href="${pageContext.request.contextPath}/bookdetail?id=${item.ebook.id}"
                                               class="cart-img">
                                                <img src="${ebookThumbnails[item.ebook.id]}"
                                                     alt="${item.ebook.title}"
                                                     class="thumbnail"/>
                                            </a>
                                            <div class="cart-infor">
                                                <div class="cart-name">
                                                    <div class="cart-name-row">
                                                        <a href="${pageContext.request.contextPath}/bookdetail?id=${item.ebook.id}"
                                                           class="product_name">${item.ebook.title}</a>
                                                        <c:if test="${item.discounted}">
                                                            <span class="cart-badge-discount">${item.discountLabel}</span>
                                                        </c:if>
                                                    </div>
                                                    <form action="${pageContext.request.contextPath}/cart"
                                                          method="post">
                                                        <input type="hidden" name="action" value="buyOne"/>
                                                        <input type="hidden" name="bookId" value="${item.ebook.id}"/>
                                                        <button type="submit" class="buyone-btn">
                                                            <i class="fa-solid fa-bolt"></i> Mua ngay
                                                        </button>
                                                    </form>

                                                    <form action="${pageContext.request.contextPath}/cart"
                                                          method="post" class="cart-remove-form"
                                                          data-title="Xoá khỏi giỏ hàng?"
                                                          data-desc="Bạn có chắc muốn xoá «${item.ebook.title}» khỏi giỏ hàng?">
                                                        <input type="hidden" name="action" value="remove"/>
                                                        <input type="hidden" name="bookId" value="${item.ebook.id}"/>
                                                        <button type="button" class="remove-item-cart cart-delete-trigger">
                                                            <i class="fa-solid fa-trash-can"></i> Xoá
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                            <%-- Cột Đơn giá --%>
                                        <div class="cart-price">
                                            <c:choose>
                                                <c:when test="${item.discounted}">
                                                    <span class="cart-price-final">
                                                        <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                                    </span>
                                                    <span class="cart-price-original">
                                                        <del><fmt:formatNumber value="${item.originalPrice}" type="number"/>đ</del>
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                            <%-- Cột Thành tiền --%>
                                        <div class="cart-price cart-subtotal">
                                            <fmt:formatNumber value="${item.priceAtADD}" type="number"/>đ
                                        </div>

                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </div>

            <%-- ── FOOTER ── --%>
            <div class="cart-footer">
                <div class="footer-row">
                    <div class="footer-container">
                        <div class="total-price">
                            <strong>Tổng tiền: </strong>
                            <span class="total-amount">
                <fmt:formatNumber value="${totalPrice}" type="number"/>đ
              </span>
                        </div>
                        <div class="cart-btn">
                            <c:choose>
                                <c:when test="${isGuest and not empty guestCart}">
                                    <form action="${pageContext.request.contextPath}/cart" method="post">
                                        <input type="hidden" name="action" value="checkout"/>
                                        <button type="submit" class="checkout-btn">
                                            <i class="fa-solid fa-lock"></i> Đăng nhập để thanh toán
                                        </button>
                                    </form>
                                </c:when>
                                <c:when test="${not isGuest and not empty cartItems}">
                                    <form action="${pageContext.request.contextPath}/cart" method="post">
                                        <input type="hidden" name="action" value="checkout"/>
                                        <button type="submit" class="checkout-btn">
                                            <i class="fa-solid fa-credit-card"></i> Thanh toán
                                        </button>
                                    </form>
                                </c:when>
                            </c:choose>
                            <div class="term">
                                Bằng việc mua hàng, bạn đồng ý với<br/>
                                <a href="#">Điều khoản &amp; Điều kiện EBOOK</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<%-- ── XÁC NHẬN XOÁ ── --%>
<div id="cartDeleteModal" class="ui-modal-overlay" style="display:none;" aria-modal="true" role="dialog">
    <div class="ui-modal-box">
        <div class="ui-modal-icon">
            <i class="fa-solid fa-trash-can"></i>
        </div>
        <h3 class="ui-modal-title" id="cartDeleteModalTitle">Xoá khỏi giỏ hàng?</h3>
        <p class="ui-modal-desc" id="cartDeleteModalDesc"></p>
        <div class="ui-modal-actions">
            <button type="button" class="ui-modal-cancel" id="cartDeleteModalCancel">Quay lại</button>
            <button type="button" class="ui-modal-confirm" id="cartDeleteModalConfirm">
                <i class="fa-solid fa-trash-can"></i> Xoá
            </button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/handleErrorImage.js"></script>
</body>
</html>
