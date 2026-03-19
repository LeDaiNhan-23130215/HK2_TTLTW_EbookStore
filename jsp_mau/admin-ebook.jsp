<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Ebook Manager</title>
    <link rel="stylesheet" href="../css/admin-ebook.css" />
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
            <a href="admin-ebook.html" class="active">Ebook</a>
            <a href="admin-category.html">Danh mục</a>
            <a href="admin-user.html">Người dùng</a>
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
            <h1>Quản lý Ebook</h1>
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
                <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm sách mới</h2>
                <form action="" id="addBookForm">
                    <div class="form-row">
                        <label for="bookName">Tên sách: </label>
                        <input type="text" id="bookName" placeholder="Tên sách" required>
                    </div>
                    <div class="form-row">
                        <label for="author">Tác giả: </label>
                        <input type="text" id="author" placeholder="Tác giả" required>
                    </div>
                    <div class="form-row">
                        <label for="category">Thể loại: </label>
                        <select name="genre" id="category" required>
                            <option value="">--Chọn thể loại--</option>
                            <option>Sách khoa học</option>
                            <option>Sách nấu ăn</option>
                            <option>Sách tham khảo</option>
                            <option>Sách AI</option>
                            <option>Sách tiếng Anh</option>
                            <option>Sách kĩ năng sống</option>
                        </select>
                    </div>
                    <div class="form-row">
                        <label for="price">Giá: </label>
                        <input type="text" id="price" placeholder="Giá" required>
                    </div>
                    <div class="form-row">
                        <label for="url">Ảnh bìa (URL): </label>
                        <input type="text" id="url" placeholder="Ảnh bìa (URL)" required>
                    </div>
                    <div class="banner-preview">
                        <img id="banner-preview-img" src="" alt="Xem trước banner" style="display:none;">
                    </div>
                    <div class="form-row">
                        <label for="file">File: </label>
                        <input type="text" id="file" placeholder="File" required>
                    </div>
                    <div class="form-row">
                        <label for="description">Mô tả: </label>
                        <input type="text" id="description" placeholder="Mô tả" required>
                    </div>

                    <button type="submit" class="btn-addEbook">Thêm sách</button>
                </form>
            </div>
        </section>

        <!-- Danh sách eBook-->
        <section class="table-section">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên sách</th>
                        <th>Tác giả</th>
                        <th>Thể loại</th>
                        <th>Giá</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="eBookTableBody">
                    <tr>
                        <td>1</td>
                        <td>Deep Work</td>
                        <td>Cal Newport</td>
                        <td>Sách kĩ năng sống</td>
                        <td>280.000 VND</td>
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
    <script src="../js/admin-demoIMG.js"></script>
    <script src="../js/showForm.js"></script>
</body>
</html>