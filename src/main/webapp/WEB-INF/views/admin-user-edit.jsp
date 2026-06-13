<%@ page import="models.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh sửa người dùng</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-user.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-confirm.css"/>
</head>

<body>

<%
    User u = (User) request.getAttribute("user");
    if (u == null) {
%>
<h2 style="color: red; text-align: center; margin-top: 30px;">Không tìm thấy người dùng!</h2>
<%
        return;
    }
%>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user" class="active">Người dùng</a>
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
        <a href="${pageContext.request.contextPath}/logout" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main Content -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Chỉnh sửa người dùng</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <section class="dashboard">

        <div class="add-form">
            <h2 class="toggle-title"><i class="fa-solid fa-pen-to-square"></i> Sửa người dùng</h2>

            <form action="${pageContext.request.contextPath}/admin-user" method="post">

                <input type="hidden" name="id" value="<%= u.getId() %>">
                <input type="hidden" name="action" value="update">

                <div class="form-row">
                    <label for="userName">Tên người dùng:</label>
                    <input type="text" id="userName" name="userName"
                           value="<%= u.getUsername() %>" required>
                </div>

                <div class="form-row">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email"
                           value="<%= u.getEmail() %>" required>
                </div>

                <div class="form-row">
                    <label for="phoneNum">Số điện thoại:</label>
                    <input type="text" id="phoneNum" name="phoneNum"
                           value="<%= u.getPhoneNum() %>" required>
                </div>

                <div class="form-row">
                    <label for="role">Vai trò:</label>
                    <select name="role" id="role" required>
                        <option value="user" <%= "user".equals(u.getRole()) ? "selected" : "" %>>Người dùng</option>
                        <option value="admin" <%= "admin".equals(u.getRole()) ? "selected" : "" %>>Admin</option>
                    </select>
                </div>

                <button type="submit" class="btn-addUser" style="background: #28a745;">
                    Lưu thay đổi
                </button>

                <a href="${pageContext.request.contextPath}/admin-user" class="btn btn-secondary" style="margin-left: 10px;">
                    Quay lại
                </a>

            </form>
        </div>
    </section>
</div>

<!-- Delete Confirmation Modal -->
<div id="confirmModalOverlay" class="confirm-modal-overlay">
    <div class="confirm-modal-box">
        <div class="confirm-modal-icon"><i class="fa-solid fa-triangle-exclamation"></i></div>
        <p class="confirm-modal-title">Xác nhận xóa</p>
        <p id="confirmModalMessage" class="confirm-modal-message"></p>
        <div class="confirm-modal-actions">
            <button type="button" class="confirm-modal-cancel" onclick="closeConfirmModal()">Hủy</button>
            <button type="button" id="confirmModalConfirmBtn" class="confirm-modal-confirm">Xóa</button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-confirm.js"></script>

<script src="assets/js/admin-darkmode.js"></script>
<script src="assets/js/showForm.js"></script>
</body>
</html>
