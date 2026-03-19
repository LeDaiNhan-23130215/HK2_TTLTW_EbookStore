  // --- Lấy các phần tử ---
  const emailInput = document.querySelector(".email-input");
  const codeInput = document.querySelector(".code-input");
  const passwordInput = document.querySelector(".password-input");

  const sendCodeBtn = emailInput.querySelector(".code-btn");
  const confirmCodeBtn = codeInput.querySelector(".code-btn");
  const confirmPasswordBtn = passwordInput.querySelector(".confirm-btn");

  const emailField = document.getElementById("userAndEmail");
  const codeField = document.getElementById("confirmCode");
  const newPasswordField = document.getElementById("newPassword");
  const confirmPasswordField = document.getElementById("confirmPassword");

  // --- Ẩn các bước 2 và 3 ban đầu ---
  codeInput.style.display = "none";
  passwordInput.style.display = "none";

  // --- Biến mô phỏng ---
  let mockCode = ""; // Lưu mã giả để kiểm tra

  // --- Bước 1: Gửi mã ---
  sendCodeBtn.addEventListener("click", () => {
    const email = emailField.value.trim();

    if (email === "") {
      alert("Vui lòng nhập email của bạn!");
      return;
    }

    // Kiểm tra email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      alert("Email không hợp lệ!");
      return;
    }

    // Tạo mã giả và hiển thị cho người dùng (demo)
    mockCode = Math.floor(100000 + Math.random() * 900000); // 6 chữ số
    alert(`Mã xác nhận của bạn là: ${mockCode} (demo)`);

    // Chuyển sang bước nhập mã
    codeInput.style.display = "flex";
    emailInput.querySelector(".code-btn").disabled = true;
  });

  // --- Bước 2: Xác nhận mã ---
  confirmCodeBtn.addEventListener("click", () => {
    const enteredCode = codeField.value.trim();

    if (enteredCode === "") {
      alert("Vui lòng nhập mã xác nhận!");
      return;
    }

    if (enteredCode !== mockCode.toString()) {
      alert("Mã xác nhận không đúng!");
      return;
    }

    alert("Xác nhận thành công! Vui lòng nhập mật khẩu mới.");
    passwordInput.style.display = "flex";
    confirmCodeBtn.disabled = true;
  });

  // --- Bước 3: Đổi mật khẩu ---
  confirmPasswordBtn.addEventListener("click", () => {
    const newPassword = newPasswordField.value.trim();
    const confirmPassword = confirmPasswordField.value.trim();

    if (newPassword === "" || confirmPassword === "") {
      alert("Vui lòng nhập đầy đủ mật khẩu mới và xác nhận!");
      return;
    }

    if (newPassword.length < 6) {
      alert("Mật khẩu phải có ít nhất 6 ký tự!");
      return;
    }

    if (newPassword !== confirmPassword) {
      alert("Hai mật khẩu không khớp!");
      return;
    }

    alert("Đổi mật khẩu thành công (demo) 🎉");
    window.location.href = "../pages/login.html"
    // Reset form
    emailField.value = "";
    codeField.value = "";
    newPasswordField.value = "";
    confirmPasswordField.value = "";

    emailInput.querySelector(".code-btn").disabled = false;
    confirmCodeBtn.disabled = false;
    codeInput.style.display = "none";
    passwordInput.style.display = "none";
  });
