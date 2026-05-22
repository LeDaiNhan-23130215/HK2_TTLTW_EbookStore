// ===== VALIDATE EMAIL (Step 1) =====
const EMAIL_REGEX = /^[a-zA-Z0-9]([a-zA-Z0-9._%+\-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\-]*[a-zA-Z0-9])?(\.[a-zA-Z]{2,})+$/;

const emailInput = document.getElementById('fpEmailInput');
if (emailInput) {
  const errBox = document.getElementById('fpEmailErr');
  const errTxt = document.getElementById('fpEmailErrTxt');

  function showEmailErr(msg) {
    errTxt.innerHTML = msg;
    errBox.style.display = msg ? 'flex' : 'none';
  }

  emailInput.addEventListener('blur', function () {
    const v = emailInput.value.trim();
    if (!v) {
      showEmailErr('Vui lòng nhập email.');
    } else if (!EMAIL_REGEX.test(v)) {
      showEmailErr('Email không đúng định dạng.<br>Ví dụ: abc@gmail.com');
    } else {
      showEmailErr('');
    }
  });

  emailInput.addEventListener('input', function () { showEmailErr(''); });

  document.getElementById('fpForm').addEventListener('submit', function (e) {
    const v = emailInput.value.trim();
    if (!v) {
      showEmailErr('Vui lòng nhập email.');
      e.preventDefault();
    } else if (!EMAIL_REGEX.test(v)) {
      showEmailErr('Email không đúng định dạng.<br>Ví dụ: abc@gmail.com');
      e.preventDefault();
    }
  });
}

// ===== STEP 2: Đếm ngược OTP + cooldown nút gửi lại =====
document.addEventListener('DOMContentLoaded', function () {
  var wrap      = document.getElementById('fpCountdownWrap');
  var otpDisplay = document.getElementById('fpCountdown');
  var resendBtn  = document.getElementById('fpResendBtn');
  var resendDisplay = document.getElementById('fpResendCooldown');
  var submitBtn  = document.getElementById('fpSubmitBtn');
  if (!otpDisplay) return;

  // Đọc giây còn lại từ server (tránh reset về 15:00 khi nhập sai)
  var otpTotal = wrap ? parseInt(wrap.dataset.seconds, 10) : 900;
  if (isNaN(otpTotal) || otpTotal < 0) otpTotal = 0;

  // Hiển thị ngay lập tức thay vì chờ 1 giây đầu
  (function updateDisplay() {
    var m = Math.floor(otpTotal / 60);
    var s = otpTotal % 60;
    otpDisplay.textContent = (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
  })();

  if (otpTotal <= 0) {
    otpDisplay.textContent = '00:00';
    otpDisplay.style.color = '#e74c3c';
    if (submitBtn) { submitBtn.disabled = true; submitBtn.style.opacity = '0.5'; }
  } else {
    var otpInterval = setInterval(function () {
      otpTotal--;
      var m = Math.floor(otpTotal / 60);
      var s = otpTotal % 60;
      otpDisplay.textContent = (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;
      if (otpTotal <= 0) {
        clearInterval(otpInterval);
        otpDisplay.textContent = '00:00';
        otpDisplay.style.color = '#e74c3c';
        if (submitBtn) { submitBtn.disabled = true; submitBtn.style.opacity = '0.5'; }
      }
    }, 1000);
  }

  // Cooldown nút gửi lại 30 giây
  var cooldown = 30;
  var coolInterval = setInterval(function () {
    cooldown--;
    resendDisplay.textContent = '(' + cooldown + 's)';
    if (cooldown <= 0) {
      clearInterval(coolInterval);
      resendBtn.disabled = false;
      resendDisplay.textContent = '';
    }
  }, 1000);
});

// ===== TOGGLE EYE (Step 3) =====
document.querySelectorAll('.fp-eye').forEach(function (icon) {
  icon.addEventListener('click', function () {
    var inp = document.getElementById(this.dataset.target);
    if (!inp) return;
    inp.type = inp.type === 'password' ? 'text' : 'password';
    this.classList.toggle('fa-eye');
    this.classList.toggle('fa-eye-slash');
  });
});

// ===== CHECKLIST + STRENGTH + VALIDATE (Step 3) =====
var newPwInput = document.getElementById('fpNewPw');
if (newPwInput) {
  var rules = [
    { id: 'ck-len',   test: function (pw) { return pw.length >= 10; } },
    { id: 'ck-upper', test: function (pw) { return /[A-Z]/.test(pw); } },
    { id: 'ck-lower', test: function (pw) { return /[a-z]/.test(pw); } },
    { id: 'ck-digit', test: function (pw) { return /[0-9]/.test(pw); } },
    { id: 'ck-spec',  test: function (pw) { return /[^A-Za-z0-9]/.test(pw); } }
  ];
  var colors = ['', '#e74c3c', '#e67e22', '#f1c40f', '#2ecc71', '#27ae60'];
  var labels = ['', 'Rất yếu', 'Yếu', 'Trung bình', 'Mạnh', 'Rất mạnh'];

  newPwInput.addEventListener('input', function () {
    var pw = this.value, score = 0;
    rules.forEach(function (rule) {
      var li = document.getElementById(rule.id);
      if (!li) return;
      if (rule.test(pw)) { li.classList.add('ok'); score++; }
      else { li.classList.remove('ok'); }
    });
    var bar = document.getElementById('fpStrengthBar');
    var txt = document.getElementById('fpStrengthTxt');
    if (bar) { bar.style.width = (score * 20) + '%'; bar.style.background = colors[score] || '#eee'; }
    if (txt) { txt.textContent = labels[score] || ''; txt.style.color = colors[score] || '#aaa'; }
  });

  var form3 = newPwInput.closest('form');
  if (form3) {
    form3.addEventListener('submit', function (e) {
      var pw = newPwInput.value;
      var confirmPw = document.getElementById('fpConfirmPw');
      if (!pw) { e.preventDefault(); showInlineError('Vui lòng nhập mật khẩu mới.'); return; }
      if (!rules.every(function (r) { return r.test(pw); })) {
        e.preventDefault(); showInlineError('Mật khẩu chưa đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.'); return;
      }
      if (confirmPw && pw !== confirmPw.value) {
        e.preventDefault(); showInlineError('Mật khẩu xác nhận không khớp.');
      }
    });
  }
}

function showInlineError(msg) {
  var existing = document.querySelector('.fp-error-box');
  if (!existing) {
    existing = document.createElement('p');
    existing.className = 'fp-error-box';
    var icon = document.createElement('i');
    icon.className = 'fa-solid fa-circle-exclamation';
    existing.appendChild(icon);
    existing.appendChild(document.createTextNode(' '));
    var msgSpan = document.createElement('span');
    msgSpan.className = 'fp-err-text';
    existing.appendChild(msgSpan);
    var pwInput = document.querySelector('.password-input');
    if (pwInput) pwInput.parentElement.insertBefore(existing, pwInput);
  }
  var span = existing.querySelector('.fp-err-text');
  if (span) span.textContent = msg;
  existing.style.display = 'flex';
}