<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="vi_VN"/>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>EBook Store</title>

  <link rel="stylesheet" href="<c:url value='/assets/css/base.css' />">
  <link rel="stylesheet" href="<c:url value='/assets/css/components.css' />">
  <link rel="stylesheet" href="<c:url value='/assets/css/list-book.css' />">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>

  <link rel="icon" type="image/png"
        href="<c:url value='/assets/img/ebook-logo2.png' />">
</head>

<body>

<button id="backToTopBtn" class="back-to-top">
  <i class="fa-solid fa-arrow-up"></i>
</button>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<main class="container">
  <!-- ================= FILTER SIDEBAR ================= -->
  <aside class="sidebar">
    <form id="filterForm" method="get" action="list-book">

      <!-- Hidden inputs to preserve sorting when filtering -->
      <input type="hidden" name="sortBy" value="${filter.sortBy}">
      <input type="hidden" name="sortDir" value="${filter.sortDir}">

      <!-- ===== CATEGORY ===== -->
      <div class="filter">
        <h3>Thể loại</h3>

        <c:forEach var="cat" items="${categories}">
          <label>
            <input type="checkbox" name="category" value="${cat.id}"
                   <c:if test="${filter.hasCategory(cat.id)}">checked</c:if>>
              ${cat.name}
          </label>
        </c:forEach>

        <a href="category">
          <i class="fa-solid fa-bars"></i> Tất cả danh mục
        </a>
      </div>


      <!-- ===== PRICE ===== -->
      <div class="filter">
        <h3>Giá</h3>

        <label>
          <input type="radio" name="free" value="true"
                 <c:if test="${filter.free == true}">checked</c:if>>
          Miễn phí
        </label>

        <label>
          <input type="radio" name="free" value="false"
                 <c:if test="${filter.free == false}">checked</c:if>>
          Có phí
        </label>

        <label>
          <input type="radio" name="free" value=""
                 <c:if test="${filter.free == null}">checked</c:if>>
          Tất cả
        </label>
      </div>

      <!-- ===== FORMAT ===== -->
      <div class="filter">
        <h3>Loại file</h3>

        <label>
          <input type="checkbox" name="format" value="PDF"
                 <c:if test="${filter.hasFormat('PDF')}">checked</c:if>>
          PDF
        </label>

        <label>
          <input type="checkbox" name="format" value="EPUB"
                 <c:if test="${filter.hasFormat('EPUB')}">checked</c:if>>
          EPUB
        </label>
      </div>
    </form>
  </aside>

  <!-- ================= MAIN CONTENT ================= -->
  <section class="content">

    <!-- ===== SORT BAR ===== -->
    <div class="local-filters">
      <div class="local-filter-menu">
        <button class="hamburger-btn">
          <i class="fa-solid fa-bars"></i> Bộ lọc
        </button>

        <div class="local-filter-button-container">
          <a href="list-book?sortBy=title&sortDir=asc<c:if test='${not empty queryStringForSort}'>&${queryStringForSort}</c:if>"
             class="sort-button ${filter.sortBy == 'title' && filter.sortDir == 'asc' ? 'active' : ''}">
            <i class="fa-solid fa-arrow-down-a-z"></i> A - Z
          </a>

          <a href="list-book?sortBy=title&sortDir=desc<c:if test='${not empty queryStringForSort}'>&${queryStringForSort}</c:if>"
             class="sort-button ${filter.sortBy == 'title' && filter.sortDir == 'desc' ? 'active' : ''}">
            <i class="fa-solid fa-arrow-up-a-z"></i> Z - A
          </a>

          <a href="list-book?sortBy=price&sortDir=asc<c:if test='${not empty queryStringForSort}'>&${queryStringForSort}</c:if>"
             class="sort-button ${filter.sortBy == 'price' && filter.sortDir == 'asc' ? 'active' : ''}">
            <i class="fa-solid fa-arrow-up"></i> Giá ↑
          </a>

          <a href="list-book?sortBy=price&sortDir=desc<c:if test='${not empty queryStringForSort}'>&${queryStringForSort}</c:if>"
             class="sort-button ${filter.sortBy == 'price' && filter.sortDir == 'desc' ? 'active' : ''}">
            <i class="fa-solid fa-arrow-down"></i> Giá ↓
          </a>

          <a href="list-book?sortBy=created_at&sortDir=desc<c:if test='${not empty queryStringForSort}'>&${queryStringForSort}</c:if>"
             class="sort-button ${filter.sortBy == 'created_at' && filter.sortDir == 'desc' ? 'active' : ''}">
            <i class="fa-solid fa-calendar"></i> Mới nhất
          </a>
        </div>
      </div>
    </div>
    <div id="active-filters" class="active-filters"></div>
    <div id="grid-container">
      <jsp:include page="/WEB-INF/views/list-book-grid.jsp"/>
    </div>

  </section>

</main>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="<c:url value='/assets/js/product-card.js' />"></script>

<script>
    const ctx = "${pageContext.request.contextPath}";
</script>

<script src="<c:url value='/assets/js/backToTopBtn.js' />"></script>
<script src="<c:url value='/assets/js/component.js' />"></script>
<script src="<c:url value='/assets/js/hamburger-button.js' />"></script>

<script src="<c:url value='/assets/js/list-book-pagination.js' />"></script>

<script src="<c:url value='/assets/js/list-book-filter.js' />"></script>
</body>
</html>