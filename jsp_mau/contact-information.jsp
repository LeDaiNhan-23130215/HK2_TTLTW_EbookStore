<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin</title>
    <link rel="stylesheet" href="../css/base.css" />
    <link rel="stylesheet" href="../css/components.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
    <link rel="stylesheet" href="../css/contact-information.css" />
</head>
<body>
    <div class="checkout-container">
        <div class="checkout-left">
            <div class="image"><img src="../img/ebook-logo2.png" alt="logo"></div>
            <div class="label-img">EBookStore</div>
            <div class="label-logout">
                <p>Thông tin nhận hàng</p>
                <a href="home-notLogin.html"><i class="fa-solid fa-right-from-bracket"></i>Đăng xuất</a>
            </div>
            <div class="form-container">
                <form action="">
                    <div class="input">
                        <input class="emailInput" id="emailInput" type="email" placeholder=" " required>
                        <label for="emailInput">Email</label>
                        <span class="error-msg"></span>
                    </div>
                    <div class="input">
                        <input class="fullNameInput" id="fullNameInput" type="text" placeholder=" " required>
                        <label for="fullNameInput">Họ và tên</label>
                        <span class="error-msg"></span>
                    </div>
                    <div class="input">
                        <input class="phoneNumInput" id="phoneNumInput" type="text" placeholder=" " required>
                        <label for="phoneNumInput">Số điện thoại</label>
                        <span class="error-msg"></span>
                    </div>
                    <div class="input">
                        <textarea name="note" id="note" placeholder=" "></textarea>
                        <label for="note">Ghi chú (tùy chọn)</label>
                    </div>
                    <div class="option">
                        <a href="cart.html"><i class="fa-solid fa-arrow-left"></i>Giỏ hàng</a>
                        <button class="continue-btn" type="button">Tiếp tục phương thức thanh toán</button>
                    </div>
                </form>
            </div>
        </div>
        <div class="checkout-right">
            <div class="label-right">Đơn hàng (1 sản phẩm)</div>
            <div class="product-row">
                <div class="product-detail">
                    <img src="https://tse2.mm.bing.net/th/id/OIP.IUVt53fcwXP23-Snmv6SfAHaG1?pid=Api&P=0&h=180" alt="Deep Work">
                    <div class="product-name">Deep Work</div>
                </div>  
                <div class="product-price">280.000 VND</div>
            </div>
            <div class="price">
                <div class="sub-price-title">Tạm tính</div>
                <div class="product-price">280.000 VND</div>
            </div>
            <div class="total">
                <div class="total-price-title">Tổng tiền</div>
                <div class="total-price">280.000 VND</div>
            </div>
        </div>
    </div>
    <script src="../js/contact-information.js"></script>
</body>
</html>