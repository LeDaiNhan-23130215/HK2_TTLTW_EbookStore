const faqItems = document.querySelectorAll(".faq-item");

faqItems.forEach((item) => {
  const btn = item.querySelector(".faq-question");
  btn.addEventListener("click", () => {
    // Toggle class active
    item.classList.toggle("active");
  });
});

const faqNotFound = document.querySelector('.faq-not-found');
faqNotFound.addEventListener("click", () => {
    window.location.href = "contact.html";
});
