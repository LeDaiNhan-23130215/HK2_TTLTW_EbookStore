<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin User Manager</title>
    <link rel="stylesheet" href="../css/admin-user.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
<body>
    <!--Side bar-->
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Ebook Admin</h2>
        </div>

        <nav class="sidebar-nav">
            <a href="admin-dashboard.html">Dashboard</a>
            <a href="admin-ebook.html">Ebook</a>
            <a href="admin-category.html">Danh mục</a>
            <a href="admin-user.html" class="active">Người dùng</a>
            <a href="admin-payment.html">Thanh toán</a>
            <a href="admin-banner.html">Banner</a>
            <a href="admin-news.html">Tin tức</a>
            <a href="admin-review.html">Review</a>
            <a href="admin-feedback.html">Feedback</a>
            <hr>
            <a href="admin-login.html" class="logout">Đăng xuất</a>
        </nav>
    </aside>

     <!--Main content-->
    <div class="main-content">
        <!--Top bar-->
        <header class="topbar">
            <h1>Quản lý người dùng</h1>
            <button id="toggle-theme">🌙 Dark Mode</button>
        </header>

        <!-- Search Bar -->
        <div class="search-wrapper">
            <div class="search-bar">
                <input type="text" id="adminSearchInput" placeholder="Tìm kiếm...">
                <button type="button" id="adminSearchBtn">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </button>
            </div>
        </div>

        <!--Dashboard-->
        <section class="dashboard">
            <div class="add-form">
                <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm người dùng mới</h2>
                <form action="" id="addUserForm">
                    <div class="form-row">
                        <label for="userName">Tên người dùng: </label>
                        <input type="text" id="userName" placeholder="Tên người dùng" required>
                    </div>
                    <div class="form-row">
                        <label for="email">Email: </label>
                        <input type="text" id="email" placeholder="Email" required>
                    </div>
                    <div class="form-row">
                        <label for="phoneNum">Số điện thoại: </label>
                        <input type="text" id="phoneNum" placeholder="Số điện thoại" required>
                    </div>
                    <div class="form-row">
                        <label for="password">Mật khẩu: </label>
                        <input type="password" id="password" placeholder="Mật khẩu" required>
                    </div>
                    <div class="form-row">
                        <label for="category">Vai trò: </label>
                        <select name="genre" id="category" required>
                            <option value="">--Chọn vai trò--</option>
                            <option>Admin</option>
                            <option>Người dùng</option>
                        </select>
                    </div>

                    <button type="submit" class="btn-addUser">Thêm người dùng</button>
                </form>
            </div>
        </section>

        <!-- Danh sách user-->
        <section class="table-section">
            <table>
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
                    <tr>
                        <td>1</td>
                        <td>Nguyễn Văn A</td>
                        <td>nguyenvana@gmail.com</td>
                        <td>0123456789</td>
                        <td>Người dùng</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Nguyễn Hoàng Kỳ Anh</td>
                        <td>nguyenhoangkianh@gmail.com</td>
                        <td>039713549</td>
                        <td>Admin</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                    <tr>
                        <td>3</td>
                        <td>Lê Đại Nhân</td>
                        <td>ledainhan@gmail.com</td>
                        <td>0397345689</td>
                        <td>Admin</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                    <tr>
                        <td>4</td>
                        <td>Phan Duy Long</td>
                        <td>phanduylong@gmail.com</td>
                        <td>0397696989</td>
                        <td>Admin</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </section>
    </div>
    <script src="../js/admin-darkmode.js"></script>
    <script src="../js/showForm.js"></script>
</body>
</html>