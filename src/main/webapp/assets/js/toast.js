setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => {
        t.style.opacity = '0';
        setTimeout(() => t.remove(), 300);
    });
}, 3000);
