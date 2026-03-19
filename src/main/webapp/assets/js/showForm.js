document.addEventListener("DOMContentLoaded", function () {
    // Lấy tất cả input có name="mode"
    const modeInputs = document.querySelectorAll('input[name="mode"]');
    const manualForm = document.getElementById("manualForm");
    const importForm = document.getElementById("importForm");

    modeInputs.forEach(function (input) {
        input.addEventListener("change", function () {
            if (this.value === "manual") {
                manualForm.style.display = "block";
                importForm.style.display = "none";
            } else {
                manualForm.style.display = "none";
                importForm.style.display = "block";
            }
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const formWrapper = document.querySelector(".add-form");
    const title = document.querySelector(".add-form h2");

    title.style.cursor = "pointer";

    title.addEventListener("click", () => {
        formWrapper.classList.toggle("open");
    });
});