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
      <c:when test="${empty voucher}">Tạo</c:when>
      <c:otherwise>Sửa</c:otherwise>
    </c:choose>
    mã giảm giá
  </title>

  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-voucher.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
</head>
<body data-context="${pageContext.request.contextPath}">

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
    <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
    <a href="${pageContext.request.contextPath}/admin-voucher" class="active">Mã giảm giá</a>
    <a href="${pageContext.request.contextPath}/admin-logs">System Logs</a>
    <hr>
    <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
  </nav>
</aside>

<div class="main-content">
  <header class="topbar">
    <div class="topbar-title">
      <i class="fa-solid fa-ticket"></i>
      <c:choose>
        <c:when test="${empty voucher}">Tạo mã giảm giá mới</c:when>
        <c:otherwise>Sửa mã giảm giá: ${voucher.code}</c:otherwise>
      </c:choose>
    </div>
    <button id="toggle-theme">🌙 Dark Mode</button>
  </header>

  <section class="dashboard">

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger" style="margin-bottom:1.5rem;">
        <i class="fa-solid fa-circle-exclamation"></i> ${errorMessage}
      </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/admin-voucher"
          method="post" id="voucherForm">

      <c:if test="${not empty voucher}">
        <input type="hidden" name="id" value="${voucher.id}"/>
      </c:if>

      <%-- ── THÔNG TIN CƠ BẢN ───────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-circle-info"></i> Thông tin cơ bản</h4>

        <div class="form-grid">
          <div class="form-row">
            <label>Mã giảm giá <span style="color:red">*</span></label>
            <div class="code-input-group">
              <input type="text" name="code" id="code" maxlength="50"
                     placeholder="Để trống để tự tạo mã"
                     value="${voucher.code}"/>
              <button type="button" id="btnGenerateCode" class="btn-generate-code">
                Tạo mã tự động
              </button>
            </div>
            <small class="hint">Có thể nhập mã tuỳ ý hoặc bấm "Tạo mã tự động" để sinh mã.</small>
          </div>

          <div class="form-row">
            <label>Trạng thái</label>
            <select name="status">
              <option value="ACTIVE"
                      <c:choose>
                        <c:when test="${not empty voucher && voucher.status eq 'ACTIVE'}">selected</c:when>
                        <c:when test="${empty voucher}">selected</c:when>
                      </c:choose>>Hoạt động</option>
              <option value="INACTIVE"
              ${(not empty voucher && voucher.status eq 'INACTIVE') ? 'selected' : ''}>Tạm dừng</option>
              <option value="EXPIRED"
              ${(not empty voucher && (voucher.status eq 'EXPIRED' || voucher.status eq 'USED_UP' || voucher.status eq 'PENDING')) ? 'selected' : ''}>Hết hạn</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <label>Mô tả</label>
          <textarea name="description" rows="2"
                    placeholder="Mô tả ngắn về mã giảm giá...">${voucher.description}</textarea>
        </div>

        <div class="form-grid">
          <div class="form-row">
            <label>Loại giảm giá <span style="color:red">*</span></label>
            <select name="discountType" id="discountType" required>
              <option value="PERCENT" ${empty voucher || voucher.discountType eq 'PERCENT' ? 'selected' : ''}>
                Phần trăm (%)
              </option>
              <option value="FIXED" ${voucher.discountType eq 'FIXED' ? 'selected' : ''}>
                Số tiền cố định (₫)
              </option>
            </select>
          </div>

          <div class="form-row">
            <label>Mức giảm giá <span style="color:red">*</span></label>
            <div class="combo-with-custom">
              <select id="discountValuePreset">
                <%-- Các option sẽ được JS sinh động dựa theo loại giảm giá --%>
              </select>
              <div class="custom-input-wrap" id="discountValueCustomWrap">
                <div class="discount-input-group">
                  <button type="button" class="discount-btn" onclick="stepValue('discountValue', -1)">−</button>
                  <input type="number" id="discountValue" name="discountValue"
                         min="0.01" step="any"
                         data-initial-value="${voucher.discountValue}"
                         value="${voucher.discountValue}"
                         placeholder="Nhập mức giảm khác"/>
                  <button type="button" class="discount-btn" onclick="stepValue('discountValue', 1)">+</button>
                </div>
                <small id="discountValueWords" class="hint amount-words"></small>
              </div>
            </div>
            <small class="hint">Phần trăm: chọn mức 5–50% hoặc nhập mức khác (≤100%). Cố định: chọn 50.000₫–500.000₫ hoặc nhập mức khác.</small>
          </div>
        </div>

        <div class="form-grid">
          <div class="form-row">
            <label>Đơn hàng tối thiểu (₫)</label>
            <div class="discount-input-group">
              <button type="button" class="discount-btn" onclick="stepValue('minOrderValue', -1)">−</button>
              <input type="number" id="minOrderValue" name="minOrderValue" min="0" step="1000"
                     value="${voucher.minOrderValue}"
                     placeholder="0 = không yêu cầu tối thiểu"/>
              <button type="button" class="discount-btn" onclick="stepValue('minOrderValue', 1)">+</button>
            </div>
            <small id="minOrderValueWords" class="hint amount-words"></small>
          </div>

          <div class="form-row">
            <label>Giảm tối đa (₫) <small class="hint">(chỉ áp dụng cho loại %)</small></label>
            <div class="discount-input-group">
              <button type="button" class="discount-btn" onclick="stepValue('maxDiscount', -1)">−</button>
              <input type="number" id="maxDiscount" name="maxDiscount" min="0" step="1000"
                     value="${voucher.maxDiscount}"
                     placeholder="0 = không giới hạn"/>
              <button type="button" class="discount-btn" onclick="stepValue('maxDiscount', 1)">+</button>
            </div>
            <small id="maxDiscountWords" class="hint amount-words"></small>
          </div>
        </div>
      </div>

      <%-- ── THỜI GIAN ──────────────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-calendar-days"></i> Thời gian áp dụng</h4>

        <div class="form-grid">
          <div class="form-row">
            <label>Thời điểm bắt đầu</label>
            <input type="datetime-local" name="startedAt" id="startedAt"
                   value="${voucher.startedAtForInput}"/>
            <small class="hint">Để trống nếu áp dụng ngay khi tạo.</small>
          </div>

          <div class="form-row">
            <label>Thời điểm kết thúc <span style="color:red">*</span></label>
            <input type="datetime-local" name="expiredAt" id="expiredAt" required
                   value="${voucher.expiredAtForInput}"/>
          </div>
        </div>
      </div>

      <%-- ── GIỚI HẠN SỬ DỤNG ───────────────────────────────────────────── --%>
      <div class="form-section">
        <h4><i class="fa-solid fa-users"></i> Giới hạn sử dụng</h4>

        <div class="form-grid">
          <div class="form-row">
            <label>Số lần sử dụng / 1 người dùng</label>
            <div class="combo-with-custom">
              <select id="maxUsesPreset">
                <option value="unlimited">Vô hạn</option>
                <c:forEach var="i" begin="1" end="10">
                  <option value="${i}">${i} lần</option>
                </c:forEach>
                <option value="custom">Mức khác...</option>
              </select>
              <div class="custom-input-wrap" id="maxUsesCustomWrap">
                <div class="discount-input-group">
                  <button type="button" class="discount-btn" onclick="stepValue('maxUsesPerUser', -1)">−</button>
                  <input type="text" id="maxUsesPerUser" name="maxUsesPerUser"
                         inputmode="numeric" pattern="[0-9]*" placeholder="Nhập số lần khác"/>
                  <button type="button" class="discount-btn" onclick="stepValue('maxUsesPerUser', 1)">+</button>
                </div>
              </div>
            </div>
          </div>

          <div class="form-row">
            <label>Số lượng sử dụng tối đa (toàn hệ thống)</label>
            <div class="combo-with-custom">
              <select id="quantityPreset">
                <option value="unlimited">Vô hạn</option>
                <c:forEach var="i" begin="10" end="100" step="10">
                  <option value="${i}">${i} lượt</option>
                </c:forEach>
                <option value="custom">Mức khác...</option>
              </select>
              <div class="custom-input-wrap" id="quantityCustomWrap">
                <div class="discount-input-group">
                  <button type="button" class="discount-btn" onclick="stepValue('quantity', -1)">−</button>
                  <input type="text" id="quantity" name="quantity"
                         inputmode="numeric" pattern="[0-9]*" placeholder="Nhập số lượng khác"/>
                  <button type="button" class="discount-btn" onclick="stepValue('quantity', 1)">+</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <%-- ── ACTIONS ─────────────────────────────────────────────────────── --%>
      <div style="display:flex; gap:1rem; margin-top:.5rem;">
        <button type="submit" class="btn btn-primary">
          <i class="fa-solid fa-floppy-disk"></i>
          <c:choose>
            <c:when test="${empty voucher}">Tạo mã giảm giá</c:when>
            <c:otherwise>Lưu thay đổi</c:otherwise>
          </c:choose>
        </button>
        <a href="${pageContext.request.contextPath}/admin-voucher" class="btn btn-secondary">
          <i class="fa-solid fa-arrow-left"></i> Quay lại
        </a>
      </div>

    </form>
  </section>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-theme.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-voucher-form.js"></script>

<%-- ── Dữ liệu ban đầu cho các combo khi sửa mã giảm giá ── --%>
<c:if test="${not empty voucher}">
  <script>
    document.addEventListener('DOMContentLoaded', function () {
      initEditCombos({
        maxUsesPerUser: <c:choose><c:when test="${empty voucher.maxUsesPerUser}">null</c:when><c:otherwise>${voucher.maxUsesPerUser}</c:otherwise></c:choose>,
        quantity: ${voucher.quantity}
      });
    });
  </script>
</c:if>

</body>
</html>

