<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vn">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>ReadBook</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="assets/css/home.css" />
    <link rel="stylesheet" href="assets/css/news.css">
    <link rel="stylesheet" href="assets/css/readbook.css">
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>
  <!-- Nội dung chính -->
  <main class="reader-container">
    <!-- Khung đọc sách -->
    <section class="book-view">
      <iframe src="assets/bookfile/atomic-habits-thay-doi-ti-hon-hieu-qua-bat-ngo-james-clear-ly-ngoc-anh-dich-6467.pdf" title="Sách đọc thử"></iframe>
    </section>

    <!-- Thanh công cụ bên phải -->
    <aside class="book-controls">
      <div class="control-group">
        <h3>Lật trang</h3>
        <a href="pdf/example.pdf#page=1" target="book-frame" class="btn">Trang trước</a>
        <a href="pdf/example.pdf#page=2" target="book-frame" class="btn">Trang sau</a>
      </div>

      <div class="control-group">
        <h3>Phóng to / Thu nhỏ</h3>
        <label><input type="radio" name="zoom" checked> Bình thường</label>
        <label><input type="radio" name="zoom"> Phóng to</label>
        <label><input type="radio" name="zoom"> Thu nhỏ</label>
      </div>

      <div class="control-group">
        <h3>Ghi chú</h3>
        <textarea placeholder="Nhập ghi chú tại trang này..."></textarea>
      </div>

      <div class="control-group">
        <h3>Kiểu chữ</h3>
        <label><input type="radio" name="style"> In đậm</label>
        <label><input type="radio" name="style"> Nghiêng</label>
        <label><input type="radio" name="style"> Gạch chân</label>
      </div>
    </aside>
  </main>
  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

    <script src="assets/js/component.js"></script>
    <script src="assets/js/home.js"></script>

  </body>
</html>
