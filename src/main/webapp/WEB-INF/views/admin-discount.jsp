<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Admin – Chương trình giảm giá</title>

  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-delete-modal.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

  <!-- Bootstrap + DataTables -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
  <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
  <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
  <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

  <script>
    $(document).ready(function () {
      $('#discountTable').DataTable({
        pageLength: 10,
        lengthMenu: [5, 10, 20, 50],
        ordering:  true,
        searching: true,
        language: {
          lengthMenu: "Hiển thị _MENU_ dòng",
          search:     "Tìm kiếm:",
          info:       "Trang _PAGE_ / _PAGES_",
          paginate:   { first: "Đầu", last: "Cuối", next: "Tiếp", previous: "Trước" },
          zeroRecords: "Không có chương trình giảm giá nào"
        }
      });
    });
  </script>

  <style>
    .badge-status          { padding:.3em .7em; border-radius:99px; font-size:.8rem; font-weight:600; }
    .badge-status.active   { background:#d1fae5; color:#065f46; }
    .badge-status.inactive { background:#fef3c7; color:#92400e; }
    .badge-status.ended    { background:#fee2e2; color:#991b1b; }
  </style>
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
    <a href="${pageContext.request.contextPath}/logout" class="logout">Đăng xuất</a>  </nav>
</aside>

<!-- Main -->
<div class="main-content">
  <header class="topbar">
    <div class="topbar-title">Quản lý chương trình giảm giá</div>
    <button id="toggle-theme">🌙 Dark Mode</button>
  </header>

  <section class="dashboard">
    <div class="add-form" style="margin-bottom:1.5rem">
      <a href="${pageContext.request.contextPath}/admin-discount?action=new"
         class="btn btn-primary">
        <i class="fa-solid fa-plus"></i> Thêm chương trình mới
      </a>
    </div>

    <div class="table-section">
      <h3>Danh sách chương trình giảm giá</h3>
      <table id="discountTable" class="table table-bordered table-hover">
        <thead class="table-dark">
        <tr>
          <th>#</th>
          <th>Tên chương trình</th>
          <th>Loại giảm</th>
          <th>Mức giảm</th>
          <th>Bắt đầu</th>
          <th>Kết thúc</th>
          <th>Trạng thái</th>
          <th>Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="d" items="${discounts}" varStatus="st">
          <tr>
            <td>${st.index + 1}</td>
            <td><strong>${d.name}</strong>
              <c:if test="${not empty d.description}">
                <br><small class="text-muted">${d.description}</small>
              </c:if>
            </td>
            <td>
              <c:choose>
                <c:when test="${d.discountType eq 'PERCENT'}">Phần trăm (%)</c:when>
                <c:otherwise>Cố định (₫)</c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:choose>
                <c:when test="${d.discountType eq 'PERCENT'}">
                  <strong>${d.discountValue}%</strong>
                </c:when>
                <c:otherwise>
                  <strong><fmt:formatNumber value="${d.discountValue}"
                                            type="number"
                                            groupingUsed="true"/> ₫</strong>
                </c:otherwise>
              </c:choose>
            </td>
            <td>${d.formattedStartDate}</td>
            <td>${d.formattedEndDate}</td>
            <td>
              <c:choose>
                <c:when test="${d.status eq 'ACTIVE'}">
                  <span class="badge-status active">Hoạt động</span>
                </c:when>
                <c:when test="${d.status eq 'INACTIVE'}">
                  <span class="badge-status inactive">Tạm dừng</span>
                </c:when>
                <c:otherwise>
                  <span class="badge-status ended">Kết thúc</span>
                </c:otherwise>
              </c:choose>
            </td>
            <td>
              <a href="${pageContext.request.contextPath}/admin-discount?action=edit&id=${d.id}"
                 class="btn btn-sm btn-warning">
                <i class="fa-solid fa-pen"></i> Sửa
              </a>
              <a href="${pageContext.request.contextPath}/admin-discount?action=delete&id=${d.id}"
                 class="btn btn-sm btn-danger admin-delete-trigger"
                 data-title="Xoá chương trình giảm giá?"
                 data-desc="Bạn có chắc muốn xoá chương trình '${d.name}'? Hành động này không thể hoàn tác.">
                <i class="fa-solid fa-trash"></i> Xóa
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </section>
</div>

<!-- Delete Confirmation Modal -->
<div id="adminDeleteModal" class="ui-modal-overlay" style="display:none;" aria-modal="true" role="dialog">
  <div class="ui-modal-box">
    <div class="ui-modal-icon"><i class="fa-solid fa-trash-can"></i></div>
    <h3 class="ui-modal-title" id="adminDeleteModalTitle">Xác nhận xoá</h3>
    <p class="ui-modal-desc" id="adminDeleteModalDesc"></p>
    <div class="ui-modal-actions">
      <button type="button" class="ui-modal-cancel" id="adminDeleteModalCancel">Quay lại</button>
      <button type="button" class="ui-modal-confirm" id="adminDeleteModalConfirm">
        <i class="fa-solid fa-trash-can"></i> Xoá
      </button>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-delete-modal.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-theme.js"></script>
</body>
<jsp:include page="/WEB-INF/views/admin-header-fragment.jsp"/>
</html>
