<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Thông tin người dùng</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link rel="stylesheet" href="assets/css/user-infor.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />

      <script>
          $(document).ready(function () {
              $('#orderTable').DataTable({
                  "pageLength": 5,
                  "lengthMenu": [5, 10, 20, 50],
                  "ordering": true,
                  "searching": true,
                  "language": {
                      "lengthMenu": "Hiển thị _MENU_ đơn hàng",
                      "search": "Tìm kiếm:",
                      "info": "Trang _PAGE_ / _PAGES_",
                      "paginate": {
                          "next": "Tiếp",
                          "previous": "Trước"
                      },
                      "zeroRecords": "Không tìm thấy đơn hàng"
                  }
              });
          });
      </script>
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
          <li class="selected"><a href="#">Đơn hàng của bạn</a></li>
          <li><a href="book-shelf">Tủ sách của bạn</a></li>
          <li><a href="wishlist">Danh mục yêu thích</a></li>
          <li><a href="change-password">Đổi mật khẩu</a></li>
        </ul>
      </div>
      <div class="box-right">
        <div class="subTitle">
          <h5>ĐƠN HÀNG CỦA BẠN</h5>
        </div>
          <table id="orderTable" class="table table-striped table-bordered">
              <thead>
              <tr>
                  <th>Mã đơn</th>
                  <th>Ngày</th>
                  <th>Giá trị</th>
                  <th>Trạng thái</th>
              </tr>
              </thead>

              <tbody>
              <c:choose>
                  <c:when test="${empty orders}">
                      <tr>
                          <td colspan="4">Không có đơn hàng nào.</td>
                      </tr>
                  </c:when>
                  <c:otherwise>
                      <c:forEach var="o" items="${orders}">
                          <tr>
                              <td>#${o.id}</td>

                              <td>
                                  <fmt:formatDate value="${o.checkoutDate}"
                                                  pattern="dd/MM/yyyy HH:mm"/>
                              </td>

                              <td>
                                  <fmt:formatNumber value="${o.totalAmount}"
                                                    type="currency"
                                                    currencySymbol="₫"/>
                              </td>

                              <td>
                                  <c:choose>
                                      <c:when test="${o.status eq 'success'}">
                                          <span class="badge bg-success">Thành công</span>
                                      </c:when>
                                      <c:when test="${o.status eq 'pending'}">
                                          <span class="badge bg-warning text-dark">Chờ xử lý</span>
                                      </c:when>
                                      <c:otherwise>
                                          <span class="badge bg-danger">Thất bại</span>
                                      </c:otherwise>
                                  </c:choose>
                              </td>
                          </tr>
                      </c:forEach>
                  </c:otherwise>
              </c:choose>
              </tbody>
          </table>
      </div>
    </div>

  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

    <script src="assets/js/user-infor.js" defer></script>
    <script src="assets/js/component.js"></script>
  </body>
</html>
