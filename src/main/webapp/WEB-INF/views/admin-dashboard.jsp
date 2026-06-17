<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
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

                <div class="card" id="cardEbooks">
                    <h3>Tổng số eBook</h3>
                    <p>${totalEbooks}</p>
                </div>

                <div class="card" id="cardUsers">
                    <h3>Người dùng</h3>
                    <p>${totalUsers}</p>
                </div>

                <div class="card" id="cardOrders">
                    <h3>Đơn hàng</h3>
                    <p>${totalOrders}</p>
                </div>

                <div class="card" id="cardRevenue">
                    <h3>Doanh thu tháng</h3>
                    <p><fmt:formatNumber value="${totalMonthlyRevenue}" type="number" groupingUsed="true"/> VND</p>
                </div>

            </div>
        </section>


        <!-- Chart -->
        <div class="chart-grid">

            <!-- CHART CHÍNH -->
            <div class="chart-box full">
                <div class="chart-header">
                    <div>
                        <h3>📈 Doanh thu</h3>
                    </div>

                    <select id="yearSelect"
                            class="form-select"
                            style="width: 120px;">
                    </select>
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
                    <h3>🔥 Top eBook </h3>
                    <span>Doanh thu cao nhất</span>
                </div>
                <canvas id="ebookHighestChart"></canvas>
            </div>

            <div class="chart-box full">
                <div class="chart-header">
                    <h3>Bottom eBook </h3>
                    <span>Doanh thu thấp nhất</span>
                </div>
                <canvas id="ebookLowestChart"></canvas>
            </div>

        </div>
    </div>
    <script>
        const BASE_URL = "${pageContext.request.contextPath}";
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
    <script>
        let revenueChart;
        let orderChart;
        let categoryChart;
        let ebookHighestChart;
        let ebookLowestChart;

        /* =========================
           DOM READY
        ========================= */
        document.addEventListener("DOMContentLoaded", () => {

            setupCardClick();
            setupYearSelect();
            loadYears();
        });

        /* =========================
           CARD CLICK
        ========================= */
        function setupCardClick() {

            document.querySelectorAll('.card').forEach(card => {

                card.addEventListener('click', () => {

                    switch (card.id) {

                        case 'cardEbooks':
                            window.location.href = BASE_URL + "/admin-ebook";
                            break;

                        case 'cardUsers':
                            window.location.href = BASE_URL + "/admin-user";
                            break;

                        case 'cardOrders':
                        case 'cardRevenue':
                            window.location.href = BASE_URL + "/admin-payment";
                            break;
                    }
                });
            });
        }

        /* =========================
           YEAR SELECT
        ========================= */
        function setupYearSelect() {

            const select = document.getElementById("yearSelect");

            select.addEventListener("change", function () {

                reloadAllCharts(this.value);
            });
        }

        /* =========================
           LOAD YEARS
        ========================= */
        function loadYears() {

            fetch(BASE_URL + "/admin-chart-year")
                .then(res => {
                    console.log("admin-chart-year status:", res.status, res.url); // thêm dòng này
                    if (!res.ok) throw new Error("HTTP " + res.status);
                    return res.json();
                })
                .then(years => {
                    console.log("Years received:", years);
                    console.log("YEARS:", years);
                    const select = document.getElementById("yearSelect");

                    if (!select) {
                        console.error("yearSelect not found");
                        return;
                    }

                    select.innerHTML = "";

                    years.forEach(year => {

                        const option = document.createElement("option");

                        option.value = year;
                        option.textContent = year;

                        select.appendChild(option);
                    });

                    const maxYear = Math.max(...years);

                    select.value = maxYear;

                    reloadAllCharts(maxYear);
                })
                .catch(err => {
                    console.error("Load years error:", err);
                });
        }

        /* =========================
           RELOAD ALL CHARTS
        ========================= */
        function reloadAllCharts(year) {

            loadRevenueChart(year);
            loadOrderChart(year);
            loadCategoryChart(year);
            loadTopEbookChart(year);
            loadBotEbookChart(year)
        }

        /* =========================
           REVENUE CHART
        ========================= */
        function loadRevenueChart(year) {

            fetch(BASE_URL + "/admin-dashboard-revenue?year=" + year)
                .then(res => res.json())
                .then(data => {

                    if (!revenueChart) {

                        revenueChart = new Chart(
                            document.getElementById("revenueChart"),
                            {
                                type: 'line',

                                data: {
                                    labels: data.labels,

                                    datasets: [{
                                        label: "Doanh thu (VND)",
                                        data: data.values,
                                        borderWidth: 3,
                                        tension: 0.3,
                                        fill: true
                                    }]
                                },

                                options: {
                                    responsive: true,
                                    maintainAspectRatio: false,

                                    scales: {
                                        y: {
                                            beginAtZero: true
                                        }
                                    }
                                }
                            }
                        );

                    } else {

                        revenueChart.data.labels = data.labels;
                        revenueChart.data.datasets[0].data = data.values;

                        revenueChart.update();
                    }
                });
        }

        /* =========================
           ORDER CHART
        ========================= */
        function loadOrderChart(year) {

            fetch(BASE_URL + "/admin-chart-orders?year=" + year)
                .then(res => res.json())
                .then(data => {

                    if (!orderChart) {

                        orderChart = new Chart(
                            document.getElementById("orderChart"),
                            {
                                type: 'bar',

                                data: {
                                    labels: data.labels,

                                    datasets: [{
                                        label: "Số đơn",
                                        data: data.values
                                    }]
                                }
                            }
                        );

                    } else {

                        orderChart.data.labels = data.labels;
                        orderChart.data.datasets[0].data = data.values;

                        orderChart.update();
                    }
                });
        }

        /* =========================
           CATEGORY CHART
        ========================= */
        function loadCategoryChart(year) {

            fetch(BASE_URL + "/admin-chart-category?year=" + year)
                .then(res => res.json())
                .then(data => {

                    if (!categoryChart) {

                        categoryChart = new Chart(
                            document.getElementById("categoryChart"),
                            {
                                type: 'doughnut',

                                data: {
                                    labels: data.labels,

                                    datasets: [{
                                        data: data.values
                                    }]
                                }
                            }
                        );

                    } else {

                        categoryChart.data.labels = data.labels;
                        categoryChart.data.datasets[0].data = data.values;

                        categoryChart.update();
                    }
                });
        }

        /* =========================
           TOP EBOOK CHART
        ========================= */
        function loadTopEbookChart(year) {
            fetch(BASE_URL + "/admin-chart-top-ebooks?year=" + year)
                .then(res => res.json())
                .then(data => {
                    if (!ebookHighestChart) {
                        ebookHighestChart = new Chart(
                            document.getElementById("ebookHighestChart"),
                            {
                                type: 'bar',
                                data: {
                                    labels: data.labels,
                                    datasets: [{
                                        label: "Doanh thu",
                                        data: data.values
                                    }]
                                },
                                options: { indexAxis: 'y' }
                            }
                        );
                    } else {
                        ebookHighestChart.data.labels = data.labels;
                        ebookHighestChart.data.datasets[0].data = data.values;
                        ebookHighestChart.update();
                    }
                });
        }

        function loadBotEbookChart(year) {
            fetch(BASE_URL + "/admin-chart-bot-ebooks?year=" + year)
                .then(res => res.json())
                .then(data => {
                    if (!ebookLowestChart) {
                        ebookLowestChart = new Chart(
                            document.getElementById("ebookLowestChart"),
                            {
                                type: 'bar',
                                data: {
                                    labels: data.labels,
                                    datasets: [{
                                        label: "Doanh thu",
                                        data: data.values
                                    }]
                                },
                                options: { indexAxis: 'y' }
                            }
                        );
                    } else {
                        ebookLowestChart.data.labels = data.labels;
                        ebookLowestChart.data.datasets[0].data = data.values;
                        ebookLowestChart.update();
                    }
                });
        }
    </script>
    <jsp:include page="/WEB-INF/views/admin-header-fragment.jsp"/>
</body>

</html>