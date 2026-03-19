<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết thanh toán</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-payment.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
</head>

<body>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo">
        <h2>Ebook Admin</h2>
    </div>

    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment" class="active">Thanh toán</a>
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

    <!-- Topbar -->
    <header class="topbar d-flex justify-content-between align-items-center">
        <div class="topbar-title">
            Chi tiết thanh toán
        </div>

        <a href="${pageContext.request.contextPath}/admin-payment"
           class="btn btn-secondary btn-sm">
            <i class="fa-solid fa-arrow-left"></i> Quay lại
        </a>
    </header>

    <!-- Table -->
    <section class="table-section">
        <table class="table table-striped table-bordered align-middle">
            <thead>
            <tr>
                <th>ID</th>
                <th>Người dùng</th>
                <th>Số tiền (VNĐ)</th>
                <th>Phương thức</th>
                <th>Trạng thái</th>
                <th>Ngày thanh toán</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr>
                <td>${payment.id}</td>

                <td>${payment.username}</td>

                <td>
                    <fmt:formatNumber value="${payment.amount}"
                                      type="number"
                                      groupingUsed="true"/>
                </td>

                <td>${payment.method}</td>

                <td>
                    <c:choose>
                        <c:when test="${payment.status == 'success'}">
                            <span class="badge bg-success">Thành công</span>
                        </c:when>
                        <c:when test="${payment.status == 'pending'}">
                            <span class="badge bg-warning text-dark">Đang xử lý</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-danger">Thất bại</span>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <fmt:formatDate value="${payment.checkoutDate}"
                                    pattern="yyyy-MM-dd"/>
                </td>

                <td>
                    <a href="${pageContext.request.contextPath}/admin-payment"
                       class="btn btn-primary btn-sm">
                        <i class="fa-solid fa-list"></i> Danh sách
                    </a>
                </td>
            </tr>
            </tbody>
        </table>
    </section>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
</body>
</html>
