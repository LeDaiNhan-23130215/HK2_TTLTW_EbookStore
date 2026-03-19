<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Review</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/login.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="assets/css/review.css"> 
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
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
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
    
    <script src="assets/js/component.js"></script>
    <script src="assets/js/login.js" defer></script>
  </body>
</html>
