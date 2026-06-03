
(function () {

    var ctx = window.ctxPath || window.ctx || '';

    function showToast(msg, type) {
        var existing = document.getElementById('cartToast');
        if (existing) existing.remove();
        var colors = { success:'#27ae60', warning:'#f59e0b', error:'#e53e3e', info:'#0396c7' };
        var el = document.createElement('div');
        el.id = 'cartToast';
        el.textContent = msg;
        el.style.cssText = [
            'position:fixed','top:20px','right:20px','z-index:9999',
            'padding:14px 22px','border-radius:8px','font-size:14px',
            'color:#fff','box-shadow:0 4px 12px rgba(0,0,0,.25)',
            'max-width:320px','word-break:break-word',
            'background:' + (colors[type] || colors.info)
        ].join(';');
        document.body.appendChild(el);
        setTimeout(function () { if (el.parentNode) el.remove(); }, 4000);
    }

    function handleWishlistClick(e) {
        var btn = e.target.closest('.favorite-btn');
        if (!btn) return;

        var form = btn.closest('form');
        if (!form) return;

        e.preventDefault();
        e.stopPropagation();

        var ebookId = form.querySelector('[name=ebookId]').value;
        var action  = form.querySelector('[name=action]').value;

        var body = new URLSearchParams();
        body.set('ebookId', ebookId);
        body.set('action',  action);

        fetch(ctx + '/wishlist', {
            method:  'POST',
            headers: { 'X-Requested-With': 'XMLHttpRequest' },
            body:    body
        })
            .then(function (r) { return r.json(); })
            .then(function (data) {
                if (data.msg) showToast(data.msg, data.type || 'success');

                var actionInput = form.querySelector('[name=action]');
                var icon        = btn.querySelector('i');
                if (data.inWishlist) {
                    btn.classList.add('active');
                    if (actionInput) actionInput.value = 'remove';
                    if (icon) {
                        icon.classList.remove('fa-regular');
                        icon.classList.add('fa-solid');
                    }
                } else {
                    btn.classList.remove('active');
                    if (actionInput) actionInput.value = 'add';
                    if (icon) {
                        icon.classList.remove('fa-solid');
                        icon.classList.add('fa-regular');
                    }
                }
            })
            .catch(function () {
                showToast('Đã xảy ra lỗi, vui lòng thử lại.', 'error');
            });
    }

    document.addEventListener('click', function (e) {
        var card = e.target.closest('.product-card');
        if (!card) return;

        // Wishlist → AJAX riêng
        if (e.target.closest('.favorite-btn')) {
            handleWishlistClick(e);
            return;
        }

        if (e.target.closest('.add-to-cart-form') || e.target.closest('.add-to-cart-btn')) {
            return;
        }

        e.preventDefault();
        e.stopPropagation();

        var detailLink = card.querySelector('a[href*="book-detail"], a[href*="bookdetail"]');
        if (detailLink && detailLink.href) {
            window.location.href = detailLink.href;
            return;
        }
        var anyLink = card.querySelector('a');
        if (anyLink && anyLink.href) {
            window.location.href = anyLink.href;
            return;
        }
        var bookId = card.dataset.id || card.dataset.bookId;
        if (bookId) window.location.href = ctx + '/book-detail?id=' + bookId;

    }, true);

})();