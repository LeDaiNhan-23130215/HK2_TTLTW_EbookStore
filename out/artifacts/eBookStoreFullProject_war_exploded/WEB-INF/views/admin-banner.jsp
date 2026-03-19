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
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
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
                        <input name="url" type="text">
                    </div>

                    <div class="form-row">
                        <label>Vị trí:</label>
                        <select name="position">
                            <option value="">--Chọn vị trí--</option>
                            <option>Home-1</option>
                            <option>Home-2</option>
                            <option>Home-3</option>
                            <option>Home-4</option>
                            <option>Home-5</option>
                            <option>Home-6</option>
                            <option>Home-7</option>
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

                        <a class="btn btn-danger btn-sm"
                           href="${pageContext.request.contextPath}/admin-banner?action=delete&id=${b.id}"
                           onclick="return confirm('Bạn có chắc muốn xóa banner này?');">
                            <i class="fa-solid fa-trash"></i> Xóa
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>
</body>
</html>