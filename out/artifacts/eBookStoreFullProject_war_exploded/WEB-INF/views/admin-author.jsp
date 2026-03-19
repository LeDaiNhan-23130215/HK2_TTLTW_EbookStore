<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Author Manager</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-author.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <!-- Bootstrap + DataTable -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#authorTable').DataTable({
                pageLength: 5,
                lengthMenu: [5, 10, 20, 50],
                ordering: true,
                searching: true,
                language: {
                    lengthMenu: "Hiển thị _MENU_ dòng",
                    search: "Tìm kiếm:",
                    info: "Trang _PAGE_ / _PAGES_",
                    paginate: {
                        first: "Đầu",
                        last: "Cuối",
                        next: "Tiếp",
                        previous: "Trước"
                    },
                    zeroRecords: "Không tìm thấy dữ liệu"
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
        <a href="${pageContext.request.contextPath}/admin-author" class="active">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý tác giả</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form -->
    <section class="dashboard">
        <div class="add-form">
            <h2><i class="fa-solid fa-plus"></i> Thêm tác giả</h2>

            <form action="${pageContext.request.contextPath}/admin-author"
                  method="post"
                  enctype="multipart/form-data">

                <input type="hidden" name="action" value="add">

                <!-- Mode -->
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
                        <label>Tên tác giả:</label>
                        <input type="text" name="authorName">
                    </div>

                    <div class="form-row">
                        <label>Chi tiết:</label>
                        <textarea name="authorDetail"></textarea>
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

    <!-- Table -->
    <section class="table-section">
        <table id="authorTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên tác giả</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="a" items="${authors}">
                <tr>
                    <td>${a.id}</td>
                    <td>${a.authorName}</td>
                    <td>
                        <a class="btn-Edit"
                           href="${pageContext.request.contextPath}/admin-author?action=edit&id=${a.id}">
                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                        </a>

                        <a class="btn-Del"
                           onclick="return confirm('Bạn có chắc muốn xóa tác giả này?')"
                           href="${pageContext.request.contextPath}/admin-author?action=delete&id=${a.id}">
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