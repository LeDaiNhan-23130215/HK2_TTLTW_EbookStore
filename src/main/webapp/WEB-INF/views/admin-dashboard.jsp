<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
      <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />

    <!-- Bootstrap 5 -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">

    <!-- DataTables + Bootstrap 5 theme -->
    <link rel="stylesheet"
          href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <!-- Admin Dashboard theme -->
    <link rel="stylesheet" href="assets/css/admin-dashboard.css" />
    <!-- jQuery + DataTables -->
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

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
</head>
<body>
    <!--Side bar-->
    <aside class="sidebar">
        <div class="sidebar-logo">
            <h2>Ebook Admin</h2>
        </div>

        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin-dashboard" class="active">Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin-ebook">Ebook</a>
            <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
            <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
            <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
            <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
            <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
            <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
            <a href="${pageContext.request.contextPath}/admin-review">Review</a>
            <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
            <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
            <a href="${pageContext.request.contextPath}/admin-voucher">Mã giảm giá</a>
            <a href="${pageContext.request.contextPath}/admin-logs">
                System Logs
            </a>
            <hr>
            <a href="${pageContext.request.contextPath}/logout" class="logout">Đăng xuất</a>
        </nav>
    </aside>

    <!--Main content-->
    <div class="main-content">
        <!--Top bar-->
        <header class="topbar">
            <div class="topbar-title">Bảng điều khiển</div>
            <div class="topbar-actions">
                <span class="admin-name">Xin chào, Admin</span>
                <button id="toggle-theme">🌙 Dark Mode</button>
            </div>
        </header>

        <!--Dashboard content-->
        <section class="dashboard">
            <div class="cards">
                <div class="card">
                    <h3>Tổng số eBook</h3>
                    <p id="totalEbooks">${totalEbooks}</p>
                </div>
                <div class="card">
                    <h3>Người dùng</h3>
                    <p id="totalUsers">${totalUsers}</p>
                </div>
                <div class="card">
                    <h3>Đơn hàng</h3>
                    <p id="totalOrders">${totalOrders}</p>
                </div>
                <div class="card">
                    <h3>Doanh thu tháng</h3>
                    <p id="totalRevenue">${totalMonthlyRevenue} VND</p>
                </div>
            </div>
        </section>

        <!-- Chart -->
        <div class="chart-grid">

            <!-- CHART CHÍNH -->
            <div class="chart-box full">
                <div class="chart-header">
                    <h3>📈 Doanh thu theo tháng</h3>
                    <span>VNĐ / 2026</span>
                </div>
                <canvas id="revenueChart"></canvas>
            </div>

            <!-- CHART PHỤ -->
            <div class="chart-box">
                <div class="chart-header">
                    <h3>🧾 Đơn hàng</h3>
                    <span>Theo tháng</span>
                </div>
                <canvas id="orderChart"></canvas>
            </div>

            <div class="chart-box">
                <div class="chart-header">
                    <h3>📚 Danh mục</h3>
                    <span>Tỉ lệ sách</span>
                </div>
                <canvas id="categoryChart"></canvas>
            </div>

            <!-- CHART FULL -->
            <div class="chart-box full">
                <div class="chart-header">
                    <h3>🔥 Top eBook</h3>
                    <span>Doanh thu cao nhất</span>
                </div>
                <canvas id="ebookChart"></canvas>
            </div>

        </div>
    </div>
    <script>
        const BASE_URL = "${pageContext.request.contextPath}";
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/admin-dashboard.js"></script>
</body>
<jsp:include page="/WEB-INF/views/admin-header-fragment.jsp"/>
</html>