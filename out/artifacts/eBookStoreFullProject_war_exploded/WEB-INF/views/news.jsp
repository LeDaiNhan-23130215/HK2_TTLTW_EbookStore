<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vn">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>News</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
  
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="assets/css/home.css" />
    <link rel="stylesheet" href="assets/css/news.css">
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>



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
                <img src="assets/img/10439-lam-ra-lam-choi-ra-choi-1.jpg" alt="4 Tựa sách giúp bạn 'chữa lành'" class="news-image">
                <h3 class="news-title">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024</h3>
                <p class="news-description">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024 Chắc hẳn bất cứ ai trong chúng ta đều có những khoảng thời gian mệt mỏi, mất niềm...</p>
                <a href="#" class="read-more-btn">Đọc tiếp</a>
            </div>

            <div class="news-item-lg">
                <div class="date-tag">
                    <p>31/07/2024</p>
                </div>
                <img src="assets/img/atomic-habits-dots.png" alt="Review TOP 10 những cuốn sách nên đọc" class="news-image">
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
                    <img src="assets/img/10439-lam-ra-lam-choi-ra-choi-1.jpg" alt="4 Tựa sách giúp bạn 'chữa lành'" class="latest-news-image">
                    <div class="news-info">
                        <p class="latest-news-title">4 TỰA SÁCH GIÚP BẠN "CHỮA LÀNH" ĐƯỢC YÊU THÍCH NHẤT NĂM 2024</p>
                        <span class="latest-news-date">09/06/2025</span>
                    </div>
                </div>
                <div class="latest-news-item">
                    <span class="news-number">2</span>
                    <img src="assets/img/atomic-habits-dots.png" alt="Review TOP 10 những cuốn sách nên đọc" class="latest-news-image">
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


  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
    <script src="assets/js/component.js"></script>
    <script src="assets/js/home.js"></script>

  </body>
</html>
