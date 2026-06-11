<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c"
           uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">
    <title>System Logs</title>
    <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css"/>
</head>

<body>
<aside class="sidebar">
    <div class="sidebar-logo">
        <h2>Ebook Admin</h2>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">
            Dashboard
        </a>
        <a href="${pageContext.request.contextPath}/admin-ebook">
            Ebook
        </a>
        <a href="${pageContext.request.contextPath}/admin-author">
            Tác giả
        </a>
        <a href="${pageContext.request.contextPath}/admin-category">
            Danh mục
        </a>
        <a href="${pageContext.request.contextPath}/admin-user">
            Người dùng
        </a>
        <a href="${pageContext.request.contextPath}/admin-payment">
            Thanh toán
        </a>
        <a href="${pageContext.request.contextPath}/admin-banner">
            Banner
        </a>
        <a href="${pageContext.request.contextPath}/admin-news">
            Tin tức
        </a>
        <a href="${pageContext.request.contextPath}/admin-review">
            Review
        </a>
        <a href="${pageContext.request.contextPath}/admin-feedback">
            Feedback
        </a>
        <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-logs">
            System Logs
        </a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login"
           class="logout">
            Đăng xuất
        </a>
    </nav>
</aside>

<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">
            System Logs
        </div>
        <div class="topbar-actions">
            <span class="admin-name">
                Xin chào, Admin
            </span>
            <button id="toggle-theme">
                🌙 Dark Mode
            </button>
        </div>
    </header>
    <section class="dashboard">
        <div class="card">
            <h3 class="mb-4">
                <i class="fa-solid fa-file-lines"></i>
                Hệ thống Log
            </h3>
            <div class="table-responsive">
                <table id="logTable"
                       class="table table-striped table-bordered log-table">
                    <thead>
                    <tr>
                        <th>
                            Nội dung Log
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="log" items="${logs}">
                        <tr>
                            <td class="log">
                                    ${log}
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </section>
</div>

<script>
    const BASE_URL = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
</body>
</html>
