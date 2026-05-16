  // --- Lấy các phần tử ---
  const EMAIL_REGEX =
      /^[a-zA-Z0-9]([a-zA-Z0-9._%+\-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\-]*[a-zA-Z0-9])?(\.[a-zA-Z]{2,})+$/;

  function validateForgotEmail(emailInput, errorEl) {
    const v = emailInput.value.trim();
    if (!v) { errorEl.textContent = "Vui lòng nhập email."; return false; }
    if (!EMAIL_REGEX.test(v)) {
      errorEl.textContent = "Email không đúng định dạng. Ví dụ: abc@gmail.com";
      return false;
    }
    errorEl.textContent = "";
    return true;
  }

  const emailInput = document.querySelector('input[name="email"]');
  if (emailInput) {
    const errEl = document.createElement('span');
    errEl.style.cssText = 'color:red;font-size:12px;display:block;margin-top:4px';
    emailInput.parentElement.appendChild(errEl);
    emailInput.addEventListener('blur', () => validateForgotEmail(emailInput, errEl));
    emailInput.closest('form').addEventListener('submit', e => {
      if (!validateForgotEmail(emailInput, errEl)) e.preventDefault();
    });
  }
  const codeInput = document.querySelector(".code-input");
  const passwordInput = document.querySelector(".password-input");

  const sendCodeBtn = emailInput.querySelector(".code-btn");
  const confirmCodeBtn = codeInput.querySelector(".code-btn");
  const confirmPasswordBtn = passwordInput.querySelector(".confirm-btn");

  const emailField = document.getElementById("userAndEmail");
  const codeField = document.getElementById("confirmCode");
  const newPasswordField = document.getElementById("newPassword");
  const confirmPasswordField = document.getElementById("confirmPassword");

  // --- Thanh độ mạnh mật khẩu ---
  const bar = document.createElement("div");
  bar.style.cssText =
      "height:6px;border-radius:3px;background:#eee;margin-top:6px;width:0%;transition:all .3s";

  const txt = document.createElement("small");
  txt.style.color = "#888";

  newPasswordField.parentElement.append(bar, txt);

  newPasswordField.addEventListener("input", function () {

    const pw = this.value;

    let score = [
      pw.length >= 8,
      /[A-Z]/.test(pw),
      /[a-z]/.test(pw),
      /\d/.test(pw),
      /[^A-Za-z0-9]/.test(pw)
    ].filter(Boolean).length;

    const colors = [
      '',
      '#e74c3c',
      '#e67e22',
      '#f1c40f',
      '#2ecc71',
      '#27ae60'
    ];

    const labels = [
      '',
      'Rất yếu',
      'Yếu',
      'Trung bình',
      'Mạnh',
      'Rất mạnh'
    ];

    bar.style.width = (score * 20) + '%';
    bar.style.background = colors[score];

    txt.textContent = labels[score];
    txt.style.color = colors[score];
  });

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
    if (!EMAIL_REGEX.test(email)) {
      alert("Email không đúng định dạng!");
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

    alert("Đổi mật khẩu thành công 🎉");
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
