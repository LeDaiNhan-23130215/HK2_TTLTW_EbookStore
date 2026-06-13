document.querySelectorAll('.thumbnail').forEach(function(img) {
    img.onerror = function () {
        this.onerror = null;
        this.src = window.ctxPath + "/assets/img/default-book.png";
    };
});