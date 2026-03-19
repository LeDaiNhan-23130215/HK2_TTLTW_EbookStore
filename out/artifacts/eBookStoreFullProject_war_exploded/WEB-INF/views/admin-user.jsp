<%@ page import="models.User" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin User Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-user.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css">
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
      <!-- Bootstrap 5 -->
      <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

      <!-- DataTables + Bootstrap 5 theme -->
      <link rel="stylesheet"
            href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

      <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

      <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
      <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

      <script>
          $(document).ready(function () {
              $('#activityTable').DataTable({
                  "pageLength": 5,
                  "lengthMenu": [5, 10, 20, 50],
                  "ordering": true,
                  "searching": true,
                  "language": {
                      "lengthMenu": "Hiển thị _MENU_ dòng",
                      "search": "Tìm kiếm:",
                      "info": "Trang _PAGE_ / _PAGES_",
                      "paginate": {
                          "first": "Đầu",
                          "last": "Cuối",
                          "next": "Tiếp",
                          "previous": "Trước"
                      },
                      "zeroRecords": "Không tìm thấy dữ liệu"
                  }
              });
          });
      </script>
<body>
    <!--Side bar-->
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Ebook Admin</h2>
        </div>

        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
            <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
            <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
            <a href="${pageContext.request.contextPath}/admin-user" class="active">Người dùng</a>
            <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
            <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
            <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
            <a href="${pageContext.request.contextPath}/admin-review">Review</a>
            <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
            <hr>
            <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
        </nav>
    </aside>

     <!--Main content-->
    <div class="main-content">
        <!--Top bar-->
        <header class="topbar">
            <div class="topbar-title">Quản lý người dùng</div>
            <button id="toggle-theme">🌙 Dark Mode</button>
        </header>

        <!--Dashboard-->
        <section class="dashboard">
            <div class="add-form">
                <h2 class="toggle-title">
                    <i class="fa-solid fa-plus"></i> Thêm người dùng
                </h2>

                <form action="${pageContext.request.contextPath}/admin-user"
                      method="post"
                      enctype="multipart/form-data">

                    <input type="hidden" name="action" value="add">

                    <!-- MODE SELECT -->
                    <div class="mode-select">
                        <label>
                            <input type="radio" name="mode" value="manual" checked>
                            Nhập thủ công
                        </label>
                        <label>
                            <input type="radio" name="mode" value="import">
                            Import từ CSV
                        </label>
                    </div>

                    <!-- MANUAL FORM -->
                    <div id="manualForm">

                        <div class="form-row">
                            <label>Tên người dùng:</label>
                            <input type="text" name="userName">
                        </div>

                        <div class="form-row">
                            <label>Email:</label>
                            <input type="text" name="email">
                        </div>

                        <div class="form-row">
                            <label>Số điện thoại:</label>
                            <input type="text" name="phoneNum">
                        </div>

                        <div class="form-row">
                            <label>Mật khẩu:</label>
                            <input type="password" name="password">
                        </div>

                        <div class="form-row">
                            <label>Vai trò:</label>
                            <select name="role">
                                <option value="">--Chọn vai trò--</option>
                                <option value="admin">Admin</option>
                                <option value="user">Người dùng</option>
                            </select>
                        </div>

                    </div>

                    <!-- IMPORT FORM -->
                    <div id="importForm" style="display:none">

                        <div class="form-row">
                            <label>File CSV:</label>
                            <input type="file" name="file" accept=".csv">
                        </div>

                        <small>
                            CSV gồm các cột: username,email,phone,password,role
                        </small>
                    </div>

                    <button type="submit" class="btn-addUser">
                        Thực hiện
                    </button>
                </form>
            </div>
        </section>

        <!-- Danh sách user-->
        <section class="table-section">
            <table id="activityTable" class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên</th>
                        <th>Email</th>
                        <th>Số điện thoại</th>
                        <th>Vai trò</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="userTableBody">
                    <%
                        List<User> users = (List<User>) request.getAttribute("users");
                        if (users != null){
                            for(User u : users){
                    %>
                    <tr>
                        <td><%=u.getId()%></td>
                        <td><%=u.getUsername()%></td>
                        <td><%=u.getEmail()%></td>
                        <td><%=u.getPhoneNum()%></td>
                        <td><%=u.getRole()%></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin-user?action=edit&id=<%=u.getId()%>" class="btn-Edit">
                                <i class="fa-solid fa-pen-to-square"></i> Sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/admin-user?action=delete&id=<%=u.getId()%>" class="btn-Del"
                               onclick="return confirm('Bạn có chắc muốn xóa người dùng này ?')">
                                <i class="fa-solid fa-trash"></i> Xóa
                            </a>
                        </td>
                    </tr>
                    <%
                        }
                    }
                    %>
                </tbody>
            </table>
        </section>
    </div>
    <script src="assets/js/admin-darkmode.js"></script>
    <script src="assets/js/showForm.js"></script>
</body>
</html>