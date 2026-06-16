<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>

<div class="product-grid">
    <c:if test="${not empty pageView.items}">
        <c:forEach var="eb"
                   items="${pageView.items}">
            <div class="product-card" title="${eb.title}">
                <c:choose>
                    <c:when test="${sessionScope.user != null}">
                        <form action="${pageContext.request.contextPath}/wishlist"
                              method="post">
                            <input type="hidden"
                                   name="ebookId"
                                   value="${eb.id}"/>
                            <c:choose>
                                <c:when test="${wishlistIds != null
                                                and wishlistIds.contains(eb.id)}">
                                    <input type="hidden" name="action"
                                           value="remove"/>
                                    <button type="submit" class="favorite-btn active"
                                            title="Xoá khỏi yêu thích">
                                        <i class="fa-solid fa-heart"></i>
                                    </button>
                                </c:when>

                                <c:otherwise>
                                    <input type="hidden" name="action" value="add"/>
                                    <button type="submit"
                                            class="favorite-btn"
                                            title="Thêm vào yêu thích">
                                        <i class="fa-regular fa-heart"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <button type="button"
                                class="favorite-btn"
                                data-guest-wishlist="true">
                            <i class="fa-regular fa-heart"></i>
                        </button>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                    <div class="img-wrapper">
                        <img src="${eb.imageLink}"
                             alt="${eb.title}" onerror="
                                     this.onerror=null;
                                     this.src='${pageContext.request.contextPath}/assets/img/default-book.png';">
                    </div>
                </a>

                <p class="product-title">
                    <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                            ${eb.title}
                    </a>
                </p>

                <div class="product-bottom">
                    <div class="product-price">
                        <c:choose>
                            <c:when test="${eb.price eq 0}">
                                <span class="price--free">
                                    Free!!!
                                </span>
                            </c:when>

                            <c:when test="${eb.hasDiscount}">
                                <div class="discount-wrapper">
                                    <span class="price--final">
                                        <fmt:formatNumber
                                                value="${eb.finalPrice}"
                                                type="number"/>
                                        đ
                                    </span>
                                    <span class="price--original">
                                        <del>
                                            <fmt:formatNumber
                                                    value="${eb.price}"
                                                    type="number"/>
                                            đ
                                        </del>
                                    </span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <span class="price">
                                    <fmt:formatNumber
                                            value="${eb.price}" type="number"/>đ</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <form action="${pageContext.request.contextPath}/cart"
                          method="post"
                          class="add-to-cart-form">
                        <input type="hidden" name="action" value="add"/>
                        <input type="hidden" name="bookId" value="${eb.id}"/>
                        <input type="hidden" name="price" value="${eb.hasDiscount
                        ? eb.finalPrice
                        : eb.price}"/>
                        <button type="submit" class="add-to-cart-btn">
                            <i class="fa-solid fa-cart-plus"></i>
                        </button>
                    </form>
                </div>

                <c:if test="${eb.hasDiscount}">
                    <div class="card-row-2">
                        <span class="badge-discount">
                                ${eb.discountLabel}
                        </span>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </c:if>
    <c:if test="${empty pageView.items}">
        <div class="empty-state">
            <i class="fa-solid fa-book-open"></i>
            <p>Không tìm thấy kết quả!!!</p>
        </div>
    </c:if>
</div>

<c:if test="${totalPages > 1}">
    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a class="nav-btn" href="list-book?page=${currentPage - 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>"></a>
        </c:if>
        <c:forEach begin="1"
                   end="${totalPages}"
                   var="i">
            <a class="page-btn ${i == currentPage ? 'active' : ''}" href="list-book?page=${i}<c:if test='${not empty queryString}'>&${queryString}</c:if>">${i}</a>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
            <a class="nav-btn" href="list-book?page=${currentPage + 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>"></a>
        </c:if>
    </div>
</c:if>

