<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Admin Login Page</title>
    <link rel="stylesheet" href="../css/admin-login.css" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
    />
    <link rel="icon" type="image/png" href="../img/ebook-logo2.png" />
    <body>
        <form action="">
            <div class="container">
                <p class="header">Đăng nhập</p>
                <div class="input">
                <div class="input-div">
                    <input
                    type="text"
                    name="userAndEmail"
                    id="userAndEmail"
                    placeholder="Email"
                    />
                    <span class="error-msg"></span>
                </div>
                <div class="input-div">
                    <input
                    type="password"
                    name="password"
                    id="password"
                    placeholder="Mật khẩu"
                    autocomplete=""
                    />
                    <span class="error-msg"></span>
                </div>
                <button type="button" class="signIn-btn">Đăng nhập</button>
                </div>
            </div>
        </form>
    <script src="../js/admin-login.js"></script>
  </body>
</html>
