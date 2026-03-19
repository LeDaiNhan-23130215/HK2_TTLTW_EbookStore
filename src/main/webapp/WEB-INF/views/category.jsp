<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Danh mục</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link rel="stylesheet" href="assets/css/category.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
  </head>
  <body>
    <jsp:include page="/WEB-INF/views/header.jsp"/>
    
    <main class="main-filters">
      <div class="main-container">
        <!-- Banner bên trái -->
        <div class="side-banner">
          <img
            src="https://tse2.mm.bing.net/th/id/OIP.TgM9m1BkUQxyAeISQc3SqwHaLY?cb=ucfimg2ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
            alt="banner"
          />
        </div>

        <!-- Nội dung chính -->
        <form method="get" action="list-book" class="main-content">

          <h3>Thể loại</h3>

          <c:set var="selectedCategories" value="${paramValues.category}" />

          <div class="filter-row">
            <c:forEach var="cat" items="${categories}">
              <div class="filter-item">
                <label class="filter-option">
                  <input type="checkbox"
                         name="category"
                         value="${cat.id}"
                  <c:if test="${fn:contains(selectedCategories, cat.id.toString())}">
                         checked
                  </c:if>
                  >
                  <i class="${cat.icon}"></i>
                  <span class="cat-name">${cat.name}</span>
                </label>

                <span class="cat-desc">
                    ${cat.description}
                </span>
              </div>
            </c:forEach>
          </div>

          <button type="submit" class="btn-filter">
            Lọc sách
          </button>
        </form>

        <!-- Banner bên phải -->
        <div class="side-banner">
          <img
            src="https://tse2.mm.bing.net/th/id/OIP.TgM9m1BkUQxyAeISQc3SqwHaLY?cb=ucfimg2ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3"
            alt="banner"
          />
        </div>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/footer.jsp"/>

    <script src="assets/js/category.js"></script>
    <script src="assets/js/component.js"></script>
    <script src="assets/js/backToTopBtn.js"></script>
  </body>
</html>
