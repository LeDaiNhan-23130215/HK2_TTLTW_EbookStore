document.addEventListener('DOMContentLoaded', () => {
  const slider = document.querySelector('.slider');
  const prevBtn = document.querySelector('.prev-btn');
  const nextBtn = document.querySelector('.next-btn');
  if (!slider || !prevBtn || !nextBtn) return;

  // Hàm tính bước scroll dựa vào kích thước card + gap
  function getStep() {
    const card = slider.querySelector('.product-card');
    if (!card) return slider.clientWidth * 0.8;
    const style = getComputedStyle(slider);
    const gap = parseInt(style.gap) || 16;
    return card.offsetWidth * 2 + gap*2;
  }

  nextBtn.addEventListener('click', () => {
    slider.scrollBy({ left: getStep(), behavior: 'smooth' });
  });

  prevBtn.addEventListener('click', () => {
    slider.scrollBy({ left: -getStep(), behavior: 'smooth' });
  });
});