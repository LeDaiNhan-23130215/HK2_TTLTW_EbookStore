document.addEventListener("click", function (e) {
    // CHỈ xử lý pagination, KHÔNG xử lý sort-button
    const link = e.target.closest(".pagination a");
    if (!link) return;

    e.preventDefault();

    fetch(link.href, {
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
        .then(res => res.text())
        .then(html => {
            document.getElementById("grid-container").innerHTML = html;

            // Scroll lên đầu danh sách sản phẩm
            document.querySelector('.content').scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        })
        .catch(err => {
            console.error("Error loading page:", err);
        });
});