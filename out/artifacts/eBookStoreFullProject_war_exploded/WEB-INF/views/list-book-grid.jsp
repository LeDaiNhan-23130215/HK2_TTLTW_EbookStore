<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="vi_VN"/>

<div class="product-grid">

    <c:if test="${not empty newEBooks}">
        <c:forEach var="eb" items="${newEBooks}">
            <div class="product-card" title="${eb.title}">

                <!-- WISHLIST -->
                <form action="${pageContext.request.contextPath}/wishlist" method="post">
                    <input type="hidden" name="ebookId" value="${eb.id}"/>

                    <c:choose>
                        <c:when test="${wishlistIds != null and wishlistIds.contains(eb.id)}">
                            <input type="hidden" name="action" value="remove"/>
                            <button type="submit" class="favorite-btn active" title="Remove from wishlist">
                                <i class="fa-solid fa-heart"></i>
                            </button>
                        </c:when>

                        <c:otherwise>
                            <input type="hidden" name="action" value="add"/>
                            <button type="submit" class="favorite-btn" title="Add to wishlist">
                                <i class="fa-solid fa-heart"></i>
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>

                <!-- IMAGE -->
                <div class="img-wrapper">
                    <img src="${eb.imageLink}" alt="${eb.title}"/>
                </div>

                <!-- TITLE -->
                <p>${eb.title}</p>

                <div>

                    <!-- PRICE -->
                    <c:if test="${eb.price != null and eb.price gt 0}">
                        <span class="price">
                            <fmt:formatNumber value="${eb.price}" type="currency"/>
                        </span>
                    </c:if>

                    <c:if test="${eb.price eq 0}">
                        <span>Free!!!</span>
                    </c:if>

                    <!-- ADD TO CART -->
                    <form action="${pageContext.request.contextPath}/cart" method="post" class="add-to-cart-form">
                        <input type="hidden" name="action" value="add"/>
                        <input type="hidden" name="bookId" value="${eb.id}"/>
                        <input type="hidden" name="price" value="${eb.price}"/>

                        <button type="submit" class="add-to-cart-btn">
                            <i class="fa-solid fa-cart-plus"></i>
                        </button>
                    </form>

                </div>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${empty newEBooks}">
        <p>Không tìm thấy kết quả!!!</p>
    </c:if>

</div>


<!-- PAGINATION -->
<div class="pagination">

    <c:if test="${currentPage > 1}">
        <a class="nav-btn"
           href="list-book?page=${currentPage - 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>">
            «
        </a>
    </c:if>

    <c:forEach begin="1" end="${totalPages}" var="i">
        <a class="page-btn ${i == currentPage ? 'active' : ''}"
           href="list-book?page=${i}<c:if test='${not empty queryString}'>&${queryString}</c:if>">
                ${i}
        </a>
    </c:forEach>

    <c:if test="${currentPage < totalPages}">
        <a class="nav-btn"
           href="list-book?page=${currentPage + 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>">
            »
        </a>
    </c:if>

</div>
