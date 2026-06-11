<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Ebook Store</title>
    <link rel="stylesheet" href="assets/css/base.css"/>
    <link rel="stylesheet" href="assets/css/components.css"/>
    <link rel="stylesheet" href="assets/css/home.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/discount-price.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png"/>
    <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet"/>
</head>
<body>
<button id="backToTopBtn" class="back-to-top"><i class="fa-solid fa-arrow-up"></i></button>
<jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

<div class="home-image">
    <img src="${homeTop.url}" alt="homeTop"/>
    <p class="slogan">Digital books, infinite stories.</p>
</div>

<div class="container">
    <div class="danh-muc-noi-bat">
        <h5>Một vài danh mục:</h5>
        <div class="danh-sach-danh-muc">
            <c:forEach var="category" items="${randomCategory}">
                <a href="${pageContext.request.contextPath}/list-book?sortBy=created_at&sortDir=desc&category=${category.id}&free=&page=1">
                    <div class="danh-muc">
                        <i class="${category.icon}"></i>
                        <p>${category.name}</p>
                    </div>
                </a>
            </c:forEach>
        </div>

        <!-- ══ SẢN PHẨM MỚI ══════════════════════════════════════════════ -->
        <div class="san-pham-moi">
            <h5>Sản phẩm mới:</h5>
            <div class="slider-container">
                <button class="prev-btn"><i class="fa-solid fa-arrow-left"></i></button>
                <div class="slider">
                    <jsp:useBean id="newEBooks" scope="request" type="java.util.List"/>
                    <c:forEach var="eb" items="${newEBooks}">
                        <div class="product-card" title="${eb.title}">
                            <c:choose>
                                <c:when test="${not empty sessionScope.userID}">
                                    <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                        <input type="hidden" name="ebookId" value="${eb.id}">
                                        <input type="hidden" name="action" value="${wishlistIds != null && wishlistIds.contains(eb.id) ? 'remove' : 'add'}">
                                        <button type="submit" class="favorite-btn ${wishlistIds.contains(eb.id) ? 'active' : ''}">
                                            <i class="fa-solid fa-heart"></i>
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" class="favorite-btn" data-guest-wishlist="true">
                                        <i class="fa-regular fa-heart"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>

                            <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                                <div class="img-wrapper">
                                    <img src="${eb.imageLink}" alt="${eb.title}"
                                         onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/default-book.png';">
                                </div>
                            </a>
                            <p class="book-title">
                                <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">${eb.title}</a>
                            </p>
                            <div>
                                <c:choose>
                                    <c:when test="${eb.price eq 0}">
                                        <span class="price--free">Free!!!</span>
                                    </c:when>
                                    <c:when test="${eb.hasDiscount}">
                                        <span class="price--final"><fmt:formatNumber value="${eb.finalPrice}" type="number"/>đ</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="price"><fmt:formatNumber value="${eb.price}" type="number"/>đ</span>
                                    </c:otherwise>
                                </c:choose>
                                <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                    <input type="hidden" name="action" value="add"/>
                                    <input type="hidden" name="bookId" value="${eb.id}"/>
                                    <input type="hidden" name="price" value="${eb.hasDiscount ? eb.finalPrice : eb.price}"/>
                                    <button type="submit" class="add-to-cart-btn"><i class="fa-solid fa-cart-plus"></i></button>
                                </form>
                            </div>
                            <c:if test="${eb.hasDiscount}">
                                <div class="card-row-2">
                                    <span class="price--original"><del><fmt:formatNumber value="${eb.price}" type="number"/>đ</del></span>
                                    <span class="badge-discount">${eb.discountLabel}</span>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                    <button class="next-btn"><i class="fa-solid fa-arrow-right"></i></button>
                </div>
            </div>

            <!-- ══ SẢN PHẨM BÁN CHẠY ════════════════════════════════════ -->
            <div class="sp-noi-bat">
                <h5>Sản phẩm bán chạy:</h5>
                <div class="sp-noi-bat-container">
                    <div class="sp-noi-bat-grid">
                        <c:forEach var="eb" items="${topSaleEBooks}">
                            <div class="product-card" title="${eb.title}">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.userID}">
                                        <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                            <input type="hidden" name="ebookId" value="${eb.id}">
                                            <input type="hidden" name="action" value="${wishlistIds != null && wishlistIds.contains(eb.id) ? 'remove' : 'add'}">
                                            <button type="submit" class="favorite-btn ${wishlistIds.contains(eb.id) ? 'active' : ''}">
                                                <i class="fa-solid fa-heart"></i>
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="favorite-btn" data-guest-wishlist="true">
                                            <i class="fa-regular fa-heart"></i>
                                        </button>
                                    </c:otherwise>
                                </c:choose>

                                <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                                    <div class="img-wrapper">
                                        <img src="${eb.imageLink}" alt="${eb.title}"
                                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/default-book.png';">
                                    </div>
                                </a>
                                <p class="book-title">
                                    <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">${eb.title}</a>
                                </p>
                                <div>
                                    <c:choose>
                                        <c:when test="${eb.price eq 0}">
                                            <span class="price--free">Free!!!</span>
                                        </c:when>
                                        <c:when test="${eb.hasDiscount}">
                                            <span class="price--final"><fmt:formatNumber value="${eb.finalPrice}" type="number"/>đ</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="price"><fmt:formatNumber value="${eb.price}" type="number"/>đ</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                        <input type="hidden" name="action" value="add"/>
                                        <input type="hidden" name="bookId" value="${eb.id}"/>
                                        <input type="hidden" name="price" value="${eb.hasDiscount ? eb.finalPrice : eb.price}"/>
                                        <button type="submit" class="add-to-cart-btn"><i class="fa-solid fa-cart-plus"></i></button>
                                    </form>
                                </div>
                                <c:if test="${eb.hasDiscount}">
                                    <div class="card-row-2">
                                        <span class="price--original"><del><fmt:formatNumber value="${eb.price}" type="number"/>đ</del></span>
                                        <span class="badge-discount">${eb.discountLabel}</span>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                    <a href="list-book" id="xem-tat-ca" class="xem-tat-ca">Xem tất cả</a>
                </div>
            </div>

            <div class="banner">
                <img src="${HomeMiddleLeft.url}" alt="banner"/>
                <img src="${HomeMiddleRight.url}" alt="banner"/>
            </div>

            <!-- ══ SẢN PHẨM THEO DANH MỤC ═══════════════════════════════ -->
            <section class="sp-theo-danh-muc">
                <h5>Sản phẩm theo danh mục:</h5>
                <div class="sp-container">
                    <div class="sp-banner">
                        <img src="${rdBookimg1}" alt="Banner 1"/>
                        <img src="${rdBookimg2}" alt="Banner 2"/>
                    </div>
                    <div class="sp-content">
                        <div class="sp-noi-bat-grid">
                            <c:forEach var="eb" items="${randomEbook}">
                                <div class="product-card" title="${eb.title}">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.userID}">
                                            <form action="${pageContext.request.contextPath}/wishlist" method="post">
                                                <input type="hidden" name="ebookId" value="${eb.id}">
                                                <input type="hidden" name="action" value="${wishlistIds != null && wishlistIds.contains(eb.id) ? 'remove' : 'add'}">
                                                <button type="submit" class="favorite-btn ${wishlistIds.contains(eb.id) ? 'active' : ''}">
                                                    <i class="fa-solid fa-heart"></i>
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="button" class="favorite-btn" data-guest-wishlist="true">
                                                <i class="fa-regular fa-heart"></i>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>

                                    <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                                        <div class="img-wrapper">
                                            <img src="${eb.imageLink}" alt="${eb.title}"
                                                 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/default-book.png';">
                                        </div>
                                    </a>
                                    <p class="book-title">
                                        <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">${eb.title}</a>
                                    </p>
                                    <div>
                                        <c:choose>
                                            <c:when test="${eb.price eq 0}">
                                                <span class="price--free">Free!!!</span>
                                            </c:when>
                                            <c:when test="${eb.hasDiscount}">
                                                <span class="price--final"><fmt:formatNumber value="${eb.finalPrice}" type="number"/>đ</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="price"><fmt:formatNumber value="${eb.price}" type="number"/>đ</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                                            <input type="hidden" name="action" value="add"/>
                                            <input type="hidden" name="bookId" value="${eb.id}"/>
                                            <input type="hidden" name="price" value="${eb.hasDiscount ? eb.finalPrice : eb.price}"/>
                                            <button type="submit" class="add-to-cart-btn"><i class="fa-solid fa-cart-plus"></i></button>
                                        </form>
                                    </div>
                                    <c:if test="${eb.hasDiscount}">
                                        <div class="card-row-2">
                                            <span class="price--original"><del><fmt:formatNumber value="${eb.price}" type="number"/>đ</del></span>
                                            <span class="badge-discount">${eb.discountLabel}</span>
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="more">
                            <div class="all-product-button"><button>Xem tất cả</button></div>
                        </div>
                    </div>
                </div>
            </section>

            <div class="ebook-description">
                <div class="ebook-image">
                    <img src="https://tse2.mm.bing.net/th/id/OIP.CWCAk80qMr8GQe3zEFt-wwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3" alt="EBookStore"/>
                </div>
                <div class="ebook-content">
                    <div class="intro">
                        <h2>Giới thiệu về eBookStore</h2>
                        <p>Bạn là tín đồ sách và yêu thích không gian yên tĩnh? Tại đây, bạn không chỉ tìm thấy những cuốn sách hay mà còn có cơ hội gặp gỡ những người bạn cùng sở thích. Hãy đến Ebook để khám phá thế giới tri thức và tận hưởng những giây phút thư giãn tuyệt vời.</p>
                    </div>
                    <div class="why-choose">
                        <h3>Tại sao chọn chúng tôi</h3>
                        <div class="why-list">
                            <div class="why-item">
                                <div class="icon">🛍️</div>
                                <div class="text"><h4>Mua sắm</h4><p>Với hơn 10,000 đầu sách thuộc nhiều lĩnh vực khác nhau cho các bạn thỏa sức lựa chọn.</p></div>
                            </div>
                            <div class="why-item">
                                <div class="icon">👩‍💼</div>
                                <div class="text"><h4>Đội ngũ nhân viên thân thiện</h4><p>Đội ngũ nhân viên thân thiện và nhiệt tình của chúng tôi sẽ tư vấn và hỗ trợ bạn chọn lựa sản phẩm phù hợp.</p></div>
                            </div>
                            <div class="why-item">
                                <div class="icon">💬</div>
                                <div class="text"><h4>Đồng hành và hỗ trợ</h4><p>Chúng tôi đồng hành và hỗ trợ bạn trong suốt quá trình sử dụng sản phẩm.</p></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script>
    const ctx = "${pageContext.request.contextPath}";
    window.ctxPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-card.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/backToTopBtn.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
</body>
</html>
