const greetings = [
    "Chào mừng bạn đến với EBookStore!",
    "Hôm nay bạn muốn đọc gì nào?",
    "Khám phá kho sách cực hot ngay hôm nay!!",
    "EBookStore nơi tri thức được lưu trữ số hóa!",
    "Đến lúc tìm thêm tri thức rồi!",
    "Mau mau sống lại tri thức đi bạn ơi!"
]


const greeting = document.getElementById('welcomeMessage');

function autoGreetingsChanger() {
    greeting.classList.add("fade-out");
    setTimeout(()=> {
        const randomIndex = Math.floor(Math.random() * greetings.length);
        greeting.textContent = greetings[randomIndex];
        greeting.classList.remove("fade-out");
    },800);
}

autoGreetingsChanger();

setInterval(autoGreetingsChanger, 5000);

const notyBtn = document.querySelector('.notification-button');
  notyBtn.addEventListener('click', () => {
    window.location.href = "../pages/news.html"
  });
