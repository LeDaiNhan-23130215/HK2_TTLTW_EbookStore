
(function () {
    var modal = document.getElementById('cartDeleteModal');
    if (!modal) return;

    var modalTitle = document.getElementById('cartDeleteModalTitle');
    var modalDesc = document.getElementById('cartDeleteModalDesc');
    var btnCancel = document.getElementById('cartDeleteModalCancel');
    var btnConfirm = document.getElementById('cartDeleteModalConfirm');
    var pendingForm = null;

    document.querySelectorAll('.cart-delete-trigger').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var form = btn.closest('.cart-remove-form');
            if (!form) return;
            pendingForm = form;
            modalTitle.textContent = form.dataset.title || 'Xoá khỏi giỏ hàng?';
            modalDesc.textContent = form.dataset.desc || 'Bạn có chắc muốn xoá sản phẩm này?';
            modal.style.display = 'flex';
        });
    });

    function closeModal() {
        modal.style.display = 'none';
        pendingForm = null;
    }
    if(btnCancel) btnCancel.addEventListener('click', closeModal);
    modal.addEventListener('click', function (e) {
        if (e.target === modal) closeModal();
    });
    if(btnConfirm) btnConfirm.addEventListener('click', function () {
        if (pendingForm) pendingForm.submit();
    });
})();

document.addEventListener('submit', function (e) {
    // Tìm xem form đang submit có phải form add to cart không
    var form = e.target.closest('.add-to-cart-form, form[data-ajax-cart]');
    if (!form) return;

    e.preventDefault(); // Chặn reload trang mặc định

    var formData = new FormData(form);
    formData.set('action', 'add');

    // Chuyển sang URLSearchParams để tương thích với req.getParameter() ở Servlet Java
    var searchParams = new URLSearchParams(formData);

    fetch((window.ctxPath || window.ctx || '') + '/cart', {
        method: 'POST',
        body: searchParams
    })
        .then(function (r) {
            return r.json();
        })
        .then(function (data) {
            // Cập nhật số lượng hiển thị trên badge Header
            if (typeof data.count !== 'undefined') {
                var badge = document.getElementById('cartBadge');
                if (badge) badge.textContent = data.count;
            }

            // Hiển thị thông báo Toast thành công/thất bại từ server gửi về
            if (data.msg) {
                showCartToast(data.msg, data.type || 'success');
            }
        })
        .catch(function () {
            showCartToast('Đã xảy ra lỗi, vui lòng thử lại.', 'error');
        });
});

function showCartToast(msg, type) {
    var existing = document.getElementById('cartToast');
    if (existing) existing.remove();

    var colors = {
        success: '#27ae60',
        warning: '#f59e0b',
        error: '#e53e3e',
        info: '#0396c7'
    };

    var toast = document.createElement('div');
    toast.id = 'cartToast';
    toast.textContent = msg;

    toast.style.cssText = [
        'position:fixed',
        'top:20px',
        'right:20px',
        'z-index:9999',
        'padding:14px 22px',
        'border-radius:8px',
        'font-size:14px',
        'color:#fff',
        'box-shadow:0 4px 12px rgba(0,0,0,.25)',
        'max-width:320px',
        'word-break:break-word',
        'background:' + (colors[type] || colors.info)
    ].join(';');

    document.body.appendChild(toast);

    setTimeout(function () {
        if (toast.parentNode) {
            toast.remove();
        }
    }, 4000);
}