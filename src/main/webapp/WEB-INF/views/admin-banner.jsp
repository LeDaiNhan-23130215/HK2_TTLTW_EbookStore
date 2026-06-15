<%@ page import="models.Banner" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Banner Manager</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-banner.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-delete-modal.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png" />

    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

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
                    "paginate": {
                        "first": "Đầu",
                        "last": "Cuối",
                        "next": "Tiếp",
                        "previous": "Trước"
                    },
                    "zeroRecords": "Không tìm thấy dữ liệu"
                }
            });
        });
    </script>
</head>

<body>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo">
        <h2>Ebook Admin</h2>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner" class="active">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-voucher">Mã giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-logs">
            System Logs
        </a>
        <hr>
        <a href="${pageContext.request.contextPath}/logout" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main content -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý banner</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form thêm banner -->
    <section class="dashboard">
        <div class="add-form">
            <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm banner mới</h2>

            <form action="${pageContext.request.contextPath}/admin-banner"
                  method="post"
                  enctype="multipart/form-data">

                <input type="hidden" name="action" value="add">

                <!-- MODE SELECT -->
                <div class="mode-select">
                    <label>
                        <input type="radio" name="mode" value="manual" checked>
                        Nhập thủ công
                    </label>
                    <label>
                        <input type="radio" name="mode" value="import">
                        Import từ CSV
                    </label>
                </div>

                <!-- MANUAL FORM -->
                <div id="manualForm">

                    <div class="form-row">
                        <label>Đường dẫn (URL):</label>
                        <input id="url" name="url" type="text">
                    </div>

                    <div class="form-row">
                        <label>Xem trước:</label>

                        <div class="banner-preview">
                            <img id="banner-preview-img"
                                 src=""
                                 alt="Banner preview"
                                 style="display:none; max-width:100%; max-height:200px; border:1px solid #ddd; padding:5px;">
                        </div>
                    </div>

                    <div class="form-row">
                        <label>Vị trí:</label>
                        <select name="position">
                            <option value="">--Chọn vị trí--</option>
                            <option>HomeTop</option>
                            <option>HomeMiddleLeft</option>
                            <option>HomeMiddleRight</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label>Ngày bắt đầu:</label>
                        <input name="startDate" type="date">
                    </div>

                    <div class="form-row">
                        <label>Ngày kết thúc:</label>
                        <input name="endDate" type="date">
                    </div>

                    <div class="form-row">
                        <label>Kích hoạt:</label>
                        <select name="isActive">
                            <option value="">--Chọn trạng thái--</option>
                            <option value="1">Hoạt động</option>
                            <option value="0">Không hoạt động</option>
                        </select>
                    </div>

                </div>

                <!-- IMPORT FORM -->
                <div id="importForm" style="display:none">

                    <div class="form-row">
                        <label>File CSV:</label>
                        <input type="file" name="file" accept=".csv">
                    </div>

                    <small>
                        CSV gồm các cột:<br>
                        <b>url,position,startDate,endDate,isActive</b>
                    </small>
                </div>

                <button type="submit" class="btn-addBanner">
                    Thực hiện
                </button>
            </form>
        </div>
    </section>

    <!-- Danh sách banner -->
    <section class="table-section">
        <table id="activityTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Hình banner</th>
                <th>Vị trí</th>
                <th>Ngày bắt đầu</th>
                <th>Ngày kết thúc</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody id="bannerTableBody">
            <c:forEach var="b" items="${banners}">
                <tr>
                    <td>${b.id}</td>
                    <td><img src="${b.url}" style="width:80px; height:auto;" alt=""></td>
                    <td>${b.position}</td>
                    <td>${b.startDate}</td>
                    <td>${b.endDate}</td>
                    <td>
                        <c:choose>
                            <c:when test="${b.isActive == 1}">
                                <span class="badge bg-success">Hoạt động</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Không hoạt động</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>
                        <a class="btn btn-warning btn-sm"
                           href="${pageContext.request.contextPath}/admin-banner?action=edit&id=${b.id}">
                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                        </a>

                        <a class="btn btn-danger btn-sm admin-delete-trigger"
                           href="${pageContext.request.contextPath}/admin-banner?action=delete&id=${b.id}"
                           data-title="Xoá banner?"
                           data-desc="Bạn có chắc muốn xoá banner này? Hành động này không thể hoàn tác.">
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
<script src="${pageContext.request.contextPath}/assets/js/admin-demoIMG.js"></script>
</body>
<jsp:include page="/WEB-INF/views/admin-header-fragment.jsp"/>
</html>