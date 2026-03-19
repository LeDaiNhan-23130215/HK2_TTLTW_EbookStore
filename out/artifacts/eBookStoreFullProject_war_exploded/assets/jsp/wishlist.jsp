<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Danh mục yêu thích</title>
    <link rel="stylesheet" href="../css/base.css" />
    <link rel="stylesheet" href="../css/components.css" />
    <link rel="stylesheet" href="../css/user-infor.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
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

    <div class="container">
      <div class="box-left">
        <div class="subTitle">
          <h5>TRANG TÀI KHOẢN</h5>
          <p>
            <b>Xin chào, </b>
            <b style="color: hsl(0, 100%, 60%); font-weight: 550"
              >Nguyễn Văn A</b
            >
            !
          </p>
        </div>
        <ul class="option-list">
          <li><a href="user-infor.html">Thông tin tài khoản</a></li>
          <li><a href="your-order.html">Đơn hàng của bạn</a></li>
          <li><a href="book-shelf.html">Tủ sách của bạn</a></li>
          <li class="selected"><a href="#">Danh mục yêu thích</a></li>
          <li><a href="change-password.html">Đổi mật khẩu</a></li>
        </ul>
      </div>
      <div class="box-right">
        <div class="subTitle">
            <h5>Danh mục yêu thích của bạn</h5>
            <div class="wishlist-container" id="wishListContainer">
                
                <div class="wishlist-item">
                    <img src="https://tse1.mm.bing.net/th/id/OIP.fxy5-bUKrgCVIKa5hGTbZgHaG1?rs=1&pid=ImgDetMain&o=7&rm=3" alt="MachineLearning">
                    <div class="infor">
                        <h3>Machine Learning Yearning</h3>
                        <p>Tác giả: Andrew Ng</p>
                        <p><b>250.000VND</b></p>
                    </div>
                    <div class="actions">
                        <button class="add-cart" type="button">
                            <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ
                        </button>
                        <button class="remove" type="button">
                           <i class="fa-solid fa-trash"></i> Xóa
                        </button>
                    </div>
                </div>

                <div class="wishlist-item">
                    <img src="https://ebookvie.com/wp-content/uploads/2024/12/Thay-Me-Cha-Ganh-Vac-Son-Ha.jpg" alt="Thay-mẹ-cha-gánh-vác-sơn-hà">
                    <div class="infor">
                        <h3>Thay mẹ cha gánh vác sơn hà</h3>
                        <p>Tác giả: Đoàn Công Lê Duy</p>
                        <p><b>40.000VND</b></p>
                    </div>
                    <div class="actions">
                        <button class="add-cart" type="button">
                           <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ
                        </button>
                        <button class="remove" type="button">
                           <i class="fa-solid fa-trash"></i> Xóa
                        </button>
                    </div>
                </div>

                <div class="wishlist-item">
                    <img src="https://cdn1.fahasa.com/media/catalog/product/8/9/8935095620654.jpg" alt="Các-món-hè-Bánh-Truyền-Thống">
                    <div class="infor">
                        <h3>Các món hè & Bánh Truyền Thống</h3>
                        <p>Tác giả: Kim Phượng</p>
                        <p><b>25.000VND</b></p>
                    </div>
                    <div class="actions">
                        <button class="add-cart" type="button">
                           <i class="fa-solid fa-cart-shopping"></i> Thêm vào giỏ
                        </button>
                        <button class="remove" type="button">
                           <i class="fa-solid fa-trash"></i> Xóa
                        </button>
                    </div>
                </div>
            </div>
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
    <script src="../js/user-infor.js" defer></script>
  </body>
</html>
