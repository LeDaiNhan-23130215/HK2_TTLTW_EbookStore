const checkoutBtn = document.querySelector('.checkout-btn');
const qrContainer = document.querySelector('.qr-container');
const paymentRadios = document.querySelectorAll('input[name="paymentMethod"]');

// Ẩn/hiện QR khi người dùng chọn phương thức
paymentRadios.forEach(radio => {
  radio.addEventListener('change', () => {
    if (radio.value === 'qrcode') {
      qrContainer.classList.remove('hidden');
      // nhỏ delay để animation hoạt động
      setTimeout(() => qrContainer.classList.add('active'), 10);
    } else {
      qrContainer.classList.remove('active');
      setTimeout(() => qrContainer.classList.add('hidden'), 300);
    }
  });
});

function startLoading() {
  checkoutBtn.textContent = "Đang xử lý...";
  checkoutBtn.disabled = true;
}

checkoutBtn.addEventListener('click', () => {
  const selectedPayment = document.querySelector('input[name="payment"]:checked')?.value;
  startLoading()
});
