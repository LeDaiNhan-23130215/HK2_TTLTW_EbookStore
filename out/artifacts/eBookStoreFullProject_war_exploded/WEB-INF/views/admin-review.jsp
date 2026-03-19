<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý Review</title>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin-review.css"/>
    <link rel="icon"
          href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <!-- JS -->
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#reviewTable').DataTable({
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
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review" class="active">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý Review</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Table -->
    <section class="table-section">
        <table id="reviewTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Người dùng</th>
                <th>Ebook</th>
                <th>Đánh giá</th>
                <th>Bình luận</th>
                <th>Ngày</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="r" items="${reviews}">
                <tr>
                    <td>${r.id}</td>
                    <td>${userMap[r.userId]}</td>
                    <td>${ebookMap[r.ebookId]}</td>

                    <td>
                        <c:forEach begin="1" end="${r.rating}">
                            ⭐
                        </c:forEach>
                    </td>

                    <td>${r.comment}</td>
                    <td>${r.createdAt}</td>

                    <td>
                        <a class="btn-Del"
                           onclick="return confirm('Xóa review này?')"
                           href="${pageContext.request.contextPath}/admin-review?action=delete&id=${r.id}">
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
</body>
</html>