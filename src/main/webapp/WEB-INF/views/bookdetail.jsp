<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>${ebook.title} | EBookStore</title>

  <link rel="icon" type="image/png"
        href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bookdetail.css"/>

  <link rel="stylesheet"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
</head>

<script>
  function showTab(index) {
    const buttons = document.querySelectorAll(".tab-btn");
    const contents = document.querySelectorAll(".tab-content");

    buttons.forEach(b => b.classList.remove("active"));
    contents.forEach(c => c.classList.remove("active"));

    buttons[index].classList.add("active");
    contents[index].classList.add("active");
  }
</script>

<body>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="container">
  <div class="product-wrapper">

    <!-- LEFT: IMAGE -->
    <div class="col-left gallery-section">
      <div class="main-image-box">
        <img src="${ebook.images[0].imgLink}" alt="${ebook.title}">
      </div>
    </div>

    <!-- CENTER: INFO -->
    <div class="col-center product-info">

      <span class="badge">EBOOK</span>

      <h1 class="product-title">${ebook.title}</h1>

      <div class="meta-row">
        <p>
          Tác giả:
          <c:forEach var="author" items="${ebook.authors}">
            <strong>${author.authorName}</strong>
          </c:forEach>
        </p>
        <p>Mã SP: <span>#${ebook.EBookCode}</span></p>
      </div>

      <div class="price-box">
        <span class="current-price">${ebook.price} đ</span>
      </div>

      <!-- ===== ACTION BUTTONS ===== -->
      <div class="actions-wrapper">
        <div class="btn-group">

          <!-- ADD TO CART -->
          <form action="${pageContext.request.contextPath}/cart" method="post">
            <input type="hidden" name="action" value="add"/>
            <input type="hidden" name="bookId" value="${ebook.id}"/>
            <input type="hidden" name="price" value="${ebook.price}"/>
            <input type="hidden" name="quantity" value="1"/>
            <button type="submit" class="btn btn-primary">
              Thêm vào giỏ
            </button>
          </form>

          <!-- READ SAMPLE -->
          <a href="${pageContext.request.contextPath}/readbook?id=${ebook.id}&page=1"
             class="btn btn-docthu">
            Đọc thử
          </a>



          <!-- ❤️ WISHLIST (NẰM TRONG BTN-GROUP) -->
          <c:choose>
            <c:when test="${not empty sessionScope.userID}">
              <form class="wishlist-form"
                    action="${pageContext.request.contextPath}/wishlist"
                    method="post">
                <input type="hidden" name="ebookId" value="${ebook.id}">
                <input type="hidden" name="action"
                       value="${wishlistIds != null && wishlistIds.contains(ebook.id) ? 'remove' : 'add'}">

                <button type="submit"
                        class="favorite-btn ${wishlistIds != null && wishlistIds.contains(ebook.id) ? 'active' : ''}">
                  <i class="${wishlistIds != null && wishlistIds.contains(ebook.id)
                                                ? 'fa-solid'
                                                : 'fa-regular'} fa-heart"></i>
                </button>
              </form>
            </c:when>

            <c:otherwise>
              <button type="button"
                      class="favorite-btn"
                      onclick="alert('Vui lòng đăng nhập để sử dụng chức năng này')">
                <i class="fa-regular fa-heart"></i>
              </button>
            </c:otherwise>
          </c:choose>

        </div>
      </div>

    </div>

    <!-- RIGHT: RELATED -->
    <div class="similar-list">
      <c:forEach items="${similarEbooks}" var="e">
        <a class="similar-item"
           href="${pageContext.request.contextPath}/bookdetail?id=${e.id}">

          <img src="${e.images[0].imgLink}" alt="${e.title}">

          <div class="similar-info">
            <span class="title">${e.title}</span>
            <span class="price">
          <fmt:formatNumber value="${e.price}" pattern="#,### đ"/>
        </span>
          </div>

        </a>
      </c:forEach>
    </div>

  </div>

  <!-- ===== TABS ===== -->
  <div class="product-bottom-tabs">
    <div class="tab-headers">
      <button class="tab-btn active" onclick="showTab(0)">
        Mô tả sản phẩm
      </button>
      <button class="tab-btn" onclick="showTab(1)">
        Hướng dẫn mua hàng
      </button>
    </div>


    <div class="tab-content-wrapper">
      <div class="tab-content active">
        <div id="fullDescription">
          ${ebook.description}
        </div>
      </div>

      <div class="tab-content">
        <div class="guide-box">
          <h4>Quy trình mua hàng</h4>
          <ul>
            <li><strong>Bước 1:</strong> Chọn sản phẩm</li>
            <li><strong>Bước 2:</strong> Thêm vào giỏ hàng</li>
            <li><strong>Bước 3:</strong> Nhập thông tin giao hàng</li>
            <li><strong>Bước 4:</strong> Thanh toán</li>
          </ul>
        </div>
      </div>
      <!-- ===== REVIEW LINK ===== -->
      <div class="review-link-wrapper">
        <a href="${pageContext.request.contextPath}/review?id=${ebook.id}"
           class="btn btn-review">
          Xem đánh giá & nhận xét
        </a>
      </div>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>

</body>
</html>
