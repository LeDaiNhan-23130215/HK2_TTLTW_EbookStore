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
          <form class="add-to-cart-form"
                action="${pageContext.request.contextPath}/cart" method="post">
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
<script>window.ctxPath = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-card.js"></script>
<script>
  // Wishlist AJAX for bookdetail — form is outside .product-card, wire it directly
  (function () {
    var ctx = window.ctxPath || '';
    function showToast(msg, type) {
      var existing = document.getElementById('cartToast');
      if (existing) existing.remove();
      var colors = { success:'#27ae60', warning:'#f59e0b', error:'#e53e3e', info:'#0396c7' };
      var el = document.createElement('div');
      el.id = 'cartToast';
      el.textContent = msg;
      el.style.cssText = [
        'position:fixed','top:20px','right:20px','z-index:9999',
        'padding:14px 22px','border-radius:8px','font-size:14px',
        'color:#fff','box-shadow:0 4px 12px rgba(0,0,0,.25)',
        'max-width:320px','word-break:break-word',
        'background:' + (colors[type] || colors.info)
      ].join(';');
      document.body.appendChild(el);
      setTimeout(function () { if (el.parentNode) el.remove(); }, 4000);
    }

    var form = document.querySelector('.wishlist-form');
    if (!form) return;
    var btn = form.querySelector('.favorite-btn');
    if (!btn) return;

    btn.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();

      var ebookId = form.querySelector('[name=ebookId]').value;
      var action  = form.querySelector('[name=action]').value;

      var body = new URLSearchParams();
      body.set('ebookId', ebookId);
      body.set('action',  action);

      fetch(ctx + '/wishlist', {
        method:  'POST',
        headers: { 'X-Requested-With': 'XMLHttpRequest' },
        body:    body
      })
              .then(function (r) { return r.json(); })
              .then(function (data) {
                if (data.msg) showToast(data.msg, data.type || 'success');
                var actionInput = form.querySelector('[name=action]');
                var icon = btn.querySelector('i');
                if (data.inWishlist) {
                  btn.classList.add('active');
                  if (actionInput) actionInput.value = 'remove';
                  if (icon) { icon.classList.remove('fa-regular'); icon.classList.add('fa-solid'); }
                } else {
                  btn.classList.remove('active');
                  if (actionInput) actionInput.value = 'add';
                  if (icon) { icon.classList.remove('fa-solid'); icon.classList.add('fa-regular'); }
                }
              })
              .catch(function () {
                showToast('Đã xảy ra lỗi, vui lòng thử lại.', 'error');
              });
    });
  })();
</script>
</body>
</html>
