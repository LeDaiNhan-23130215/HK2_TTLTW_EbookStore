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
    <link rel="stylesheet" href="assets/css/faq.css" />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

    <main class="faq-container">
      <section class="faq-banner">
        <h1>FAQ - Câu hỏi thường gặp</h1>
        <p>
          Tổng hợp các câu hỏi và câu trả lời dành cho người dùng eBookStore.
        </p>
      </section>

      <section class="faq-section">
        <h2>Thông tin chung</h2>
        <div class="faq-item">
          <button class="faq-question">
            1. Làm sao để tìm sách trên eBookStore?
          </button>
          <div class="faq-answer">
            <p>
              Bạn có thể dùng thanh tìm kiếm trên thanh tìm kiếm ở phía trên
              cùng trang web, hoặc truy cập vào trang chủ để tìm kiếm, có thể
              tìm và lọc theo danh mục ở nút danh mục.
            </p>
          </div>
        </div>

        <div class="faq-item">
          <button class="faq-question">
            2. Có bao nhiêu phương thức thanh toán?
          </button>
          <div class="faq-answer">
            <p>
              Hiện tại web đang hỗ trợ thanh toán bằng ví điện tử MOMO, MB bank,
              ZaloPay, VNPay.
            </p>
          </div>
        </div>

        <div class="faq-item">
          <button class="faq-question">
            3. Tôi cần tìm sách yêu thích của mình ở đâu?
          </button>
          <div class="faq-answer">
            <p>
              Bạn cần vào thông tin người dùng, tìm kiếm mục "Danh mục yêu
              thích"
            </p>
          </div>
        </div>
        <div class="faq-item">
          <button class="faq-question">
            4. Tôi cần liên hệ với người bán ở đâu?
          </button>
          <div class="faq-answer">
            <p>
              Bạn có thể tìm thấy số điện thoại liên hệ ở góc trên cùng bên phải
              hoặc dưới dùng footer, có link zalo và facebook đã được lưu ở bên
              dưới.
            </p>
          </div>
        </div>
      </section>

      <div class="faq-not-found">Bạn không tìm thấy câu trả lời mong muốn?</div>
    </main>

  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

    <script src="assets/js/faq.js"></script>
    <script src="assets/js/component.js"></script>
    <script src="assets/js/backToTopBtn.js"></script>
  </body>
</html>
