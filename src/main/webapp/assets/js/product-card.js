// Sử dụng cơ chế { capture: true } ở cuối để chạy TRƯỚC các script bộ lọc/phân trang
document.addEventListener('click', function (e) {
    // 1. Xác định xem điểm click có nằm trong .product-card không
    const card = e.target.closest('.product-card');
    if (!card) return;

    // 2. NẾU CLICK VÀO: Nút yêu thích, Form giỏ hàng hoặc Nút thêm vào giỏ hàng
    // Hãy GIỮ NGUYÊN để nhường quyền xử lý hoàn toàn cho nút đó và file cart.js
    if (
        e.target.closest('.favorite-btn') ||
        e.target.closest('.add-to-cart-form') ||
        e.target.closest('.add-to-cart-btn')
    ) {
        return;
    }

    // 3. NẾU CLICK VÀO: Các vùng còn lại (Ảnh, Tên sách, Khoảng trống trên card...)
    // Chủ động CHẶN sự kiện khuếch tán lên để các script lọc/phân trang không thể can thiệp phá hỏng link
    e.preventDefault();
    e.stopPropagation();

    // 4. Tìm đường dẫn chuẩn tới trang chi tiết sách (thẻ <a> chứa chữ bookdetail)
    const detailLink = card.querySelector('a[href*="bookdetail"]');

    if (detailLink && detailLink.href) {
        // Ép trình duyệt chuyển hướng trực tiếp bằng URL chuẩn tìm thấy
        window.location.href = detailLink.href;
    } else {
        // Phương án dự phòng 1: Lấy thẻ <a> bất kỳ đầu tiên trong card
        const anyLink = card.querySelector('a');
        if (anyLink && anyLink.href) {
            window.location.href = anyLink.href;
        } else {
            // Phương án dự phòng 2: Tự dựng link bằng data-id nếu có chuẩn hóa
            const bookId = card.dataset.id || card.dataset.bookId;
            const contextPath = window.ctxPath || window.ctx || '';
            if (bookId) {
                window.location.href = contextPath + "/bookdetail?id=" + bookId;
            }
        }
    }
}, true); // Đánh dấu TRUE ở đây để kích hoạt Event Capturing siêu cấp