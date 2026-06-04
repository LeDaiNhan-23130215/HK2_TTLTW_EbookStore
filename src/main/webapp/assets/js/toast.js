setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => {
        t.style.opacity = '0';
        setTimeout(() => t.remove(), 300);
    });
}, 3000);

function showToast(message, type = "success") {
    const toast = document.createElement("div");

    toast.className = `toast toast-${type}`;
    toast.textContent = message;

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}