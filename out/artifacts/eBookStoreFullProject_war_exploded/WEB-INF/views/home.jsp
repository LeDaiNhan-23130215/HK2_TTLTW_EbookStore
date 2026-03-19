<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Ebook Store</title>
  <link rel="stylesheet" href="assets/css/base.css" />
  <link rel="stylesheet" href="assets/css/components.css" />
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
  />
  <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
  <link rel="stylesheet" href="assets/css/home.css" />
  <link
          href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap"
          rel="stylesheet"
  />
</head>
<body>
<button id="backToTopBtn" class="back-to-top">
  <i class="fa-solid fa-arrow-up"></i>
</button>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
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
          <jsp:useBean id="newEBooks" scope="request" type="java.util.List"/>
          <c:forEach var="eb" items="${newEBooks}">
            <div class="product-card" title="${eb.title}">

              <c:choose>
                <c:when test="${not empty sessionScope.userID}">
                  <form action="${pageContext.request.contextPath}/wishlist" method="post">
                    <input type="hidden" name="ebookId" value="${eb.id}">
                    <input type="hidden" name="action"
                           value="${wishlistIds != null && wishlistIds.contains(eb.id) ? 'remove' : 'add'}">

                    <button type="submit"
                            class="favorite-btn ${wishlistIds.contains(eb.id) ? 'active' : ''}">
                      <i class="fa-solid fa-heart"></i>
                    </button>
                  </form>
                </c:when>

                <c:otherwise>
                  <button type="button"
                          class="favorite-btn"
                          onclick="alert('Vui lòng đăng nhập để sử dụng chức năng này')">
                    <i class="fa-regular fa-heart"></i>
                  </button>
                </c:otherwise>
              </c:choose>

              <!-- CLICK ẢNH → BOOK DETAIL -->
              <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                <div class="img-wrapper">
                  <img src="${eb.imageLink}" alt="${eb.title}">
                </div>
              </a>

              <!-- CLICK TÊN → BOOK DETAIL -->
              <p class="book-title">
                <a href="${pageContext.request.contextPath}/bookdetail?id=${eb.id}">
                    ${eb.title}
                </a>
              </p>

              <div>
                <c:if test="${eb.price != null and eb.price gt 0}">
                <span class="price">
                    <fmt:formatNumber value="${eb.price}" type="currency" groupingUsed="true"/>
                </span>
                </c:if>

                <c:if test="${eb.price eq 0}">
                  <span>Free!!!</span>
                </c:if>

                <!-- ADD TO CART KHÔNG BỊ ẢNH HƯỞNG -->
                <form action="${pageContext.request.contextPath}/cart" method="post"
                      class="add-to-cart-form">
                  <input type="hidden" name="action" value="add"/>
                  <input type="hidden" name="bookId" value="${eb.id}"/>
                  <input type="hidden" name="price" value="${eb.price}"/>
                  <button type="submit" class="add-to-cart-btn">
                    <i class="fa-solid fa-cart-plus"></i>
                  </button>
                </form>
              </div>
            </div>
          </c:forEach>



          <button class="next-btn">
          <i class="fa-solid fa-arrow-right"></i>
        </button>
      </div>
    </div>

    <div class="sp-noi-bat">
      <h5>Sản phẩm bán chạy:</h5>

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

        <a href="list-book">Xem tất cả</a>
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
      <h5>Sản phẩm theo danh mục:</h5>

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
            <div class="all-product-button">
              <button>Xem tất cả</button>
            </div>
            <div class="finding">
              <h6>Tìm kiếm nhiều nhất</h6>
              <div class="finding-button">
                <button>Trinh thám</button>
                <button>Kinh dị</button>
                <button>Tiểu thuyết</button>
              </div>
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
            <div class="all-product-button">
              <button>Xem tất cả</button>
            </div>
            <div class="finding">
              <h6>Tìm kiếm nhiều nhất</h6>
              <div class="finding-button">
                <button>Trinh thám</button>
                <button>Kinh dị</button>
                <button>Tiểu thuyết</button>
              </div>
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
          <h2>Giới thiệu về eBookStore</h2>
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
</div>
  <jsp:include page="/WEB-INF/views/footer.jsp"/>
</body>
<script>
  const ctx = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/home.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/product-card.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/backToTopBtn.js"></script>


</body>
</html>
