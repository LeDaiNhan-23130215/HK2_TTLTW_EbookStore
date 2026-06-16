<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="vi_VN"/>

<div class="product-grid">
    
</div>

<div class="pagination">
    <c:if test="${currentPage > 1}">
        <a class="nav-btn" href="list-book?page=${currentPage - 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>"></a>
    </c:if>
    <c:forEach begin="1" end="${totalPages}" var="i">
        <a class="page-btn ${i == currentPage ? 'active' : ''}"
           href="list-book?page=${i}<c:if test='${not empty queryString}'>&${queryString}</c:if>">${i}</a>
    </c:forEach>
    <c:if test="${currentPage < totalPages}">
        <a class="nav-btn" href="list-book?page=${currentPage + 1}<c:if test='${not empty queryString}'>&${queryString}</c:if>">»</a>
    </c:if>
</div>
