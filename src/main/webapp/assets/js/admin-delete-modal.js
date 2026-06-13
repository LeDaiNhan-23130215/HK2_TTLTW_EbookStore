/* ===========================================================
   Admin Delete Modal — dùng chung cho các trang admin
   Style/hành vi giống modal xoá ở trang cart (ui-modal-*)
   ===========================================================
   HTML cần có (đặt 1 lần, trước </body>):

     <div id="adminDeleteModal" class="ui-modal-overlay" style="display:none;" aria-modal="true" role="dialog">
         <div class="ui-modal-box">
             <div class="ui-modal-icon"><i class="fa-solid fa-trash-can"></i></div>
             <h3 class="ui-modal-title" id="adminDeleteModalTitle">Xác nhận xoá</h3>
             <p class="ui-modal-desc" id="adminDeleteModalDesc"></p>
             <div class="ui-modal-actions">
                 <button type="button" class="ui-modal-cancel" id="adminDeleteModalCancel">Quay lại</button>
                 <button type="button" class="ui-modal-confirm" id="adminDeleteModalConfirm">
                     <i class="fa-solid fa-trash-can"></i> Xoá
                 </button>
             </div>
         </div>
     </div>

   Trên link/nút xoá, thêm class "admin-delete-trigger" và data attributes:

     <a class="btn-Del admin-delete-trigger"
        href="${pageContext.request.contextPath}/admin-xxx?action=delete&id=5"
        data-title="Xoá tác giả?"
        data-desc="Bạn có chắc muốn xoá tác giả 'Nguyễn Nhật Ánh'? Hành động này không thể hoàn tác.">
         <i class="fa-solid fa-trash"></i> Xóa
     </a>
   =========================================================== */

(function () {
    var modal = document.getElementById('adminDeleteModal');
    if (!modal) return;

    var modalTitle  = document.getElementById('adminDeleteModalTitle');
    var modalDesc   = document.getElementById('adminDeleteModalDesc');
    var btnCancel   = document.getElementById('adminDeleteModalCancel');
    var btnConfirm  = document.getElementById('adminDeleteModalConfirm');
    var pendingUrl  = null;

    document.querySelectorAll('.admin-delete-trigger').forEach(function (el) {
        el.addEventListener('click', function (e) {
            e.preventDefault();
            pendingUrl = el.getAttribute('href') || el.dataset.url;
            modalTitle.textContent = el.dataset.title || 'Xác nhận xoá';
            modalDesc.textContent  = el.dataset.desc  || 'Bạn có chắc muốn xoá mục này? Hành động này không thể hoàn tác.';
            modal.style.display = 'flex';
        });
    });

    function closeModal() {
        modal.style.display = 'none';
        pendingUrl = null;
    }

    if (btnCancel) btnCancel.addEventListener('click', closeModal);

    modal.addEventListener('click', function (e) {
        if (e.target === modal) closeModal();
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') closeModal();
    });

    if (btnConfirm) btnConfirm.addEventListener('click', function () {
        if (pendingUrl) window.location.href = pendingUrl;
    });
})();
