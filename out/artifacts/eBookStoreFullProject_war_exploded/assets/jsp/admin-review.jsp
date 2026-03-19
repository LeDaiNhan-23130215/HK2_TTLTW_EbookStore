<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - Quản lý Review</title>
    <link rel="stylesheet" href="../css/admin-review.css" />
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
        <a href="admin-review.html" class="active">Review</a>
        <a href="admin-feedback.html">Feedback</a>
        <hr>
        <a href="admin-login.html" class="logout">Đăng xuất</a>
        </nav>
    </aside>

    <div class="main-content">
        <header class="topbar">
            <h1>Quản lý Review Ebook</h1>
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
            <div class="filter-section">
                <h2><i class="fa-solid fa-filter"></i> Bộ lọc Review</h2>
                <form action="" id="fiterReviewForm">
                    <div class="form-row">
                        <label for="sortMethod">Lọc theo tên:</label>
                        <select id="sortMethod">
                            <option value="">--Tất cả--</option>
                            <option value="momo">Người dùng</option>
                            <option value="vnpay">Tên eBook</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="sortMethod">Lọc theo đánh giá:</label>
                        <select id="sortMethod">
                            <option value="">--Tất cả--</option>
                            <option value="1Star">⭐</option>
                            <option value="2Star">⭐⭐</option>
                            <option value="3Star">⭐⭐⭐</option>
                            <option value="4Star">⭐⭐⭐⭐</option>
                            <option value="5Star">⭐⭐⭐⭐⭐</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="dateRange">Từ ngày:</label>
                        <input type="date" id="startDate" placeholder="Ngày">
                        <label for="endDate">Đến ngày:</label>
                        <input type="date" id="endDate" placeholder="Ngày">
                    </div>

                    <button type="submit" class="btn-filter">Lọc kết quả</button>
                </form>
            </div>
        </section>

        <section class="table-section">
            <h2>Danh sách review</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Người dùng</th>
                        <th>Tên Ebook</th>
                        <th>Đánh giá</th>
                        <th>Bình luận</th>
                        <th>Ngày</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="reviewTableBody">
                    <tr>
                        <td>1</td>
                        <td>nguyenvanA</td>
                        <td>Clean Code</td>
                        <td>⭐⭐⭐⭐</td>
                        <td>Rất hữu ích và dễ hiểu!</td>
                        <td>2025-10-30</td>
                        <td>
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
