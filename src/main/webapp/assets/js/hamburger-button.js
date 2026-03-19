document.addEventListener("DOMContentLoaded", () => {
    const hamburgerBtn = document.querySelector(".hamburger-btn");
    const filterContainer = document.querySelector(".local-filter-button-container");
    const filterMenu = document.querySelector(".local-filter-menu");

    if (!hamburgerBtn || !filterContainer || !filterMenu) return;

    hamburgerBtn.addEventListener("click", (e) => {
        e.stopPropagation();
        filterContainer.classList.toggle("open");
    });

    document.addEventListener("click", (e) => {
        if (!filterMenu.contains(e.target)) {
            filterContainer.classList.remove("open");
        }
    });
});