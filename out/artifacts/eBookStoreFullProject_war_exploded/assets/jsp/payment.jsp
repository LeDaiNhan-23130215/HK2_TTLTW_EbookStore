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
    <link rel="stylesheet" href="../css/payment.css" />
</head>
<body>
    <div class="checkout-container">
        <div class="checkout-left">
            <div class="image"><img src="../img/ebook-logo2.png" alt="logo"></div>
            <div class="label-img">EBookStore</div>
            <div class="label-infor">
                <p>Thông tin nhận hàng</p>
                <a href="contact-information.html">Thay đổi</a>
            </div>
            <div class="user-information">
                <p>Nguyễn Văn A</p>
                <p>nguyenvana@gmail.com</p>
                <p>0397134548</p>
            </div>
            <form action="" class="payment-method">    
                <p>Phương thức thanh toán</p>
                <div class="payment-container">
                    <div class="payment-option">
                        <input type="radio" id="momo" name="payment" value="momo" checked>
                        <label for="momo">
                            <img src="../img/momo-logo.png" alt="Momo"> Momo
                        </label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="vnpay" name="payment" value="vnpay">
                        <label for="vnpay">
                            <img src="../img/vnpay.png" alt="VNPay"> VNPay
                        </label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="zalopay" name="payment" value="zalopay">
                        <label for="zalopay">
                            <img src="../img/zalo-pay.png" alt="ZaloPay"> ZaloPay
                        </label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="qrcode" name="payment" value="qrcode">
                        <label for="qrcode">
                            <i class="fa-solid fa-qrcode"></i> Quét mã QR (Tổng hợp)
                        </label>
                    </div>

                    <div class="qr-container hidden">
                        <p>Quét mã để thanh toán</p>
                        <img src="https://cdn.britannica.com/17/155017-050-9AC96FC8/Example-QR-code.jpg" alt="QR Code">
                    </div>
                </div>
            </form>
            <div class="option">
                <a href="cart.html"><i class="fa-solid fa-arrow-left"></i>Giỏ hàng</a>
                <button class="checkout-btn" type="button">Thanh toán</button>
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
    <script src="../js/payment.js"></script>
</body>
</html>