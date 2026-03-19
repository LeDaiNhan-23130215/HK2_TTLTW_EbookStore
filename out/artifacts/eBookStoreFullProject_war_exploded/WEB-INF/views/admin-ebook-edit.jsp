<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh sửa eBook</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-ebook.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <!-- Bootstrap -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
</head>

<body>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook" class="active">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
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
        <div class="topbar-title">Chỉnh sửa eBook</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Edit form -->
    <section class="dashboard">
        <div class="add-form">
            <h2><i class="fa-solid fa-pen-to-square"></i> Cập nhật eBook</h2>

            <form action="${pageContext.request.contextPath}/admin-ebook"
                  method="post">

                <input type="hidden" name="action" value="update"/>
                <input type="hidden" name="id" value="${ebook.id}"/>

                <div class="form-row">
                    <label>Tên sách:</label>
                    <input type="text"
                           name="title"
                           value="${ebook.title}"
                           required>
                </div>

                <div class="form-row">
                    <label>Tác giả:</label>
                    <select name="authorId" required>
                        <c:forEach var="a" items="${authors}">
                            <option value="${a.id}"
                                    <c:if test="${a.id == ebook.authors[0].id}">
                                        selected
                                    </c:if>
                            >
                                    ${a.authorName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-row">
                    <label>Thể loại:</label>
                    <select name="categoryId" required>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c.id}"
                                    <c:if test="${c.id == ebook.categoryID}">
                                        selected
                                    </c:if>>
                                    ${c.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-row">
                    <label>Giá:</label>
                    <input type="number"
                           name="price"
                           value="${ebook.price}"
                           required>
                </div>

                <div class="form-row">
                    <label>Mô tả:</label>
                    <textarea name="description"
                              rows="4">${ebook.description}</textarea>
                </div>

                <!-- Placeholder cho upload sau này -->
                <div class="form-row">
                    <label>Ảnh bìa:</label>
                    <input type="text" disabled value="(Chưa hỗ trợ chỉnh sửa)">
                </div>

                <div class="form-row">
                    <label>File ebook:</label>
                    <input type="text" disabled value="(Chưa hỗ trợ chỉnh sửa)">
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-Edit" style="background:#28a745; color: white">
                        <i class="fa-solid fa-save"></i> Lưu thay đổi
                    </button>

                    <a href="${pageContext.request.contextPath}/admin-ebook"
                       class="btn-Del">
                        <i class="fa-solid fa-xmark"></i> Hủy
                    </a>
                </div>

            </form>
        </div>
    </section>

</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>
</body>
</html>
