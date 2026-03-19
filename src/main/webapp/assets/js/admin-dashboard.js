document.querySelectorAll('.card').forEach(card => {
    card.addEventListener('click', () => {
        if (card.id === 'totalEbooks') {
            window.location.href = '../pages/admin-ebook.html'
        }
        if (card.id === 'totalUsers') {
            window.location.href = '../pages/admin-user.html'
        }
        if (card.id === 'totalOrders' || card.id === 'totalRevenue'){
            window.location.href = '../pages/admin-payment.html'
        }
    });
});

document.addEventListener("DOMContentLoaded", () => {

    fetch(BASE_URL + "/admin-dashboard-revenue")
        .then(res => res.json())
        .then(data => {
            new Chart(document.getElementById("revenueChart"), {
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
                        y: { beginAtZero: true }
                    }
                }
            });
        })

    fetch(BASE_URL + "/admin-chart-orders")
        .then(r => r.json())
        .then(d => {
            new Chart(document.getElementById("orderChart"), {
                type: 'bar',
                data: {
                    labels: d.labels,
                    datasets: [{ label: "Số đơn", data: d.values }]
                }
            });
        });

    fetch(BASE_URL + "/admin-chart-category")
        .then(r => r.json())
        .then(d => {
            new Chart(document.getElementById("categoryChart"), {
                type: 'doughnut',
                data: {
                    labels: d.labels,
                    datasets: [{ data: d.values }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });
        });

    fetch(BASE_URL + "/admin-chart-top-ebooks")
        .then(r => r.json())
        .then(d => {
            new Chart(document.getElementById("ebookChart"), {
                type: 'bar',
                data: {
                    labels: d.labels,
                    datasets: [{ label: "Doanh thu", data: d.values }]
                },
                options: { indexAxis: 'y' }
            });
        });
});