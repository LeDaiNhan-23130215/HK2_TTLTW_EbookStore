<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Payment Manager</title>
    <link rel="stylesheet" href="../css/admin-payment.css" />
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
            <a href="admin-category.html">Danh mục</a>
            <a href="admin-user.html">Người dùng</a>
            <a href="admin-payment.html" class="active">Thanh toán</a>
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
            <h1>Quản lý thanh toán</h1>
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
                <h2><i class="fa-solid fa-filter"></i> Bộ lọc thanh toán</h2>
                <form action="" id="fiterPaymentForm">
                    <div class="form-row">
                        <label for="paymentStatus">Trạng thái:</label>
                        <select id="paymentStatus">
                            <option value="">--Tất cả--</option>
                            <option value="success">Thành công</option>
                            <option value="pending">Đang xử lý</option>
                            <option value="failed">Thất bại</option>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="paymentMethod">Phương thức:</label>
                        <select id="paymentMethod">
                            <option value="">--Tất cả--</option>
                            <option value="momo">Momo</option>
                            <option value="vnpay">VNPay</option>
                            <option value="zalopay">Zalopay</option>
                            <option value="qrcode">QR</option>
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

        <!-- Danh sách thanh toán -->
        <section class="table-section">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Người dùng</th>
                        <th>Số tiền (VNĐ)</th>
                        <th>Phương thức</th>
                        <th>Trạng thái</th>
                        <th>Ngày thanh toán</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="paymentTableBody">
                    <tr>
                        <td>1</td>
                        <td>Nguyễn Văn A</td>
                        <td>120.000</td>
                        <td>Momo</td>
                        <td><span class="status success">Thành công</span></td>
                        <td>2025-10-20</td>
                        <td>
                        <button type="button" class="btn-View"><i class="fa-solid fa-eye"></i> Xem</button>
                        <button type="button" class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                    <tr>
                        <td>2</td>
                        <td>Trần Thị B</td>
                        <td>80.000</td>
                        <td>VNPay</td>
                        <td><span class="status pending">Đang xử lý</span></td>
                        <td>2025-10-21</td>
                        <td>
                        <button type="button" class="btn-View"><i class="fa-solid fa-eye"></i> Xem</button>
                        <button type="button" class="btn-Del"><i class="fa-solid fa-trash"></i> Xóa</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </section>
    </div>

    <!-- JS -->
    <script src="../js/admin-darkmode.js"></script>
</body>
</html>
