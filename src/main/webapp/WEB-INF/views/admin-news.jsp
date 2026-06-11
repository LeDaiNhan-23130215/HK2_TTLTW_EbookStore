<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Admin - Quản lý Tin tức</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-news.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

    <!-- DataTables -->
    <link rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#newsTable').DataTable({
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

            // Preview ảnh khi nhập URL
            $("#imageUrl").on("input", function () {
                const url = $(this).val();
                if (url.trim().length > 5) {
                    $("#previewImg").attr("src", url).show();
                } else {
                    $("#previewImg").hide();
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
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news" class="active">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-logs">
            System Logs
        </a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>


<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý tin tức</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form thêm tin tức -->
    <section class="dashboard">
        <div class="add-form">
            <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm bài viết mới</h2>

            <form action="${pageContext.request.contextPath}/admin-news"
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
                        <label>Tiêu đề:</label>
                        <input type="text" name="title">
                    </div>

                    <div class="form-row">
                        <label>Ảnh minh họa (URL):</label>
                        <input type="text" id="imageUrl" name="imgURL"
                               placeholder="Nhập đường dẫn ảnh...">
                    </div>

                    <div class="banner-preview">
                        <img id="previewImg"
                             src=""
                             style="display:none; width:300px; border-radius:6px;">
                    </div>

                    <div class="form-row">
                        <label>Tác giả:</label>
                        <input type="text" name="author">
                    </div>

                    <div class="form-row">
                        <label>Nội dung:</label>
                        <textarea name="content" rows="4"></textarea>
                    </div>

                    <div class="form-row">
                        <label>Trạng thái:</label>
                        <select name="status">
                            <option value="1">Hiển thị</option>
                            <option value="0">Ẩn</option>
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
                        <b>title,imgURL,author,content,status</b>
                    </small>

                </div>

                <button type="submit" class="btn-add">
                    Thực hiện
                </button>
            </form>
        </div>
    </section>

    <!-- Bảng danh sách tin -->
    <section class="table-section">
        <table id="newsTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tiêu đề</th>
                <th>Ngày đăng</th>
                <th>Trạng thái</th>
                <th>Ảnh minh họa</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="n" items="${newsList}">
                <tr>
                    <td>${n.id}</td>
                    <td>${n.title}</td>
                    <td>${n.createdAt}</td>
                    <td>
                        <c:choose>
                            <c:when test="${n.status == 1}">
                                <span class="badge bg-success">Hiển thị</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary">Ẩn</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td>
                        <img src="${n.imgURL}" style="width:80px; border-radius:6px;">
                    </td>

                    <td>
                        <a href="${pageContext.request.contextPath}/admin-news?action=edit&id=${n.id}"
                           class="btn btn-warning btn-sm">
                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                        </a>

                        <a href="${pageContext.request.contextPath}/admin-news?action=delete&id=${n.id}"
                           class="btn btn-danger btn-sm"
                           onclick="return confirm('Bạn chắc chắn muốn xóa?');">
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
