<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Banner Manager</title>
    <link rel="stylesheet" href="../css/admin-banner.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
</head>
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
            <a href="admin-user.html">Người dùng</a>
            <a href="admin-payment.html">Thanh toán</a>
            <a href="admin-banner.html" class="active">Banner</a>
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
            <h1>Quản lý Banner</h1>
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
                <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm banner mới</h2>
                <form action="" id="addBannerForm">
                    <div class="form-row">
                        <label for="bannerName">Tên banner: </label>
                        <input type="text" id="bannerName" placeholder="Tên banner" required>
                    </div>
                    <div class="form-row">
                        <label for="url">Đường dẫn (URL): </label>
                        <input type="text" id="url" placeholder="URL" required>
                    </div>
                    <div class="banner-preview">
                        <img id="banner-preview-img" src="" alt="Xem trước banner" style="display:none;">
                    </div>
                    <div class="form-row">
                        <label for="category">Vị trí: </label>
                        <select name="genre" id="category" required>
                            <option value="">--Chọn vị trí của banner--</option>
                            <option>Home-1</option>
                            <option>Home-2</option>
                            <option>Home-3</option>
                            <option>Home-4</option>
                            <option>Home-5</option>
                            <option>Home-6</option>
                            <option>Home-7</option>
                        </select>
                    </div>
                    <div class="form-row">
                        <label for="sDate">Ngày bắt đầu (Start date): </label>
                        <input type="date" id="sDate" placeholder="Ngày bắt đầu" required>
                    </div>
                    <div class="form-row">
                        <label for="eDate">Ngày kết thúc (End date): </label>
                        <input type="date" id="eDate" placeholder="Ngày kết thúc" required>
                    </div>
                    <button type="submit" class="btn-addBanner">Thêm banner</button>
                </form>
            </div>
        </section>

        <!-- Danh sách banner-->
        <section class="table-section">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tên banner</th>
                        <th>Vị trí</th>
                        <th>Ngày bắt đầu</th>
                        <th>Ngày kết thúc</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="bannerTableBody">
                    <tr>
                        <td>1</td>
                        <td>Banner 20/11</td>
                        <td>Home-1</td>
                        <td>11/10/2025</td>
                        <td>11/30/2025</td>
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