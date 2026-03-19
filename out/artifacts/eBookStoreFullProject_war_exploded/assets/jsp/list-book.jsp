<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>EBook</title>
    <link rel="stylesheet" href="../css/base.css" />
    <link rel="stylesheet" href="../css/components.css" />
    <link rel="stylesheet" href="../css/list-book.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
  </head>
  <body>
    <button id="backToTopBtn" class="back-to-top">
      <i class="fa-solid fa-arrow-up"></i>
    </button>
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
            <a href="category.html"
              ><i class="fa-solid fa-bars"></i> Danh mục</a
            >
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

    <main>
      <aside class="sidebar">
        <div class="filter">
          <h3>Thể loại</h3>
          <label><input type="checkbox" /> Tech</label>
          <label><input type="checkbox" /> Tham khảo</label>
          <label><input type="checkbox" /> Tiếng Anh</label>
          <label><input type="checkbox" /> Văn học Việt Nam</label>
          <a href="category.html"
            ><i class="fa-solid fa-book"></i> Tất cả danh mục</a
          >
        </div>

        <div class="filter">
          <h3>Giá</h3>
          <label><input type="checkbox" /> Miễn phí</label>
          <label><input type="checkbox" /> Có phí</label>
        </div>

        <div class="filter">
          <h3>Loại file</h3>
          <label><input type="checkbox" /> PDF</label>
          <label><input type="checkbox" /> EPUB</label>
          <label><input type="checkbox" /> MOBI</label>
        </div>
      </aside>

      <section class="content">
        <div class="selected-filters">
          <strong>Đang chọn:</strong>
          <div class="filter-show">
            <span
              >Thể loại:
              <p></p
            ></span>
            <span
              >Giá:
              <p></p
            ></span>
            <span
              >File:
              <p></p
            ></span>
          </div>
        </div>

        <div class="local-filters">
          <div class="local-filter-menu">
            <button class="hamburger-btn">
              <i class="fa-solid fa-bars"></i> Bộ lọc
            </button>

            <div class="local-filter-button-container">
              <button class="sort-button" data-order="asc" id="sortByNameAsc">
                <i class="fa-solid fa-arrow-down-a-z"></i> A - Z
              </button>

              <button class="sort-button" data-order="desc" id="sortByNameDesc">
                <i class="fa-solid fa-arrow-up-a-z"></i> Z - A
              </button>

              <button class="sort-button" data-order="asc" id="sortByPriceAsc">
                <i class="fa-solid fa-arrow-up"></i> Giá tăng dần
              </button>

              <button
                class="sort-button"
                data-order="desc"
                id="sortByPriceDesc"
              >
                <i class="fa-solid fa-arrow-down"></i> Giá giảm dần
              </button>

              <button class="sort-button" data-order="desc" id="sortByDate">
                <i class="fa-solid fa-calendar"></i> Sản phẩm mới
              </button>
            </div>
          </div>
        </div>

        <div class="product-grid">
          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://tse2.mm.bing.net/th/id/OIP.IUVt53fcwXP23-Snmv6SfAHaG1?pid=Api&P=0&h=180"
                alt="Deep Work"
              />
            </div>
            <p>Deep Work – Cal Newport</p>
            <div>
              <span>280.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/71fqxXDY2ZL._AC_UF1000,1000_QL80_.jpg"
                alt="Machine Learning: A Probabilistic Perspective — Kevin P. Murphy"
              />
            </div>
            <p>
              Machine Learning: A Probabilistic Perspective — Kevin P. Murphy
            </p>
            <div>
              <span>3.288.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-ranh-gioi.jpg"
                alt="Ranh giới"
              />
            </div>
            <p>Ranh Giới</p>
            <div>
              <span>79.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://images.openai.com/static-rsc-1/UKOZBe1niJXMPnqBsmcOK9GaTn-10svkJnhO91TQE0Cfns8VTUGkXNVc-o7Hj2lyDLlpvLMiXfE2BcwWiVkEl4k2p1DRp8OH-R5Ho_DSWEaXJjMzgb6KYR05MsiXkyuoE2lDK2YbjOe0_RydthXDBt-RfbM2KTk5SpdjtmCEqBtvqGW4TyqRYyJYNaDuL4D_jMIdbW0DLXJvfbCGzXtwL_3dTkZjdpVKujQe6rxsgayMOavxsZLoJ0REjI-_Apxg"
                alt="Life Skills by Julia Laflin"
              />
            </div>
            <p>Life Skills by Julia Laflin</p>
            <div>
              <span>136.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-dem-hoi-long-tri.jpg"
                alt="Đêm Hội Long Trì"
              />
            </div>
            <p>Đêm Hội Long Trì</p>
            <div>
              <span>100.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-an-may-di-vang.jpg"
                alt="Ăn Mày Dĩ Vãng"
              />
            </div>
            <p>Ăn Mày Dĩ Vãng</p>
            <div>
              <span>34.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-mot-chu-nhat-khac.jpg"
                alt="Một Chủ Nhật Khác"
              />
            </div>
            <p>Một Chủ Nhật Khác</p>
            <div>
              <span>69.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://images.openai.com/static-rsc-1/uv2mkcawi4hHcOSCzxAJg4x5ypciL-z92U6ZfJzonI0Z-W8m3s0hFm_l5_AOKF5Z-6Ey6RL8RLgwYINnoG7aYC6zcmWY0xP7mCyKIaeOfWHlKw5qoS2Ggne7KIEvXD1MvkqTh9uuuw8nM1d85yyrDCHlzC8hezCOe9SW8bPZhOloo4rxNCwkFzS7l1t8qPY00RklS_coXkXrZwYx4DduU7F-E13T1eCJihU4WBr9RPGbBM7Yg9fRIJ5wLqvCcSW_I5LdEx6QNwr92NBEbKjAJg"
                alt="The Power of Now"
              />
            </div>
            <p>The Power of Now</p>
            <div>
              <span>250.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://images.openai.com/static-rsc-1/ZtHzCxNPOSldGnQ8CE6MI0l5yahrsdtvFHrsyBOQFfT8xlPL7RBqngnqyBynDpNj5V5UKoeP2fdRO5C4_DfqZHRkOqYcT1UKaEhDctiNnLEzTmeGCyykNcz3QTzdyTvyhbKe3UfXmEBpwl5RI8oWgHHNRmWZvaF6pyMEnoLgn19yvxnhk4k6DbcAqV83a8NQwwlVbcVQvrZtI8FkakCW8Zp4rrlJCodZRhOTaq9CWGEMRlCcZzVCRXUpnbGsbH9q"
                alt="Collins Vietnamese Dictionary Essential Edition"
              />
            </div>
            <p>Collins Vietnamese Dictionary Essential Edition</p>
            <div>
              <span>480.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://tse3.mm.bing.net/th/id/OIP.vg_jWjn6hEIOjIBAwk_IygHaHa?pid=Api&P=0&h=180"
                alt="Bí Mật Của May Mắn – Alex Rovira & Fernando Trías de Bes"
              />
            </div>
            <p>Bí Mật Của May Mắn – Alex Rovira & Fernando Trías de Besr</p>
            <div>
              <span>115.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nxbphunu.com.vn/wp-content/uploads/2018/09/120-mon-sup-bo-duong.jpg"
                alt="120 Món Súp Bổ Dưỡng Cho Trẻ Em Và Người Bệnh (Mỹ Hạnh)"
              />
            </div>
            <p>120 Món Súp Bổ Dưỡng Cho Trẻ Em Và Người Bệnh (Mỹ Hạnh)</p>
            <div>
              <span>78.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-nhieu-cach-song.jpg"
                alt="Atomic Habits"
              />
            </div>
            <p>Nhiều Cách Sống</p>
            <div>
              <span>78.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/71NMJeBW1AL._AC_UF1000%2C1000_QL80_.jpg"
                alt="The Phoenix Project — Gene Kim, Kevin Behr, George Spafford"
              />
            </div>
            <p>The Phoenix Project — Gene Kim, Kevin Behr, George Spafford</p>
            <div>
              <span>630.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/61JmBUeCaNL._AC_UF1000%2C1000_QL80_.jpg"
                alt="Computer Networks — Andrew S. Tanenbaum / Kurose & Ross"
              />
            </div>
            <p>Computer Networks — Andrew S. Tanenbaum / Kurose & Ross</p>
            <div>
              <span>250.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/71kj6C0TNdL._AC_UF1000%2C1000_QL80_.jpg"
                alt="The Linux Programming Interface — Michael Kerrisk"
              />
            </div>
            <p>The Linux Programming Interface — Michael Kerrisk</p>
            <div>
              <span>1.841.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/7167aaVxs3L._AC_UF1000%2C1000_QL80_.jpg"
                alt="Effective Java — Joshua Bloch"
              />
            </div>
            <p>Effective Java — Joshua Bloch</p>
            <div>
              <span>1.395.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1347590276i/5941151.jpg"
                alt="Physical Biology of the Cell — Rob Phillips et al."
              />
            </div>
            <p>Physical Biology of the Cell — Rob Phillips et al.</p>
            <div>
              <span>1.600.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://tse2.mm.bing.net/th/id/OIP.nXPR4zK0BI3FwnCI_NiXDAAAAA?rs=1&pid=ImgDetMain&o=7&rm=3"
                alt="General Relativity: The Theoretical Minimum"
              />
            </div>
            <p>General Relativity: The Theoretical Minimum</p>
            <div>
              <span>1.200.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://m.media-amazon.com/images/I/61-6TTTBZeL._SL1000_.jpg"
                alt="Artificial Intelligence: A Modern Approach — Russell & Norvig"
              />
            </div>
            <p>Artificial Intelligence: A Modern Approach — Russell & Norvig</p>
            <div>
              <span>1.500.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://indianbookstore.co.in/wp-content/uploads/2024/04/Thinking-Fast-and-Slow-by-Daniel-Kahneman.webp"
                alt="Thinking, Fast and Slow – Daniel Kahneman"
              />
            </div>
            <p>Thinking, Fast and Slow – Daniel Kahneman</p>
            <div>
              <span>600.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://bizweb.dktcdn.net/100/351/397/products/21067451420617153-ship-or-sheep-an-intermediate-pronunciation-course-jpeg.jpg?v=1553845949583"
                alt="Ship or Sheep? — Minimal Pairs Practice for Pronunciation"
              />
            </div>
            <p>Ship or Sheep? — Minimal Pairs Practice for Pronunciation</p>
            <div>
              <span>200.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://dokumen.pub/img/200x200/nau-mon-an-han-quoc-that-de.jpg"
                alt="Nấu Món Ăn Hàn Quốc Thật Dễ (Tổng cục du lịch Hàn Quốc)"
              />
            </div>
            <p>Nấu Món Ăn Hàn Quốc Thật Dễ (Tổng cục du lịch Hàn Quốc)</p>
            <div>
              <span>50.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://ebookvie.com/wp-content/uploads/2024/12/lo-bo-chua-te-vung-carompo.jpg"
                alt="Lô Bô Chúa tể vùng Ca Răm Pô (Lobo Chúa Tể Currumpaw)"
              />
            </div>
            <p>Lô Bô Chúa tể vùng Ca Răm Pô (Lobo Chúa Tể Currumpaw)</p>
            <div>
              <span>80.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>

          <div class="product-card">
            <div class="img-wrapper">
              <img
                src="https://nhasachmienphi.com/images/thumbnail/nhasachmienphi-quan-khu-nam-dong.jpg"
                alt="Quân Khu Nam Đồng"
              />
            </div>
            <p>Quân Khu Nam Đồng</p>
            <div>
              <span>78.000 VNĐ</span>
              <div class="add-to-cart-btn">
                <i class="fa-solid fa-cart-plus"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="pagination">
          <button class="page-btn active">1</button>
          <button class="page-btn">2</button>
          <button class="page-btn">3</button>
          <button class="page-btn">4</button>
          <button class="page-btn">5</button>
        </div>
      </section>
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

    <script src="../js/product-card.js"></script>
    <script src="../js/list-book-pagination.js"></script>
    <script>
      const hamburgerBtn = document.querySelector(".hamburger-btn");
      const filterContainer = document.querySelector(
        ".local-filter-button-container"
      );

      hamburgerBtn.addEventListener("click", () => {
        filterContainer.classList.toggle("open");
      });

      // Click ra ngoài để đóng menu
      document.addEventListener("click", (e) => {
        if (!document.querySelector(".local-filter-menu").contains(e.target)) {
          filterContainer.classList.remove("open");
        }
      });
    </script>
    <script src="../js/component.js"></script>
    <script src="../js/backToTopBtn.js"></script>
  </body>
</html>
