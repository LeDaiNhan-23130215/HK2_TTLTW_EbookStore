<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa tác giả</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-category.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png" />
</head>

<body>

<c:if test="${empty author}">
    <h2 style="color:red;text-align:center;margin-top:40px;">Không tìm thấy tác giả!</h2>
    <c:redirect url="/admin-author"/>
</c:if>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-author" class="active">Tác giả</a>
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

<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Chỉnh sửa tác giả</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <section class="dashboard">

        <div class="add-form">
            <h2 class="toggle-title">
                <i class="fa-solid fa-pen-to-square"></i> Sửa tác giả
            </h2>

            <form action="${pageContext.request.contextPath}/admin-author" method="post">

                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${author.id}">

                <div class="form-row">
                    <label>Tên tác giả:</label>
                    <input type="text" name="authorName" value="${author.authorName}" required>
                </div>

                <div class="form-row">
                    <label>Tiểu sử:</label>
                    <textarea name="authorDetail" rows="4">${author.authorDetail}</textarea>
                </div>

                <button type="submit" class="btn-addCategory" style="background:#28a745;">
                    Lưu thay đổi
                </button>

                <a href="${pageContext.request.contextPath}/admin-author"
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