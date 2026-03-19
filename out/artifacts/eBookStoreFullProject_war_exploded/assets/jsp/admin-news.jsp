<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Admin - Quản lý Tin tức</title>
  <link rel="stylesheet" href="../css/admin-news.css" />
  <link rel="icon" href="../img/ebook-logo2.png" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
      <nav class="sidebar-nav">
        <a href="admin-dashboard.html">Dashboard</a>
        <a href="admin-ebook.html">Ebook</a>
        <a href="admin-category.html">Danh mục</a>
        <a href="admin-user.html">Người dùng</a>
        <a href="admin-payment.html">Thanh toán</a>
        <a href="admin-banner.html">Banner</a>
        <a href="admin-news.html" class="active">Tin tức</a>
        <a href="admin-review.html">Review</a>
        <a href="admin-feedback.html">Feedback</a>
        <hr>
        <a href="admin-login.html" class="logout">Đăng xuất</a>
      </nav>
    </aside>

    <!-- Main -->
    <div class="main-content">
      <header class="topbar">
        <h1>Quản lý Tin tức</h1>
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

      <section class="dashboard">
        <div class="add-form">
          <h2 class="toggle-title"><i class="fa-solid fa-plus"></i> Thêm bài viết mới</h2>
          <form id="addNewsForm">
            <div class="form-row">
              <label>Tiêu đề:</label>
              <input type="text" id="title" required placeholder="Tiêu đề">
            </div>
            <div class="form-row">
              <label>Ảnh minh họa (URL):</label>
              <input type="text" id="url" placeholder="Ảnh minh họa">
            </div>
              <div class="banner-preview">
                  <img id="banner-preview-img" src="" alt="Xem trước banner" style="display:none;">
              </div>
            <div class="form-row">
              <label>Tác giả:</label>
              <input type="text" id="author" placeholder="Tác giả"></input>
            </div>
            <div class="form-row">
              <label>Nội dung chính:</label>
              <textarea id="content" rows="4" placeholder="Nội dung chính"></textarea>
            </div>
            <button type="submit" class="btn-add">Thêm bài viết</button>
          </form>
        </div>
      </section>

      <section class="table-section">
        <h2>Danh sách tin tức</h2>
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Tiêu đề</th>
              <th>Ngày đăng</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody id="newsTableBody">
            <tr>
              <td>1</td>
              <td>Giảm giá ebook tháng 11</td>
              <td>2025-11-01</td>
              <td>Hiển thị</td>
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
