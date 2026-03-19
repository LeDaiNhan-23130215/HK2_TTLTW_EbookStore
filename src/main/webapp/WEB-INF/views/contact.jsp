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
    <link rel="stylesheet" href="assets/css/contact.css" />
    <link rel="icon" type="image/png" href="assets/img/ebook-logo2.png" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
  </head>
  <body>
  <jsp:include page="/WEB-INF/views/header.jsp"></jsp:include>

    <main class="contact-container">
      <h2>Liên hệ với chúng tôi!</h2>
      <p>
        Chúng tôi rất hân hạnh liên hệ với bạn, vui lòng điền vào biểu mẫu để
        chúng tôi có thể liên hệ với bạn sớm nhất có thể!
      </p>

      <form action="#" method="post">
        <div class="form-group">
          <label class="group-title">
            Tiêu đề <span class="required-star">*</span></label
          >
          <input type="text" id="contact-title" class="input-field" required />

          <label for="contact_description" class="group-title">
            Mô tả: <span class="required-star">*</span>
          </label>
          <textarea
            id="contact_description"
            name="contact_description"
            class="textarea-field"
            required
          ></textarea>
        </div>

        <button type="submit" class="submit-button">Submit</button>
      </form>
    </main>

  <jsp:include page="/WEB-INF/views/footer.jsp"></jsp:include>

    <script src="assets/js/contact.js"></script>
    <script src="assets/js/component.js"></script>
    <script src="assets/js/backToTopBtn.js"></script>
  </body>
</html>
