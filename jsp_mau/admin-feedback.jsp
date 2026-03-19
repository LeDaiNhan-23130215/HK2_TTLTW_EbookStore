<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin - Quản lý Feedback</title>
    <link rel="stylesheet" href="../css/admin-feedback.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" href="../img/ebook-logo2.png" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
    <aside class="sidebar">
        <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
        <nav class="sidebar-nav">
            <a href="admin-dashboard.html">Dashboard</a>
            <a href="admin-ebook.html">Ebook</a>
            <a href="admin-category.html">Danh mục</a>
            <a href="admin-user.html">Người dùng</a>
            <a href="admin-payment.html">Thanh toán</a>
            <a href="admin-banner.html">Banner</a>
            <a href="admin-news.html">Tin tức</a>
            <a href="admin-review.html">Review</a>
            <a href="admin-feedback.html" class="active">Feedback</a>
        <hr>
            <a href="admin-login.html" class="logout">Đăng xuất</a>
        </nav>
    </aside>

    <div class="main-content">
        <header class="topbar">
            <h1>Quản lý Feedback người dùng</h1>
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

        <section class="table-section">
        <h2>Danh sách phản hồi</h2>
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên người gửi</th>
                <th>Email</th>
                <th>Nội dung</th>
                <th>Ngày gửi</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody id="feedbackTableBody">
            <tr>
                <td>1</td>
                <td>Trần Minh</td>
                <td>minhtran@example.com</td>
                <td>Trang web rất tiện lợi, nhưng nên có thêm phần gợi ý sách.</td>
                <td>2025-10-20</td>
                <td>Chưa đọc</td>
                <td>
                <button class="btn-View"><i class="fa-solid fa-eye"></i> Xem</button>
                <button class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                </td>
            </tr>
            </tbody>
        </table>
        </section>
    </div>

    <script src="../js/admin-darkmode.js"></script>
</body>
</html>
