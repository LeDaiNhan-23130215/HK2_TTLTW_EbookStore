<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vn">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Address</title>
    <link rel="stylesheet" href="assets/css/base.css" />
    <link rel="stylesheet" href="assets/css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link rel="stylesheet" href="assets/css/home.css" />
    <link rel="stylesheet" href="assets/css/address.css" />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

    
    <!-- Phần thẻ ở giữa của địa chỉ -->
    <main class="addressMainContent">
  <div class="contactInfoContainer">
    <div class="infoCard infoCard--primary">
      <h2>Nhà Sách EBOOK</h2>
      <div class="infoDetail">
        <div class="infoItem">
          <i class="fa-solid fa-location-dot addressIcon"></i>
          <div class="textGroup">
            <p class="label">Địa chỉ</p>
            <p class="value">
              Khu phố 33, Phường Linh
            Xuân, TP. Hồ Chí Minh, Việt Nam
            </p>
          </div>
        </div>
        <div class="infoItem">
          <i class="fa-solid fa-clock timeIcon"></i>
          <div class="textGroup">
            <p class="label">Thời gian làm việc</p>
            <p class="value">8h30 - 23h</p>
            <p class="subValue">Tất cả các ngoài trong tuần (Trừ thứ 7 - CN, các ngày lễ)</p>
          </div>
        </div>
        <div class="infoItem">
          <i class="fa-solid fa-phone phoneIcon"></i>
          <div class="textGroup">
            <p class="label">Hotline</p>
            <p class="value">0838.99.66.22</p>
          </div>
        </div>
        <div class="infoItem">
          <i class="fa-solid fa-envelope emailIcon"></i>
          <div class="textGroup">
            <p class="label">Email</p>
            <p class="value">nhasach.ebook@gmail.com</p>
          </div>
        </div>
      </div>
    </div>
    <div class="infoCard contactFormCard">
      <h3>Liên hệ với chúng tôi</h3>
      <p class="contactDescription"><a href="assets/pages/feedback.html">Nếu bạn có thắc mắc gì, có thể gửi yêu cầu cho chúng tôi, và chúng tôi
        sẽ liên lạc lại với bạn sớm nhất có thể. Hãy bấm vào đây nhé</a>
        
      </p>
      
    </div>
  </div>

  <div class="mapContainer"> <iframe
          src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d5541.199606968563!2d106.78877698187057!3d10.870878673683931!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3175276398969f7b%3A0x9672b7efd0893fc4!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBOw7RuZyBMw6JtIFRQLiBI4buTIENow60gTWluaA!5e0!3m2!1svi!2s!4v1762017953026!5m2!1svi!2s"
          allowfullscreen>
        </iframe>
      </div>
</main>






















  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

    <script src="assets/js/component.js"></script>
  </body>
</html>
