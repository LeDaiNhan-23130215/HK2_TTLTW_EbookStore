<!DOCTYPE html>
<html lang="vn">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>News</title>
  <link rel="stylesheet" href="../css/base.css" />
     <link rel="stylesheet" href="../css/components.css" />

     <link
       rel="stylesheet"
       href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
     />
     <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
     <link rel="stylesheet" href="../css/home.css" />
     <link rel="stylesheet" href="../css/news.css">
  </head>
  <body>
    <header id="siteHeader">
      <div class="headerContainer">
        <div class="topBar">
          <div class="leftContainer">
            <p id="welcomeMessage">Chào mừng bạn đến với EBookStore</p>
          </div>
          <div class="rightContainer">
            <a href="user-infor.html">Nguyễn Văn A</a>
            <a href="home-notLogin.html">Đăng xuất</a>
            <p>
              Hotline:
              <span class="phoneNumber"
                ><i class="fa-solid fa-phone phoneIcon"></i> 0354.30.09.05</span
              >
            </p>
          </div>
        </div>

        <div class="headerMenu">
          <div class="logo">
            <a href="home-Login.html"
              ><img
                src="../img/ebook-logo2.png"
                alt="EBookStore Logo"
                class="logoImg"
            /></a>
            <p>EBookStore</p>
          </div>
          <div class="categorymenu">
            <a href="category.html"><i class="fa-solid fa-bars"></i> Danh mục</a>
          </div>

          <div class="searchBox">
            <input
              type="text"
              placeholder="Tìm sách của bạn ở đây"
              id="searchBox"
            />
            <button><i class="fa fa-search"></i></button>
          </div>

          <div class="after-searchbox">
            <div class="notification-button">
              <a><i class="fa-regular fa-bell"></i></a>
              <span class="notification-badge">3</span>
            </div>

            <div class="order">
              <a href="cart.html"
                ><button>
                  <i class="fa-solid fa-cart-shopping"></i> Giỏ hàng
                </button></a
              >
              <span class="notification-badge">0</span>
            </div>
          </div>
        </div>
      </div>
    </header>



<!-- Phần news ở đây  -->
<div class="main-news-container">
    <div class="news-content">
        <div class="news-header">
            <h2 class="section-title">Tin tức</h2>
        </div>
        
        <div class="news-list">
            <div class="news-item-lg">
                <div class="date-tag">
                    <p>09/06/2025</p>
                </div>
                <img src="../img/10439-lam-ra-lam-choi-ra-choi-1.jpg" alt="4 Tựa sách giúp bạn 'chữa lành'" class="news-image">
                <h3 class="news-title">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024</h3>
                <p class="news-description">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024 Chắc hẳn bất cứ ai trong chúng ta đều có những khoảng thời gian mệt mỏi, mất niềm...</p>
                <a href="#" class="read-more-btn">Đọc tiếp</a>
            </div>

            <div class="news-item-lg">
                <div class="date-tag">
                    <p>31/07/2024</p>
                </div>
                <img src="../img/atomic-habits-dots.png" alt="Review TOP 10 những cuốn sách nên đọc" class="news-image">
                <h3 class="news-title">REVIEW TOP 10 NHỮNG CUỐN SÁCH NÊN ĐỌC TRONG ĐỜI ĐỂ PHÁT TRIỂN BẢN THÂN</h3>
                <p class="news-description">Sách mang lại cho chúng ta 1 kho tàng tri thức khổng lồ với vô vàn bài học về cuộc sống giúp bạn vượt qua những vấn đề trong hành...</p>
                <a href="#" class="read-more-btn">Đọc tiếp</a>
            </div>
        </div>
    </div>

    <div class="sidebar">
        <div class="category-box sidebar-box">
            <h3 class="sidebar-title yellow-bg">Danh mục</h3>
            <ul class="category-list">
                <li>Sách Tech <span class="plus-icon">+</span></li>
                <li>Sách AI <span class="plus-icon">+</span></li>
                <li>Sách Science <span class="plus-icon">+</span></li>
                <li>Sách Ngoại Ngữ <span class="plus-icon">+</span></li>
                <li>Sách Nấu Ăn<span class="plus-icon">+</span></li>
                <li>Sách Thiếu Nhi<span class="plus-icon">+</span></li>
                <li>Sách Văn Học Việt Nam<span class="plus-icon">+</span></li>
            </ul>
            <h3 class="category-title-separate">Thể Loại Khác</h3>
            <ul class="category-list">
                <li>Văn Phòng Phẩm-Dụng Cụ Học Sinh <span class="plus-icon">+</span></li>
                <li>Đồ Chơi <span class="plus-icon">+</span></li>
                <li>Vật Dụng Tổng Hợp <span class="plus-icon">+</span></li>
                <li>Sách Tham Khảo</li>
            </ul>
        </div>
        
        <div class="latest-news-box sidebar-box">
            <h3 class="sidebar-title yellow-bg">Tin mới nhất</h3>
            <div class="latest-news-list">
                <div class="latest-news-item">
                    <span class="news-number">1</span>
                    <img src="../img/10439-lam-ra-lam-choi-ra-choi-1.jpg" alt="4 Tựa sách giúp bạn 'chữa lành'" class="latest-news-image">
                    <div class="news-info">
                        <p class="latest-news-title">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024</p>
                        <span class="latest-news-date">09/06/2025</span>
                    </div>
                </div>
                <div class="latest-news-item">
                    <span class="news-number">2</span>
                    <img src="../img/atomic-habits-dots.png" alt="Review TOP 10 những cuốn sách nên đọc" class="latest-news-image">
                    <div class="news-info">
                        <p class="latest-news-title">REVIEW TOP 10 NHỮNG CUỐN SÁCH NÊN ĐỌC TRONG ĐỜI ĐỂ PHÁT TRIỂN BẢN THÂN</p>
                        <span class="latest-news-date">31/07/2024</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid news-pagination-container">
    </div>










</div>
	</div>


    <footer id="siteFooter">
      <div class="topFooter">
        <div class="left-container">
          <div class="informationEmailBox">
            <input
              type="text"
              placeholder="Nhập email để nhận thông tin mới nhất!"
              id="email-input"
            />
            <a>ĐĂNG KÝ</a>
          </div>
        </div>

        <div class="right-container">
          <p>Kết nối với chúng tôi:</p>
          <img src="../img/zalo-logo.png" alt="zalo" class="zalo-logo" />
          <img
            src="../img/facebook-logo.png"
            alt="facebook"
            class="facebook-logo"
          />
        </div>
      </div>

      <div class="footer-container">
        <div class="left">
          <div class="logo">
            <a href="home-Login.html"
              ><img
                src="../img/ebook-logo2.png"
                alt="EBookStore Logo"
                class="logoImg"
            /></a>
            <p>EBookStore</p>
          </div>
          <h3>Công ty EBookStore</h3>
          <p>
            Địa chỉ: Khu phố 33, Phường Linh Xuân, TP. Hồ Chí Minh, Việt Nam
          </p>
          <p>
            <span>EBookStore</span> là mô hình nhà sách dành cho thế hệ trẻ,
            tích hợp đa dạng sản phẩm các loại sách
          </p>
          <a href="../pages/address.html" class="address">
            <i class="fa-solid fa-location-dot"></i> Khu phố 33, Phường Linh
            Xuân, TP. Hồ Chí Minh, Việt Nam</a
          >

          <p><i class="fa-solid fa-phone phoneIcon"></i> 0354.30.09.05</p>
          <p><i class="fa-solid fa-envelope"></i> ebookstore@gmail.com</p>
        </div>

        <div class="cs-hd-dm">
          <div class="option">
            <h3>CHÍNH SÁCH</h3>
            <ul>
              <li><a href="">Chính Sách Bảo Mật Thông Tin Cá Nhân</a></li>
              <li><a href="">Chính Sách Bảo Mật Thanh Toán</a></li>
              <li><a href="">Điều Khoản sử dụng</a></li>
            </ul>
          </div>

          <div class="option">
            <h3>HƯỚNG DẪN</h3>
            <ul>
              <li><a href="">Hướng dẫn mua hàng</a></li>
              <li><a href="">Phương thức thanh toán</a></li>
            </ul>
          </div>

          <div class="option">
            <h3>DANH MỤC</h3>
            <ul>
              <li><a href="../pages/home-Login.html">Trang chủ</a></li>
              <li><a href="../pages/about.html">Giới Thiệu</a></li>
              <li><a href="../pages/news.html">Tin tức</a></li>
              <li><a href="../pages/contact.html">Liên hệ</a></li>
              <li><a href="../pages/faq.html">FAQ</a></li>
            </ul>
          </div>

          <div class="kn-tt">
            <h3>GÓP Ý KHIẾU NẠI</h3>
            <a href="#">0354.30.09.05</a>
            <div class="footer-feedback">
              <p>Gửi feedback của bạn tại đây:</p>
              <a href="../pages/feedback.html" class="feedback-button">
                Feedback</a
              > 
            </div>

            <p>Tất cả các ngoài trong tuần</p>
            <p>(Trừ thứ 7 - CN, các ngày lễ)</p>
            <h3>THANH TOÁN</h3>
            <div class="payment-grid">
              <img src="../img/momo-logo.png" alt="momo" class="payment-logo" />
              <img src="../img/mb-logo.png" alt="mbbank" class="payment-logo" />
              <img
                src="../img/zalo-pay.png"
                alt="zalopay"
                class="payment-logo"
              />
              <img src="../img/vnpay.png" alt="vnpay" class="payment-logo" />
            </div>
          </div>
        </div>
      </div>

      <div class="footer-bottom">
        <p>Chúc bạn tìm được eBook ưng ý!!!</p>
      </div>
    </footer>
    <script src="../js/component.js"></script>
    <script src="../js/home.js"></script>

  </body>
</html>
