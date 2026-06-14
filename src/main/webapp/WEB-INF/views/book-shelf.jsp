<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Thông tin người dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user-infor.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
    <div class="container">
      <div class="box-left">
        <div class="subTitle">
          <h5>TRANG TÀI KHOẢN</h5>
          <p>
            <b>Xin chào, </b>
              <b style="color: hsl(0, 100%, 60%); font-weight: 550">
                  ${sessionScope.userName}
              </b>
            !
          </p>
        </div>
        <ul class="option-list">
          <li><a href="userInformation">Thông tin tài khoản</a></li>
          <li><a href="your-order">Đơn hàng của bạn</a></li>
          <li class="selected"><a href="#">Tủ sách của bạn</a></li>
          <li><a href="wishlist">Danh mục yêu thích</a></li>
          <li><a href="change-password">Đổi mật khẩu</a></li>
        </ul>
      </div>
      <section class="box-right" id="page-content">
        <h5>TỦ SÁCH CỦA BẠN</h5>
        <p>Dưới đây là các cuốn sách bạn đã sở hữu:</p>

          <div class="bookshelf">

              <c:if test="${empty books}">
                  <p class="empty-bookshelf">
                      📚 Bạn chưa có sách nào trong tủ
                  </p>
              </c:if>

              <c:forEach var="book" items="${books}">
                  <div class="book">
                      <img
                              src="${book.images[0].imgLink}"
                              alt="${book.title}"
                      />
                      <h3>${book.title}</h3>

                      <div class="read-actions">
                          <a href="${pageContext.request.contextPath}/read-book?id=${book.id}&format=pdf"
                             class="btn-read-pdf">
                              <i class="fa-solid fa-file-pdf"></i>
                              PDF
                          </a>

                          <a href="${pageContext.request.contextPath}/read-book?id=${book.id}&format=epub"
                             class="btn-read-epub">
                              <i class="fa-solid fa-book-open"></i>
                              EPUB
                          </a>
                      </div>
                  </div>
              </c:forEach>
          </div>
      </section>
    </div>

  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
    <script src="assets/js/component.js"></script>
    <script src="assets/js/user-infor.js" defer></script>
  </body>
</html>
