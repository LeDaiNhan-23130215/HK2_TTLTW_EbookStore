document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.thumbnail').forEach(function (img) {
        function fallback() {
            img.onerror = null;
            img.src = window.ctxPath + "/assets/img/default-book.png";
        }

        img.onerror = fallback;

        if (!img.getAttribute('src')) {
            fallback();
        } else if (img.complete && img.naturalWidth === 0) {
            // Image already finished loading (or failed) before this script ran
            fallback();
        }
    });
});