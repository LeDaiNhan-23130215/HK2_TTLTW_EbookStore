document.addEventListener("DOMContentLoaded", () => {
    const hamburgerBtn = document.querySelector(".hamburger-btn");
    const filterContainer = document.querySelector(".local-filter-button-container");

    if (!hamburgerBtn || !filterContainer) return;

    hamburgerBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        filterContainer.classList.toggle("open");
    });

    document.addEventListener("click", (e) => {
        if (!filterContainer.contains(e.target) && !hamburgerBtn.contains(e.target)) {
            filterContainer.classList.remove("open");
        }
    });
});