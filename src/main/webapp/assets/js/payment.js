const secEl = document.getElementById('sec');
const bar = document.getElementById('bar');
const homeBtn = document.getElementById('homeBtn');

if (secEl && bar && homeBtn) {
  const HOME = document.body.dataset.homeUrl;
  const TOTAL = 30;
  let remaining = TOTAL;

  const tick = setInterval(() => {
    remaining--;
    secEl.textContent = remaining;
    bar.style.width = ((remaining / TOTAL) * 100) + '%';

    if (remaining <= 0) {
      clearInterval(tick);
      window.location.href = HOME;
    }
  }, 1000);

  homeBtn.addEventListener('click', () => clearInterval(tick));
}