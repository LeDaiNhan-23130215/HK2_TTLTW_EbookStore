let revenueChart;
let orderChart;
let categoryChart;
let ebookChart;

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

            if (!ebookChart) {

                ebookChart = new Chart(
                    document.getElementById("ebookChart"),
                    {
                        type: 'bar',

                        data: {
                            labels: data.labels,

                            datasets: [{
                                label: "Doanh thu",
                                data: data.values
                            }]
                        },

                        options: {
                            indexAxis: 'y'
                        }
                    }
                );

            } else {

                ebookChart.data.labels = data.labels;
                ebookChart.data.datasets[0].data = data.values;

                ebookChart.update();
            }
        });
}