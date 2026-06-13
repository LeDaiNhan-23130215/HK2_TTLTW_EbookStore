<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>
    <c:choose>
      <c:when test="${empty discount}">Thêm</c:when>
      <c:otherwise>Sửa</c:otherwise>
    </c:choose>
    chương trình giảm giá
  </title>

  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-discount.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body>

<!-- Sidebar -->
<aside class="sidebar">
  <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
  <nav class="sidebar-nav">
    <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
    <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
    <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
    <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
    <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
    <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
    <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
    <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
    <a href="${pageContext.request.contextPath}/admin-review">Review</a>
    <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
    <a href="${pageContext.request.contextPath}/admin-discount" class="active">Giảm giá</a>
    <a href="${pageContext.request.contextPath}/admin-voucher">Mã giảm giá</a>
    <a href="${pageContext.request.contextPath}/admin-logs">System Logs</a>
    <hr>
    <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
  </nav>
</aside>

<div class="main-content">
  <header class="topbar">
    <div class="topbar-title">
      <i class="fa-solid fa-tag"></i>
      <c:choose>
        <c:when test="${empty discount}">Thêm chương trình giảm giá</c:when>
        <c:otherwise>Sửa chương trình: ${discount.name}</c:otherwise>
      </c:choose>
    </div>
    <button id="toggle-theme">🌙 Dark Mode</button>
  </header>

  <section class="dashboard">
    <form action="${pageContext.request.contextPath}/admin-discount"
          method="post" id="discountForm">

      <%-- ID ẩn khi edit --%>
      <c:if test="${not empty discount}">
        <input type="hidden" name="id" value="${discount.id}"/>
      </c:if>

      <%-- ── THÔNG TIN CƠ BẢN ───────────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-circle-info"></i> Thông tin cơ bản</h4>

        <div class="form-row">
          <label>Tên chương trình <span style="color:red">*</span></label>
          <input type="text" name="name" required maxlength="255"
                 value="${discount.name}"
                 placeholder="VD: Flash Sale tháng 6"/>
        </div>

        <div class="form-row">
          <label>Mô tả</label>
          <textarea name="description" rows="3"
                    placeholder="Mô tả ngắn về chương trình...">${discount.description}</textarea>
        </div>

        <div class="form-row">
          <label>Loại giảm giá <span style="color:red">*</span></label>
          <select name="discountType" id="discountType" required onchange="updateValueHint()">
            <option value="PERCENT"
                    <c:choose>
                      <c:when test="${discount.discountType eq 'PERCENT'}">selected</c:when>
                      <c:when test="${empty discount}">selected</c:when>
                    </c:choose>>
              Phần trăm (%)
            </option>
            <option value="FIXED"
            ${discount.discountType eq 'FIXED' ? 'selected' : ''}>
              Cố định (₫)
            </option>
          </select>
        </div>

        <div class="form-row">
          <label>Mức giảm <span style="color:red">*</span></label>
          <div class="combo-with-custom">
            <select id="discountValuePreset">
              <%-- Các option sẽ được JS sinh động dựa theo loại giảm giá --%>
            </select>
            <div class="custom-input-wrap" id="discountValueCustomWrap">
              <div class="discount-input-group">
                <button type="button" class="discount-btn" onclick="stepValue(-1)">−</button>
                <input type="number" name="discountValue" id="discountValue"
                       required min="0.01" step="any"
                       data-initial-value="${discount.discountValue}"
                       value="${discount.discountValue}"
                       placeholder="VD: 20 (cho %) hoặc 50000 (cho ₫)"/>
                <button type="button" class="discount-btn" onclick="stepValue(1)">+</button>
              </div>
            </div>
          </div>
          <small id="valueHint" class="hint"></small>
          <small id="amountWords" class="hint" style="color:#0396c7;font-style:italic;display:none;"></small>
        </div>

        <div class="form-row">
          <label>Trạng thái</label>
          <select name="status">
            <option value="ACTIVE"
                    <c:choose>
                      <c:when test="${discount.status eq 'ACTIVE'}">selected</c:when>
                      <c:when test="${empty discount}">selected</c:when>
                    </c:choose>>Hoạt động</option>
            <option value="INACTIVE"
            ${discount.status eq 'INACTIVE' ? 'selected' : ''}>Tạm dừng</option>
            <option value="ENDED"
            ${discount.status eq 'ENDED' ? 'selected' : ''}>Kết thúc</option>
          </select>
        </div>
      </div>

      <%-- ── THỜI GIAN ──────────────────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-calendar-days"></i> Thời gian áp dụng</h4>
        <p class="hint">Để trống nếu không giới hạn thời gian. Chương trình sẽ tự
          chuyển sang "Kết thúc" khi hết ngày kết thúc.</p>

        <div class="form-row">
          <label>Ngày bắt đầu</label>
          <input type="datetime-local" name="startDate"
                 value="${discount.startDateForInput}"/>
        </div>

        <div class="form-row">
          <label>Ngày kết thúc</label>
          <input type="datetime-local" name="endDate"
                 value="${discount.endDateForInput}"/>
        </div>
      </div>

      <%-- ── ĐỐI TƯỢNG ÁP DỤNG ─────────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-bullseye"></i> Đối tượng áp dụng</h4>
        <p class="hint">
          Chọn một hoặc nhiều nhóm. Ebook được tính vào chương trình nếu khớp bất kỳ
          điều kiện nào (ebook cụ thể <strong>hoặc</strong> theo danh mục
          <strong>hoặc</strong> theo tác giả). <br>
          <i class="fa-solid fa-circle-info"></i>
          Ebook miễn phí (Free) vẫn có thể chọn để lưu dữ liệu, nhưng sẽ
          <strong>không hiển thị badge giảm giá</strong> — trang sản phẩm chỉ hiện "Free!!!".
        </p>

        <div class="tab-btns">
          <button type="button" class="tab-btn active" onclick="switchTab('ebook')">
            <i class="fa-solid fa-book"></i> Ebook cụ thể
          </button>
          <button type="button" class="tab-btn" onclick="switchTab('category')">
            <i class="fa-solid fa-folder"></i> Danh mục
          </button>
          <button type="button" class="tab-btn" onclick="switchTab('author')">
            <i class="fa-solid fa-user-pen"></i> Tác giả
          </button>
        </div>

        <%-- Tab Ebook --%>
        <div id="tab-ebook" class="tab-panel show">
          <div class="target-list">
            <c:forEach var="e" items="${allEbooks}">
              <label>
                <input type="checkbox" name="ebookIds" value="${e.id}"
                  ${selectedEbooks != null && selectedEbooks.contains(e.id) ? 'checked' : ''}/>
                  ${e.title}
                <c:if test="${e.price eq 0}">
                  <span class="free-label">(Free)</span>
                </c:if>
              </label>
            </c:forEach>
          </div>
        </div>

        <%-- Tab Category --%>
        <div id="tab-category" class="tab-panel">
          <div class="target-list">
            <c:forEach var="cat" items="${allCategories}">
              <label>
                <input type="checkbox" name="categoryIds" value="${cat.id}"
                  ${selectedCategories != null && selectedCategories.contains(cat.id) ? 'checked' : ''}/>
                  ${cat.name}
              </label>
            </c:forEach>
          </div>
        </div>

        <%-- Tab Author --%>
        <div id="tab-author" class="tab-panel">
          <div class="target-list">
            <c:forEach var="a" items="${allAuthors}">
              <label>
                <input type="checkbox" name="authorIds" value="${a.id}"
                  ${selectedAuthors != null && selectedAuthors.contains(a.id) ? 'checked' : ''}/>
                  ${a.authorName}
              </label>
            </c:forEach>
          </div>
        </div>
      </div>

      <%-- ── ACTIONS ─────────────────────────────────────────────────────────── --%>
      <div style="display:flex; gap:1rem; margin-top:.5rem;">
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i>
          <c:choose>
            <c:when test="${empty discount}">Tạo chương trình</c:when>
            <c:otherwise>Lưu thay đổi</c:otherwise>
          </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/admin-discount" class="btn btn-secondary">
          <i class="fa-solid fa-arrow-left"></i> Quay lại
        </a>
      </div>

    </form>
  </section>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-discount-form.js"></script>
</body>
</html>