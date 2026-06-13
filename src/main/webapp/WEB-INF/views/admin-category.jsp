<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Category Manager</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-delete-modal.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#activityTable').DataTable({
                "pageLength": 5,
                "lengthMenu": [5, 10, 20, 50],
                "ordering": true,
                "searching": true,
                "language": {
                    "lengthMenu": "Hiển thị _MENU_ dòng",
                    "search": "Tìm kiếm:",
                    "info": "Trang _PAGE_ / _PAGES_",
                    "paginate": { "first": "Đầu", "last": "Cuối", "next": "Tiếp", "previous": "Trước" },
                    "zeroRecords": "Không tìm thấy dữ liệu"
                }
            });
        });
    </script>
</head>
<body>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category" class="active">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-voucher">Mã giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-logs">
            System Logs
        </a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main content -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý danh mục</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form thêm -->
    <section class="dashboard">
        <div class="add-form">

            <h2><i class="fa-solid fa-plus"></i> Thêm danh mục</h2>

            <form action="${pageContext.request.contextPath}/admin-category"
                  method="post"
                  enctype="multipart/form-data">

                <input type="hidden" name="action" value="add">

                <!-- Chọn mode -->
                <div class="mode-select">
                    <label>
                        <input type="radio" name="mode" value="manual" checked>
                        Nhập thủ công
                    </label>

                    <label>
                        <input type="radio" name="mode" value="import">
                        Import từ file
                    </label>
                </div>

                <!-- Manual -->
                <div id="manualForm">

                    <div class="form-row">
                        <label>Tên danh mục:</label>
                        <input type="text" name="categoryName">
                    </div>

                    <div class="form-row">
                        <label>Mô tả:</label>
                        <textarea name="description"></textarea>
                    </div>

                    <div class="form-row">
                        <label>Icon:</label>

                        <div class="icon-select-wrapper">
                            <select name="icon" id="iconSelect" class="icon-select">
                                <c:forEach var="ic" items="${iconList}">
                                    <option value="${ic}"
                                            <c:if test="${category != null && category.icon == ic}">selected</c:if>
                                            <c:if test="${category == null && ic == 'fa-solid fa-folder'}">selected</c:if>>
                                            ${ic}
                                    </option>
                                </c:forEach>
                            </select>

                            <!-- preview -->
                            <span class="icon-preview">
                                <i class="${category != null ? category.icon : 'fa-solid fa-folder'}"></i>
                            </span>
                        </div>

                    </div>
                </div>

                <!-- Import -->
                <div id="importForm" style="display:none">

                    <div class="form-row">
                        <label>File CSV / Excel:</label>
                        <input type="file" name="file" accept=".csv,.xlsx">
                    </div>

                </div>

                <button type="submit" class="btn-addCategory">
                    Thực hiện
                </button>
            </form>
        </div>
    </section>

    <!-- Danh sách danh mục -->
    <section class="table-section">
        <table id="activityTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th>Mô tả</th>
                <th>Icon</th>
                <th>Mã danh mục</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="c" items="${categories}">
                <tr>
                    <td>${c.id}</td>
                    <td>${c.name}</td>
                    <td>${c.description}</td>
                    <td><i class="${c.icon}"></i></td>
                    <td>${c.categoryCode}</td>
                    <td>
                        <a class="btn-Edit"
                           href="${pageContext.request.contextPath}/admin-category?action=edit&id=${c.id}">
                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                        </a>

                        <a class="btn-Del admin-delete-trigger"
                           href="${pageContext.request.contextPath}/admin-category?action=delete&id=${c.id}"
                           data-title="Xoá danh mục?"
                           data-desc="Bạn có chắc muốn xoá danh mục '${c.categoryCode}' này? Hành động này không thể hoàn tác.">
                            <i class="fa-solid fa-trash"></i> Xóa
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </table>
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
<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>
<script>
    const iconSelect = document.getElementById("iconSelect");
    const previewIcon = document.querySelector(".icon-preview i");

    if (iconSelect) {
        iconSelect.addEventListener("change", function () {
            previewIcon.className = this.value;
        });
    }
</script>
</body>
</html>
