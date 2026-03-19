// Toggle active class khi click
document.querySelectorAll('.filter-option').forEach(el => {
  el.addEventListener('click', () => {
    el.classList.toggle('active');
  });
});
