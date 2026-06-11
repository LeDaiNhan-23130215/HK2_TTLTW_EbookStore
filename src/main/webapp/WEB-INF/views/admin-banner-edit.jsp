<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa Banner</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-banner.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>

<body>

<c:if test="${banner == null}">
    <h2 style="text-align:center; color:red; margin-top:30px;">Không tìm thấy banner!</h2>
</c:if>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo">
        <h2>Ebook Admin</h2>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner" class="active">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
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

<!-- Main Content -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Chỉnh sửa Banner</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <section class="dashboard">
        <div class="add-form">
            <h2 class="toggle-title">
                <i class="fa-solid fa-pen-to-square"></i> Sửa Banner
            </h2>

            <form action="${pageContext.request.contextPath}/admin-banner" method="post">

                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${banner.id}">

                <!-- URL -->
                <div class="form-row">
                    <label for="url">Đường dẫn (URL):</label>
                    <input type="text" id="url" name="url" value="${banner.url}" required>
                </div>

                <!-- Position -->
                <div class="form-row">
                    <label for="position">Vị trí:</label>
                    <select name="position" id="position" required>
                        <option value="">--Chọn vị trí--</option>
                        <option value="HomeTop" ${banner.position == 'HomeTop' ? 'selected' : ''}>HomeTop</option>
                        <option value="HomeMiddleLeft" ${banner.position == 'HomeMiddleLeft' ? 'selected' : ''}>HomeMiddleLeft</option>
                        <option value="HomeMiddleRight" ${banner.position == 'HomeMiddleRight' ? 'selected' : ''}>HomeMiddleRight</option>
                    </select>
                </div>

                <!-- Start date -->
                <div class="form-row">
                    <label for="sDate">Ngày bắt đầu:</label>
                    <input type="date" id="sDate" name="startDate" value="${banner.startDate}" required>
                </div>

                <!-- End date -->
                <div class="form-row">
                    <label for="eDate">Ngày kết thúc:</label>
                    <input type="date" id="eDate" name="endDate" value="${banner.endDate}" required>
                </div>

                <!-- isActive -->
                <div class="form-row">
                    <label for="isActive">Trạng thái:</label>
                    <select name="isActive" id="isActive" required>
                        <option value="1" ${banner.isActive == 1 ? 'selected' : ''}>Hoạt động</option>
                        <option value="0" ${banner.isActive == 0 ? 'selected' : ''}>Không hoạt động</option>
                    </select>
                </div>

                <button type="submit" class="btn-addBanner" style="background:#28a745;">Lưu thay đổi</button>

                <a href="${pageContext.request.contextPath}/admin-banner"
                   class="btn btn-secondary" style="margin-left:10px;">Quay lại</a>

            </form>
        </div>
    </section>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>
</body>
</html>
