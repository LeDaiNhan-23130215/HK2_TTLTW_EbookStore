<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<header id="siteHeader">
    <div class="headerContainer">
        <div class="topBar">
            <div class="leftContainer">
                <p id="welcomeMessage">Chào mừng bạn đến với EBookStore</p>
            </div>
            <div class="rightContainer">
                <% String userName = (String) session.getAttribute("userName"); %>

                <% if (userName != null) {%>
                    <a href="userInformation">Xin chào, <p><%=userName%></p></a>
                    <a href="logout">Đăng xuất</a>
                <%} else {%>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/sign-up">Đăng ký</a>
                <%}%>
                <p>
                    Hotline:
                    <span class="phoneNumber"
                    ><i class="fa-solid fa-phone phoneIcon"></i> 0354.30.09.05</span
                    >
                </p>
            </div>
        </div>

        <div class="headerMenu">
            <div class="logo">
                <a href="home"
                ><img
                        src="assets/img/ebook-logo2.png"
                        alt="EBookStore Logo"
                        class="logoImg"
                /></a>
                <p>EBookStore</p>
            </div>
            <div class="categorymenu">
                <a href="category"
                ><i class="fa-solid fa-bars"></i> Danh mục</a
                >
            </div>

            <div class="searchBox">
                <form action="list-book" method="get">
                    <input type="text" id="searchBox" name="keyword" placeholder="Tìm ebook..." value="${param.keyword}"/>
                    <button type="submit"><i class="fa fa-search"></i></button>
                </form>

            </div>

            <div class="after-searchbox">
                <div class="notification-button">
                    <a href="news"><i class="fa-regular fa-bell"></i></a>
                    <span class="notification-badge">3</span>
                </div>

                <div class="order">
                    <a href="cart"
                    ><button>
                        <i class="fa-solid fa-cart-shopping"></i> Giỏ hàng
                    </button></a
                    >
                    <span class="notification-badge">
                        ${(totalCartDetails != null && totalCartDetails > 0) ? totalCartDetails : 0}
                    </span>
                </div>
            </div>
        </div>
    </div>
</header>
</html>