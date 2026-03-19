<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Review</title>
    <link rel="stylesheet" href="../css/base.css" />
    <link rel="stylesheet" href="../css/login.css" />
    <link rel="stylesheet" href="../css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
    <link rel="stylesheet" href="../css/review.css"> 
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



<!-- Phần thẻ của đánh giá -->
   <main class="review-page-content">
  <div class="review-container">
    <section class="rating-summary">
      <h2 class="section-title">Đánh giá & Nhận xét</h2>
      <div class="summary-box">
        <div class="average-rating">
          <span class="rating-score">4.5</span>/5
          <div class="stars-display">
            <i class="fa-solid fa-star primary-star"></i>
            <i class="fa-solid fa-star primary-star"></i>
            <i class="fa-solid fa-star primary-star"></i>
            <i class="fa-solid fa-star primary-star"></i>
            <i class="fa-solid fa-star-half-stroke primary-star"></i>
          </div>
        </div>

        <div class="rating-breakdown">
          <div class="star-row">
            5 sao
            <div class="bar-background">
              <div class="rating-bar" style="width: 70%;"></div>
            </div>
            70%
          </div>
          <div class="star-row">
            4 sao
            <div class="bar-background">
              <div class="rating-bar" style="width: 15%;"></div>
            </div>
            15%
          </div>
          <div class="star-row">
            3 sao
            <div class="bar-background">
              <div class="rating-bar" style="width: 8%;"></div>
            </div>
            8%
          </div>
          <div class="star-row">
            2 sao
            <div class="bar-background">
              <div class="rating-bar" style="width: 5%;"></div>
            </div>
            5%
          </div>
          <div class="star-row">
            1 sao
            <div class="bar-background">
              <div class="rating-bar" style="width: 2%;"></div>
            </div>
            2%
          </div>
        </div>
      </div>
    </section>

    <hr class="divider">

    <section class="review-list">
      <h3 class="section-subtitle">Tất cả nhận xét</h3>

      <div class="review-card">
        <div class="review-header">
          <div class="user-info">
            <span class="user-name">Nguyễn Hoàng Kỳ Anh</span>
          </div>
          <div class="review-rating-time">
            <span class="review-stars-small">
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
            </span>
            <span class="review-date">Đã đánh giá vào 15/11/2025</span>
          </div>
        </div>
        <p class="review-comment">Sách rất hay, nội dung rõ ràng và dễ hiểu. Rất đáng tiền!</p>
        </div>

      <div class="review-card">
        <div class="review-header">
          <div class="user-info">
            <span class="user-name">Lê Đại Nhân</span>
          </div>
          <div class="review-rating-time">
            <span class="review-stars-small">
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-regular fa-star primary-star"></i>
            </span>
            <span class="review-date">Đã đánh giá vào 10/11/2025</span>
          </div>
        </div>
        <p class="review-comment">Dịch vụ rất tốt hỗ trợ khách hàng nhanh. Nội dung sách thì tuyệt vời!</p>
      </div>

       <div class="review-card">
        <div class="review-header">
          <div class="user-info">
            <span class="user-name">Phan Duy Long</span>
          </div>
          <div class="review-rating-time">
            <span class="review-stars-small">
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-solid fa-star primary-star"></i>
              <i class="fa-regular fa-star primary-star"></i>
              <i class="fa-regular fa-star primary-star"></i>
              <i class="fa-regular fa-star primary-star"></i>
            </span>
            <span class="review-date">Đã đánh giá vào 10/11/2025</span>
          </div>
        </div>
        <p class="review-comment">Tôi cảm thấy nội dung của sách khá cũ và việc hỗ trợ người dùng còn khá chậm!</p>
        </div>
      
      <div class="load-more">
        <button class="submit-review-btn">Xem thêm đánh giá</button>
      </div>
    </section>

  </div>
</main>























    <footer id="siteFooter">
      <div class="topFooter">
        <div class="left-container">
          <div class="informationEmailBox">
            <input
              type="text"
              placeholder="Nhập email để nhận thông tin mới nhất!"
              id="email-input"
            />
            <button>ĐĂNG KÝ</button>
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
            <a href="home.html"
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
          <a href="../pages/address.html">
            <i class="fa-solid fa-location-dot"></i>Khu phố 33, Phường Linh
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
            <p>Tất cả các ngoài trong tuần (Trừ thứ 7 - CN, các ngày lễ)</p>
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
    <script src="../js/login.js" defer></script>
  </body>
</html>
