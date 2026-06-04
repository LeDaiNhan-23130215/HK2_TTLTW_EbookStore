(function () {

    var modal = document.getElementById('wishlistDeleteModal');
    if (!modal) return;

    var modalDesc = document.getElementById('wishlistDeleteModalDesc');
    var btnCancel = document.getElementById('wishlistDeleteModalCancel');
    var btnConfirm = document.getElementById('wishlistDeleteModalConfirm');

    var pendingForm = null;

    document.querySelectorAll('.wishlist-delete-trigger').forEach(function (btn) {
        btn.addEventListener('click', function () {

            var form = btn.closest('.wishlist-remove-form');
            if (!form) return;

            pendingForm = form;

            if (modalDesc) {
                modalDesc.textContent = form.dataset.desc || '';
            }

            modal.style.display = 'flex';
        });
    });

    function closeModal() {
        modal.style.display = 'none';
        pendingForm = null;
    }

    if (btnCancel) {
        btnCancel.addEventListener('click', closeModal);
    }

    if (btnConfirm) {
        btnConfirm.addEventListener('click', function () {
            if (pendingForm) {
                pendingForm.submit();
            }
        });
    }

    modal.addEventListener('click', function (e) {
        if (e.target === modal) {
            closeModal();
        }
    });

})();