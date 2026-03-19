<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Chi tiết Feedback</title>

    <!-- CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-feedback.css" />
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

    <style>
        .feedback-box {
            border: 1px solid #ddd;
            padding: 20px;
            border-radius: 6px;
            background-color: #f9f9f9;
            white-space: pre-wrap;
        }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback" class="active">Feedback</a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>

<div class="main-content">
    <header class="topbar">
        <div class="topbar-title">Chi tiết Feedback</div>
    </header>

    <section class="container mt-4">

        <!-- Nếu feedback không tồn tại -->
        <c:if test="${feedback == null}">
            <div class="alert alert-warning">
                Feedback không tồn tại hoặc đã bị xóa.
            </div>

            <a href="${pageContext.request.contextPath}/admin-feedback"
               class="btn btn-secondary mt-3">
                ⬅ Quay lại danh sách
            </a>
        </c:if>

        <!-- Feedback tồn tại -->
        <c:if test="${feedback != null}">
            <div class="card">
                <div class="card-header">
                    <strong>Feedback #${feedback.id}</strong>
                </div>

                <div class="card-body">
                    <p>
                        <strong>Tên người dùng:</strong>${feedback.username} <br>
                        <strong>Email:</strong>${feedback.email}
                    </p>

                    <p>
                        <strong>Trạng thái:</strong>
                        <c:choose>
                            <c:when test="${feedback.status == 0}">
                                <span class="badge bg-warning text-dark">Chưa đọc</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-success">Đã đọc</span>
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <hr>

                    <p><strong>Nội dung:</strong></p>
                    <div class="feedback-box">
                            ${feedback.message}
                    </div>
                </div>

                <div class="card-footer text-end">
                    <a href="${pageContext.request.contextPath}/admin-feedback"
                       class="btn btn-secondary">
                        ⬅ Quay lại danh sách
                    </a>
                </div>
            </div>
        </c:if>

    </section>
</div>

</body>
</html>
