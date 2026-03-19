<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>About - eBookStore</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link rel="stylesheet" href="assets/css/about.css" />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

   <main class="about-container">
    <!-- Banner nội dung -->
    <section class="about-banner">
        <h1>Welcome to eBookStore</h1>
        <p>Khám phá, đọc và quản lý sách điện tử một cách tiện lợi và thông minh.</p>
    </section>

    <!-- Giới thiệu dự án -->
    <section class="about-description">
        <h2>Về Dự Án</h2>
        <p>eBookStore là dự án mô phỏng một cửa hàng sách điện tử, được thiết kế để sinh viên thực hành xây dựng giao diện web và quản lý dữ liệu cơ bản. 
        Dự án giúp nâng cao kỹ năng HTML, CSS, JavaScript và tư duy thiết kế giao diện thân thiện với người dùng.</p>

        <h2>Mục Tiêu</h2>
        <ul>
            <li>Tạo trải nghiệm trực tuyến thuận tiện cho việc tìm kiếm và đọc sách.</li>
            <li>Cho phép người dùng quản lý sách và thông tin tài khoản dễ dàng.</li>
        </ul>

        <h2>Một vài tính năng</h2>
        <ul>
            <li>Danh sách sách, phân loại theo thể loại và tác giả.</li>
            <li>Chi tiết sách với mô tả và hình ảnh minh họa.</li>
        </ul>

        <h2>Nhóm Thực Hiện</h2>
        <div class="team-cards">
            <div class="team-card">
                <h3>Nguyễn Hoàng Kỳ Anh</h3>
                <p>MSSV: 23130010</p>
            </div>
            <div class="team-card">
                <h3>Lê Đại Nhân</h3>
                <p>MSSV: 23130215</p>
            </div>
            <div class="team-card">
                <h3>Phan Duy Long</h3>
                <p>MSSV: 23130180</p>
            </div>
        </div>
    </section>
</main>


  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>
  </body>
</html>
