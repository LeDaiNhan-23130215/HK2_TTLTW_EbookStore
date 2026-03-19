const bannerInput = document.getElementById('url');
const bannerReview = document.getElementById('banner-preview-img');

bannerInput.addEventListener("input", () => {
    const url = bannerInput.value.trim();

    if(url) {
        bannerReview.src = url;
        bannerReview.style.display = "block";
    } else{
        bannerReview.style.display = "none"
    }
});