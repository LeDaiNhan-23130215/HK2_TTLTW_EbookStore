<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Danh mục yêu thích</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link rel="stylesheet" href="assets/css/user-infor.css" />
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
<div class="container">
    <div class="box-left">
        <div class="subTitle">
            <h5>TRANG TÀI KHOẢN</h5>
            <p>
                <b>Xin chào, </b>
                <b style="color: hsl(0, 100%, 60%); font-weight: 550">
                    ${sessionScope.userName}
                </b>
                !
            </p>
        </div>
        <ul class="option-list">
            <li><a href="userInformation">Thông tin tài khoản</a></li>
            <li><a href="your-order">Đơn hàng của bạn</a></li>
            <li><a href="book-shelf">Tủ sách của bạn</a></li>
            <li class="selected"><a href="#">Danh mục yêu thích</a></li>
            <li><a href="change-password">Đổi mật khẩu</a></li>
        </ul>
    </div>
    <div class="box-right">
        <div class="subTitle">
            <h5>Danh mục yêu thích của bạn</h5>

            <div class="wishlist-container" id="wishListContainer">

                <c:if test="${empty wishlist}">
                    <p>Bạn chưa có sách nào trong wishlist.</p>
                </c:if>

                <c:forEach var="book" items="${wishlist}">
                    <div class="wishlist-item">

                        <img src="<c:url value='${book.images[0].imgLink}' />" alt="${book.title}"/>

                        <div class="infor">
                            <h3>${book.title}</h3>
                            <p>Mã số: ${book.EBookCode}</p>

                            <p>
                                Tác giả:
                                <c:choose>
                                    <c:when test="${book.authors != null}">
                                        ${book.authors[0].authorName}
                                    </c:when>
                                    <c:otherwise>
                                        Đang cập nhật
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <p class="wishlist-price"><fmt:formatNumber value="${book.price}" type="number"/>đ</p>
                        </div>

                        <div class="actions">

                            <!-- Thêm vào giỏ -->
                            <form class="add-to-cart-form"
                                  action="${pageContext.request.contextPath}/cart" method="post">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="bookId" value="${book.id}">
                                <input type="hidden" name="price" value="${book.price}">
                                <button class="add-cart" type="submit">
                                    <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ
                                </button>
                            </form>

                            <!-- Xóa wishlist -->
                            <form class="wishlist-remove-form"
                                  action="${pageContext.request.contextPath}/wishlist"
                                  method="post"
                                  data-title="Xoá khỏi danh sách yêu thích?"
                                  data-desc="${book.title}">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="ebookId" value="${book.id}">
                                <button type="button"
                                        class="remove wishlist-delete-trigger">
                                    <i class="fa-solid fa-trash-can"></i> Xoá
                                </button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<%-- Modal xác nhận xoá wishlist --%>
<div id="wishlistDeleteModal" class="ui-modal-overlay"
     style="display:none;" aria-modal="true" role="dialog">
    <div class="ui-modal-box">
        <div class="ui-modal-icon">
            <i class="fa-solid fa-trash-can" style="color:#e53e3e;font-size:1.6rem;"></i>
        </div>
        <h3 class="ui-modal-title">Xoá khỏi danh mục yêu thích?</h3>
        <p class="ui-modal-desc" id="wishlistDeleteModalDesc"></p>
        <div class="ui-modal-actions">
            <button type="button" class="ui-modal-cancel"
                    id="wishlistDeleteModalCancel">Quay lại</button>
            <button type="button" class="ui-modal-confirm"
                    id="wishlistDeleteModalConfirm">
                <i class="fa-solid fa-trash-can"></i> Xoá
            </button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
<script>window.ctxPath = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/wishlist-remove.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
</body>
</html>
