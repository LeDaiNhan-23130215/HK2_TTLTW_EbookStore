<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>EBookStore</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/toast.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">

    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"></script>
</head>

<body>
<jsp:include page="/WEB-INF/views/toast.jsp"/>

<header id="siteHeader">
    <div class="headerContainer">

        <div class="topBar">
            <div class="leftContainer">
                <p id="welcomeMessage">Chào mừng bạn đến với EBookStore</p>
            </div>

            <div class="rightContainer">
                <c:choose>
                    <c:when test="${not empty sessionScope.userName}">
                        <a href="${pageContext.request.contextPath}/userInformation">
                            Xin chào, <strong>${sessionScope.userName}</strong>
                        </a>
                        <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/sign-up">Đăng ký</a>
                    </c:otherwise>
                </c:choose>

                <p>
                    Hotline:
                    <span class="phoneNumber">
                        <i class="fa-solid fa-phone phoneIcon"></i> 0354.30.09.05
                    </span>
                </p>
            </div>
        </div>

        <div class="headerMenu">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">
                    <img src="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"
                         alt="EBookStore Logo"
                         class="logoImg"/>
                </a>
                <p>EBookStore</p>
            </div>

            <div class="categorymenu">
                <a href="${pageContext.request.contextPath}/category">
                    <i class="fa-solid fa-bars"></i> Danh mục
                </a>
            </div>

            <div class="searchBox">
                <form action="${pageContext.request.contextPath}/list-book" method="get">
                    <input type="text"
                           name="keyword"
                           placeholder="Tìm ebook..."
                           value="${param.keyword}">
                    <button type="submit">
                        <i class="fa fa-search"></i>
                    </button>
                </form>
            </div>

            <div class="after-searchbox">
                <div class="notification-button">
                    <a href="${pageContext.request.contextPath}/news">
                        <i class="fa-regular fa-bell"></i>
                    </a>
                    <span class="notification-badge">3</span>
                </div>

                <div class="order">
                    <a href="${pageContext.request.contextPath}/cart">
                        <button>
                            <i class="fa-solid fa-cart-shopping"></i> Giỏ hàng
                        </button>
                    </a>
                    <span class="notification-badge">
                        ${totalCartDetails != null ? totalCartDetails : 0}
                    </span>
                </div>
            </div>
        </div>

    </div>
</header>
<script src="${pageContext.request.contextPath}/assets/js/toast.js"></script>