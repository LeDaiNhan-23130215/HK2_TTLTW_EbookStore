<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin – Mã giảm giá</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-voucher.css"/>
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
            $('#voucherTable').DataTable({
                pageLength: 10,
                lengthMenu: [5, 10, 20, 50],
                ordering:  true,
                searching: true,
                language: {
                    lengthMenu: "Hiển thị _MENU_ dòng",
                    search:     "Tìm kiếm:",
                    info:       "Trang _PAGE_ / _PAGES_",
                    paginate:   { first: "Đầu", last: "Cuối", next: "Tiếp", previous: "Trước" },
                    zeroRecords: "Không có mã giảm giá nào"
                }
            });
        });
    </script>
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
        <a href="${pageContext.request.contextPath}/logout" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main -->
<div class="main-content">
    <header class="topbar">
        <div class="topbar-title">Quản lý mã giảm giá (Voucher)</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <section class="dashboard">
        <div class="add-form" style="margin-bottom:1.5rem">
            <a href="${pageContext.request.contextPath}/admin-voucher?action=new"
               class="btn btn-primary">
                <i class="fa-solid fa-plus"></i> Tạo mã giảm giá mới
            </a>
        </div>

        <div class="table-section">
            <h3>Danh sách mã giảm giá</h3>
            <table id="voucherTable" class="table table-bordered table-hover">
                <thead class="table-dark">
                <tr>
                    <th>#</th>
                    <th>Mã</th>
                    <th>Mức giảm</th>
                    <th>Đã dùng/Tối đa</th>
                    <th>Bắt đầu</th>
                    <th>Kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="v" items="${vouchers}" varStatus="st">
                    <tr>
                        <td>${st.index + 1}</td>
                        <td>
                            <span class="code-pill">${v.code}</span>
                            <c:if test="${not empty v.description}">
                                <br><small class="text-muted">${v.description}</small>
                            </c:if>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${v.discountType eq 'PERCENT'}">
                                    <strong>${v.discountValue}%</strong>
                                    <c:if test="${v.maxDiscount > 0}">
                                        <br><small class="text-muted">Tối đa <fmt:formatNumber value="${v.maxDiscount}" type="number" groupingUsed="true"/> ₫</small>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <strong><fmt:formatNumber value="${v.discountValue}" type="number" groupingUsed="true"/> ₫</strong>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                                ${v.usedCount} /
                            <c:choose>
                                <c:when test="${v.quantity == -1}">Vô hạn</c:when>
                                <c:otherwise>${v.quantity}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${v.formattedStartedAt}</td>
                        <td>${v.formattedExpiredAt}</td>
                        <td>
                            <c:choose>
                                <c:when test="${v.status eq 'ACTIVE'}">
                                    <span class="badge-status active">Hoạt động</span>
                                </c:when>
                                <c:when test="${v.status eq 'INACTIVE'}">
                                    <span class="badge-status inactive">Tạm ngưng</span>
                                </c:when>
                                <c:when test="${v.status eq 'PENDING'}">
                                    <span class="badge-status pending">Chưa bắt đầu</span>
                                </c:when>
                                <c:when test="${v.status eq 'USED_UP'}">
                                    <span class="badge-status usedup">Hết lượt</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-status expired">Hết hạn</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-nowrap">
                            <a href="${pageContext.request.contextPath}/admin-voucher?action=edit&id=${v.id}"
                               class="btn btn-sm btn-warning">
                                <i class="fa-solid fa-pen"></i> Sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/admin-voucher?action=delete&id=${v.id}"
                               class="btn btn-sm btn-danger admin-delete-trigger"
                               data-title="Xoá mã giảm giá?"
                               data-desc="Bạn có chắc muốn xoá mã giảm giá '${v.code}'? Hành động này không thể hoàn tác.">
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
<script src="${pageContext.request.contextPath}/assets/js/admin-voucher-form.js"></script>
</body>
<jsp:include page="/WEB-INF/views/admin-header-fragment.jsp"/>
</html>