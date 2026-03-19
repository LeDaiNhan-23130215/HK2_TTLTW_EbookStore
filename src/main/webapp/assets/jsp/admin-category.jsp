<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Category Manager</title>
    <link rel="stylesheet" href="../css/admin-category.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
</head>
<body>
        <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Ebook Admin</h2>
        </div>

        <nav class="sidebar-nav">
            <a href="admin-dashboard.html">Dashboard</a>
            <a href="admin-ebook.html">Ebook</a>
            <a href="admin-category.html" class="active">Danh mục</a>
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

     <!-- Main content -->
    <div class="main-content">
        <!-- Top bar -->
        <header class="topbar">
            <h1>Quản lý Danh mục</h1>
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

        <!-- Dashboard -->
        <section class="dashboard">
            <div class="add-form">
                <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm danh mục mới</h2>
                <form action="" id="addCategoryForm">
                    <div class="form-row">
                        <label for="catName">Tên danh mục:</label>
                        <input type="text" id="catName" placeholder="Nhập tên danh mục" required>
                    </div>
                    <div class="form-row">
                        <label for="catDesc">Mô tả:</label>
                        <textarea id="catDesc" rows="3" placeholder="Nhập mô tả ngắn về danh mục..."></textarea>
                    </div>
                    <button type="submit" class="btn-addCategory">Thêm danh mục</button>
                </form>
            </div>
        </section>

        <!-- Danh sách danh mục -->
        <section class="table-section">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên danh mục</th>
                        <th>Mô tả</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="categoryTableBody">
                    <tr>
                        <td>1</td>
                        <td>Tiểu thuyết</td>
                        <td>Các tác phẩm văn học nổi tiếng</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Kinh doanh</td>
                        <td>Sách về đầu tư, tài chính và kỹ năng lãnh đạo</td>
                        <td>
                            <button class="btn-Edit"><i class="fa-solid fa-pen-to-square"></i> Sửa</button>
                            <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </section>
    </div>

    <!-- JS -->
    <script src="../js/admin-darkmode.js"></script>
    <script src="../js/showForm.js"></script>
</body>
</html>
