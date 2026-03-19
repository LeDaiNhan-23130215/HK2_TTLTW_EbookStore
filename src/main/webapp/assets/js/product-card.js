document.querySelectorAll('.add-to-cart-btn').forEach(btn => {
    btn.addEventListener('click', (event) => {
        event.stopPropagation();
        // Thêm class animation
        btn.classList.add('adding');
        // Sau 700ms mới chuyển trang để nhìn thấy hiệu ứng
        setTimeout(() => {
            window.location.href = ctx + '/cart';
        }, 700);
    });
});

// Xử lý click trên product-card
const productCards = document.querySelectorAll('.product-card');
productCards.forEach(card => {
    card.addEventListener('click', (e) => {
        // Nếu click vào nút trái tim thì bỏ qua
        if (e.target.classList.contains('favorite-btn') || e.target.closest('.favorite-btn')) {
            return;
        }
        window.location.href = ctx + "/bookdetail";
    });
});
