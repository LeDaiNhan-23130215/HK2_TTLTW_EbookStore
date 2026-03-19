<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Home</title>
    <link rel="stylesheet" href="../css/base.css" />
    <link rel="stylesheet" href="../css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
    <link rel="stylesheet" href="../css/home.css" />
    <link
      href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
      rel="stylesheet"
    />
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
            <a href="login.html">Đăng nhập</a>
            <a href="sign-up.html">Đăng ký</a>
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
            <a href="home-notLogin.html"
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

    <div class="home-image">
      <img
        src="https://images.squarespace-cdn.com/content/v1/62ab7b824526d0409e7d65f4/6f19b9a3-f4b5-4471-8630-2dd6aeedeadd/AdobeStock_566655293.jpeg"
        alt=""
      />
      <p class="slogan">Digital books, infinite stories.</p>
    </div>

    <div class="container">
      <div class="danh-muc-noi-bat">
        <h5>Danh mục nổi bật:</h5>
        <div class="danh-sach-danh-muc">
          <a href="">
            <div class="danh-muc">
              <i class="fa-solid fa-microchip"></i>
              <p>Tech</p>
            </div>
          </a>

          <a href="">
            <div class="danh-muc">
              <i class="fa-solid fa-brain"></i>
              <p>AI</p>
            </div></a
          >

          <a href="">
            <div class="danh-muc">
              <i class="fa-solid fa-language"></i>
              <p>Tiếng Anh</p>
            </div></a
          >
        </div>

        <div class="san-pham-moi">
          <h5>Sản phẩm mới:</h5>

          <div class="slider-container">
            <button class="prev-btn">
              <i class="fa-solid fa-arrow-left"></i>
            </button>

            <div class="slider">
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
                  Machine Learning: A Probabilistic Perspective — Kevin P.
                  Murphy
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
                <p>
                  The Phoenix Project — Gene Kim, Kevin Behr, George Spafford
                </p>
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
                    src="https://m.media-amazon.com/images/I/51hobElGJaL._AC_UF1000%2C1000_QL80_.jpg"
                    alt="TCP/IP Illustrated (Vol 1) — W. Richard Stevens"
                  />
                </div>
                <p>TCP/IP Illustrated (Vol 1) — W. Richard Stevens</p>
                <div>
                  <span>250.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>
            </div>

            <button class="next-btn">
              <i class="fa-solid fa-arrow-right"></i>
            </button>
          </div>
        </div>

        <div class="sp-noi-bat">
          <h5>Sản phẩm nổi bật:</h5>

          <div class="sp-noi-bat-container">
            <div class="sp-noi-bat-grid">
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
                    src="https://th.bing.com/th/id/OIP.N-Y26oPcUF589RHjca3kFwHaLW?w=115&h=180&c=7&r=0&o=7&dpr=1.3&pid=1.7&rm=3"
                    alt="How to Solve It – George Pólya"
                  />
                </div>
                <p>How to Solve It – George Pólya</p>
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
                    src="https://th.bing.com/th/id/OIP.gFK47UBT8m34Im9JnlRUWgHaJv?w=128&h=180&c=7&r=0&o=7&dpr=1.3&pid=1.7&rm=3"
                    alt="Oxford English Dictionary"
                  />
                </div>
                <p>Oxford English Dictionary</p>
                <div>
                  <span>560.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>
              <div class="product-card">
                <div class="img-wrapper">
                  <img
                    src="https://images.openai.com/thumbnails/url/G98sUnicDcnbDkMwAADQL3JJSBuSZWEuWwTTDKsXoUWFUTS7fc7-an-zndfz_TAh-GYqSjOR9cVFQyVRT5rcbaISPZHJfFM2NnPeT91-2f3PtCJq-ASpgRtLS4shzyiAG4xO3MUDAknDBt1xfAGS7D4GsBiZEepZiuywRvDhcYyF1A40Z-k1KI9l-Dy0VX6OhxSFFsiXoowuXo-J9V5XW9Xe8AcHOjkZ"
                    alt="The Dictionary of Lost Words"
                  />
                </div>
                <p>The Dictionary of Lost Words</p>
                <div>
                  <span>442.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>

              <div class="product-card">
                <div class="img-wrapper">
                  <img
                    src="https://images.openai.com/static-rsc-1/MY5msjAr7Ym6IXr783YCOBu_4JmkJpUvEpJyRjioG9LxnblpAvTwGFE_OY4ZMgMWuqjXwl0GWJ-s1PwkxsWnIoOvoiG8iGAbkNsgMxYab-UjtqVXnj8zf1Pa7LO4Z1zdJ14jk16FYYF-MS27gqCS4apf7_x42l4bo1KgSykoj0XCukjd4l7Tb8wrt-iuKYa5L0vaUtJELRn1kgkVCTCRK8VAfbLubm_wn-5p73-WpBEgcmwHtPWX2oXYyuXbH8v4"
                    alt="Life Skills"
                  />
                </div>
                <p>Life Skills</p>
                <div>
                  <span>813.000 VNĐ</span>
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
                  <span>480.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>

              <div class="product-card">
                <div class="img-wrapper">
                  <img
                    src="https://ebookvie.com/wp-content/uploads/2024/12/toi-la-coriander-sally-gardner.jpg"
                    alt="Tôi là Coriander"
                  />
                </div>
                <p>Tôi là Coriander</p>
                <div>
                  <span>30.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>

              <div class="product-card">
                <div class="img-wrapper">
                  <img
                    src="https://ebookvie.com/wp-content/uploads/2024/07/buc-chuc-thu-bang-mat-ma-paul-jacques-bonzon-doan-dien-dich.jpg"
                    alt="Bức Chúc Thư Bằng Mật Mã "
                  />
                </div>
                <p>Bức Chúc Thư Bằng Mật Mã</p>
                <div>
                  <span>77.000 VNĐ</span>
                  <div class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </div>
                </div>
              </div>
            </div>

            <a href="list-book.html">Xem tất cả</a>
          </div>
        </div>

        <div class="banner">
          <img
            src="https://img.freepik.com/free-vector/hand-drawn-book-club-twitter-header-template_23-2149753861.jpg"
            alt="banner"
          />
          <img
            src="https://img.freepik.com/free-vector/hand-drawn-book-club-twitter-header-template_23-2149753861.jpg"
            alt="banner"
          />
        </div>

        <section class="sp-theo-danh-muc">
          <h5>Sản phẩm theo đối tượng</h5>

          <div class="sp-container">
            <!-- Cột trái: Banner -->
            <div class="sp-banner">
              <img
                src="https://tse2.mm.bing.net/th/id/OIP.IUVt53fcwXP23-Snmv6SfAHaG1?pid=Api&P=0&h=180"
                alt="Banner 1"
              />
              <img
                src="https://uploads-ssl.webflow.com/5ad143610f7efd77b6f188f3/5c0145acdabc0e856a6389fc_51q1UbBbv-L._SY346_.jpg"
                alt="Banner 2"
              />
            </div>

            <!-- Cột phải: Nút chọn + Grid sản phẩm -->
            <div class="sp-content">
              <div class="choosing-button">
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
              </div>

              <div class="product-grid">
                <!-- 8 sản phẩm -->
                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://tse2.mm.bing.net/th/id/OIP.IUVt53fcwXP23-Snmv6SfAHaG1?pid=Api&P=0&h=180"
                      alt="Sách 1"
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
                      src="https://uploads-ssl.webflow.com/5ad143610f7efd77b6f188f3/5c0145acdabc0e856a6389fc_51q1UbBbv-L._SY346_.jpg"
                      alt="Sách 2"
                    />
                  </div>
                  <p>Atomic Habits – James Clear</p>
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
                      src="https://tse3.mm.bing.net/th/id/OIP.FbpVzEfMM-LObJR9vjoYAAAAAA?rs=1&pid=ImgDetMain&o=7&rm=3"
                      alt="Sách 3"
                    />
                  </div>
                  <p>Clean Code – Robert Martin</p>
                  <div>
                    <span>300.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/71TGolfP8fL.jpg"
                      alt="Sách 4"
                    />
                  </div>
                  <p>Refactoring — Martin Fowler</p>
                  <div>
                    <span>280.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <!-- Thêm 4 sản phẩm nữa -->
                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/71CjT0N23ML.jpg"
                      alt="Sách 5"
                    />
                  </div>
                  <p>Code Complete — Steve McConnell</p>
                  <div>
                    <span>400.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/71mhqEw8LcL._AC_UF1000%2C1000_QL80_.jpg"
                      alt="Sách 6"
                    />
                  </div>
                  <p>The DevOps Handbook — Gene Kim et al.</p>
                  <div>
                    <span>330.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/61URM5B90LL._UF1000%2C1000_QL80_.jpg"
                      alt="Sách 7"
                    />
                  </div>
                  <p>Release It! — Michael T. Nygard</p>
                  <div>
                    <span>450.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/61-8ZU7X3UL._AC_UF1000%2C1000_QL80_.jpg"
                      alt="Sách 8"
                    />
                  </div>
                  <p>Algorithms (Robert Sedgewick & Kevin Wayne)</p>
                  <div>
                    <span>1.185.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>
              </div>
              <div class="more">
                <button class="all-product-button">Xem tất cả</button>
                <h6>Tìm kiếm nhiều nhất</h6>
                <div class="finding-button">
                  <button>Trinh thám</button>
                  <button>Kinh dị</button>
                  <button>Tiểu thuyết</button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section class="sp-theo-danh-muc">
          <h5>Sản phẩm có đánh giá tốt</h5>

          <div class="sp-container">
            <!-- Cột trái: Banner -->
            <div class="sp-banner">
              <img
                src="https://tiemsach.org/wp-content/uploads/2023/07/Ebook-Di-tim-le-song.jpg"
                alt="Banner 1"
              />
              <img
                src="https://n3.sdlcdn.com/imgs/h/2/7/Can-t-Hurt-Me-Master-SDL461428142-1-3c4a5.jpg"
                alt="Banner 2"
              />
            </div>

            <!-- Cột phải: Nút chọn + Grid sản phẩm -->
            <div class="sp-content">
              <div class="choosing-button">
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
                <button>Khoa học</button>
                <button>AI</button>
                <button>Tiếng Anh</button>
                <button>Bài học cuộc sống</button>
              </div>

              <div class="product-grid">
                <!-- 8 sản phẩm -->
                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://tiemsach.org/wp-content/uploads/2023/07/Ebook-Di-tim-le-song.jpg"
                      alt="Sách 1"
                    />
                  </div>
                  <p>Đi Tìm Lẽ Sống – Viktor E. Frankl</p>
                  <div>
                    <span>135.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://n3.sdlcdn.com/imgs/h/2/7/Can-t-Hurt-Me-Master-SDL461428142-1-3c4a5.jpg"
                      alt="Sách 2"
                    />
                  </div>
                  <p>Can’t Hurt Me – David Goggins</p>
                  <div>
                    <span>310.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://m.media-amazon.com/images/I/516FZ5CPZFL._SX342_SY445_.jpg"
                      alt="Sách 3"
                    />
                  </div>
                  <p>
                    Common Science & other historical science texts — available
                    at Project
                  </p>
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
                      src="https://d3525k1ryd2155.cloudfront.net/f/109/126/9781464126109.BI.0.x.jpg"
                      alt="Sách 4"
                    />
                  </div>
                  <p>Biochemistry — Berg, Tymoczko, Stryer.</p>
                  <div>
                    <span>1.750.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <!-- Thêm 4 sản phẩm nữa -->
                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://tse3.mm.bing.net/th/id/OIP.uhw6039BlnGrgR7dhy92DgHaJk?rs=1&pid=ImgDetMain&o=7&rm=3"
                      alt="Sách 5"
                    />
                  </div>
                  <p>
                    Astronomy: Principles and Practice — (various textbooks).
                  </p>
                  <div>
                    <span>900.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://th.bing.com/th/id/R.2d12087d89ca3793dc06dc80ce044909?rik=redUrA%2fE%2b%2fIwDg&pid=ImgRaw&r=0"
                      alt="Sách 6"
                    />
                  </div>
                  <p>General Chemistry — OpenStax (PDF available)</p>
                  <div>
                    <span>230.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://images.routledge.com/common/jackets/crclarge/978042911/9780429110115.jpg"
                      alt="Sách 7"
                    />
                  </div>
                  <p>Introduction to Statistical Physics</p>
                  <div>
                    <span>1.000.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>

                <div class="product-card">
                  <div class="img-wrapper">
                    <img
                      src="https://d28hgpri8am2if.cloudfront.net/book_images/onix/interior_spreads/9781501124020/principles-9781501124020.in17.jpg"
                      alt="Sách 8"
                    />
                  </div>
                  <p>Principles – Ray Dalio</p>
                  <div>
                    <span>750.000 VNĐ</span>
                    <div class="add-to-cart-btn">
                      <i class="fa-solid fa-cart-plus"></i>
                    </div>
                  </div>
                </div>
              </div>
              <div class="more">
                <button class="all-product-button">Xem tất cả</button>
                <h6>Tìm kiếm nhiều nhất</h6>
                <div class="finding-button">
                  <button>Trinh thám</button>
                  <button>Kinh dị</button>
                  <button>Tiểu thuyết</button>
                </div>
              </div>
            </div>
          </div>
        </section>

        <div class="ebook-description">
          <div class="ebook-image">
            <img
              src="https://tse2.mm.bing.net/th/id/OIP.CWCAk80qMr8GQe3zEFt-wwHaHa?rs=1&pid=ImgDetMain&o=7&rm=3"
              alt="EBookStore"
            />
          </div>

          <div class="ebook-content">
            <div class="intro">
              <h2>Giới thiệu về Nhà sách eBook</h2>
              <p>
                Bạn là tín đồ sách và yêu thích không gian yên tĩnh? Tại đây,
                bạn không chỉ tìm thấy những cuốn sách hay mà còn có cơ hội gặp
                gỡ những người bạn cùng sở thích. Hãy đến Ebook để khám phá thế
                giới tri thức và tận hưởng những giây phút thư giãn tuyệt vời.
              </p>
            </div>

            <div class="why-choose">
              <h3>Tại sao chọn chúng tôi</h3>
              <div class="why-list">
                <div class="why-item">
                  <div class="icon">🛍️</div>
                  <div class="text">
                    <h4>Mua sắm</h4>
                    <p>
                      Với hơn 10,000 đầu sách thuộc nhiều lĩnh vực khác nhau cho
                      các bạn thỏa sức lựa chọn.
                    </p>
                  </div>
                </div>

                <div class="why-item">
                  <div class="icon">👩‍💼</div>
                  <div class="text">
                    <h4>Đội ngũ nhân viên thân thiện</h4>
                    <p>
                      Đội ngũ nhân viên thân thiện và nhiệt tình của chúng tôi
                      sẽ tư vấn và hỗ trợ bạn chọn lựa sản phẩm phù hợp với nhu
                      cầu của mình.
                    </p>
                  </div>
                </div>

                <div class="why-item">
                  <div class="icon">💬</div>
                  <div class="text">
                    <h4>Đồng hành và hỗ trợ</h4>
                    <p>
                      Chúng tôi đồng hành và hỗ trợ bạn trong suốt quá trình sử
                      dụng sản phẩm, cung cấp thông tin và trả lời mọi thắc mắc
                      của khách hàng.
                    </p>
                  </div>
                </div>
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
    <script src="../js/home.js"></script>
    <script src="../js/backToTopBtn.js"></script>
  </body>
</html>
