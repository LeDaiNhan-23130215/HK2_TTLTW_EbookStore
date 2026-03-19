const bannerInput = document.getElementById('url');
const bannerPreview = document.getElementById('banner-preview-img');

if (bannerInput && bannerPreview) {
    bannerInput.addEventListener("input", () => {
        const url = bannerInput.value.trim();

        if (url) {
            bannerPreview.src = url;
            bannerPreview.style.display = "block";
        } else {
            bannerPreview.style.display = "none";
        }
    });
}