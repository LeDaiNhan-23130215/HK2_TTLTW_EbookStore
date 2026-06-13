<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Sửa bài viết</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-news.css" />
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

    <script>
        $(document).ready(function () {
            // Hiển thị preview ảnh khi sửa
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
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news" class="active">Tin tức</a>
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
        <div class="topbar-title">Chỉnh sửa bài viết</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form Edit -->
    <section class="dashboard">
        <div class="add-form">
            <h2 class="toggle-title"><i class="fa-solid fa-pen-to-square"></i> Sửa bài viết</h2>

            <form action="${pageContext.request.contextPath}/admin-news" method="post">

                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${news.id}">

                <div class="form-row">
                    <label>Tiêu đề:</label>
                    <input type="text" name="title" value="${news.title}" required>
                </div>

                <div class="form-row">
                    <label>Ảnh minh họa (URL):</label>
                    <input type="text" id="imageUrl" name="imgURL" value="${news.imgURL}">
                </div>

                <div class="banner-preview">
                    <img id="previewImg"
                         src="${news.imgURL}"
                         style="width:300px; border-radius:6px; margin-top:10px;">
                </div>

                <div class="form-row">
                    <label>Tác giả:</label>
                    <input type="text" name="author" value="${news.author}">
                </div>

                <div class="form-row">
                    <label>Nội dung:</label>
                    <textarea name="content" rows="4">${news.content}</textarea>
                </div>

                <div class="form-row">
                    <label>Trạng thái:</label>
                    <select name="status">
                        <option value="1" ${news.status == 1 ? "selected" : ""}>Hiển thị</option>
                        <option value="0" ${news.status == 0 ? "selected" : ""}>Ẩn</option>
                    </select>
                </div>

                <button type="submit" class="btn-add" style="background:#28a745;">
                    Lưu thay đổi
                </button>

                <a href="${pageContext.request.contextPath}/admin-news"
                   class="btn btn-secondary" style="margin-left:10px;">
                    Quay lại
                </a>

            </form>
        </div>
    </section>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>

</body>
</html>
