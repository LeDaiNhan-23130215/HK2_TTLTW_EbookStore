// ===== VALIDATE EMAIL (Step 1) =====
const EMAIL_REGEX = /^[a-zA-Z0-9]([a-zA-Z0-9._%+\-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\-]*[a-zA-Z0-9])?(\.[a-zA-Z]{2,})+$/;

// ===== XỬ LÝ SESSIONSTORAGE THEO TRẠNG THÁI URL =====
(function () {
  var params = new URL(window.location.href).searchParams;

  if (params.get('resent') === 'true') {
    // Xóa storage cũ
    Object.keys(sessionStorage).forEach(function (k) {
      if (k.startsWith('otp_')) sessionStorage.removeItem(k);
    });
    // Đặt flag để coolTick mở nút gửi lại ngay sau khi gửi lại thành công
    sessionStorage.setItem('otp_cooldown_skip_init', '1');
  }

  if (params.get('error') === 'alreadyUsed') {
    Object.keys(sessionStorage).forEach(function (k) {
      if (k.startsWith('otp_deadline_')) sessionStorage.removeItem(k);
      if (k.startsWith('otp_cooldown_')) sessionStorage.removeItem(k);
    });
    sessionStorage.setItem('otp_cooldown_skip_init', '1');
  }
})();

// ===== VALIDATE EMAIL =====
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
    if (!v)                        showEmailErr('Vui lòng nhập email.');
    else if (!EMAIL_REGEX.test(v)) showEmailErr('Email không đúng định dạng.<br>Ví dụ: abc@gmail.com');
    else                           showEmailErr('');
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

// ===== STEP 2: ĐẾM NGƯỢC OTP + COOLDOWN NÚT GỬI LẠI =====
document.addEventListener('DOMContentLoaded', function () {
  var params = new URL(window.location.href).searchParams;

  var wrap          = document.getElementById('fpCountdownWrap');
  var otpDisplay    = document.getElementById('fpCountdown');
  var submitBtn     = document.getElementById('fpSubmitBtn');
  var resendBtn     = document.getElementById('fpResendBtn');
  var resendDisplay = document.getElementById('fpResendCooldown');
  if (!otpDisplay) return;

  var email      = wrap ? (wrap.dataset.email || 'default') : 'default';
  var otpKey     = 'otp_deadline_' + email;
  var coolKey    = 'otp_cooldown_' + email;
  var secondsRaw = wrap ? parseInt(wrap.dataset.seconds, 10) : 300;
  if (isNaN(secondsRaw) || secondsRaw < 0) secondsRaw = 0;

  // ===== OTP COUNTDOWN =====
  var otpDeadline;
  var skipExpiredMsg = params.get('error') === 'alreadyUsed';

  if (params.get('error') === 'alreadyUsed') {
    // Xóa storage cũ
    Object.keys(sessionStorage).forEach(function (k) {
      if (k.startsWith('otp_deadline_')) sessionStorage.removeItem(k);
      if (k.startsWith('otp_cooldown_')) sessionStorage.removeItem(k);
    });
    sessionStorage.setItem('otp_cooldown_skip_init', '1');

    // Set deadline = now → remaining = 0 → timer 00:00 ngay
    otpDeadline = Date.now();

  } else {
    var storedOtp = sessionStorage.getItem(otpKey);
    if (storedOtp) {
      // F5: dùng lại deadline cũ
      otpDeadline = parseInt(storedOtp, 10);
    } else {
      // Lần đầu vào trang: tạo deadline mới từ server
      otpDeadline = Date.now() + secondsRaw * 1000;
      if (secondsRaw > 0) {
        sessionStorage.setItem(otpKey, otpDeadline.toString());
      }
    }
  }

  function otpTick() {
    var remaining = Math.max(0, Math.floor((otpDeadline - Date.now()) / 1000));
    var m = Math.floor(remaining / 60);
    var s = remaining % 60;
    otpDisplay.textContent =
        (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;

    if (remaining <= 0) {
      otpDisplay.style.color = '#e74c3c';
      if (submitBtn) { submitBtn.disabled = true; submitBtn.style.opacity = '0.5'; }

      // Chỉ hiện thông báo khi KHÔNG phải trường hợp alreadyUsed
      if (!skipExpiredMsg && !document.getElementById('fpOtpExpiredMsg')) {
        var msg = document.createElement('p');
        msg.id = 'fpOtpExpiredMsg';
        msg.className = 'fp-error-box';
        msg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i>'
            + '<span>Mã OTP đã hết hạn. Vui lòng nhấn gửi lại để nhận mã mới.</span>';
        var sentTo = document.querySelector('.fp-sent-to');
        if (sentTo) sentTo.parentNode.insertBefore(msg, sentTo);
      }
      return;
    }
    setTimeout(otpTick, 500);
  }

  otpTick();

  document.addEventListener('visibilitychange', function () {
    if (!document.hidden) otpTick();
  });

  // ===== COOLDOWN NÚT GỬI LẠI =====
  var skipInit   = sessionStorage.getItem('otp_cooldown_skip_init');
  var coolDeadline;
  var storedCool = sessionStorage.getItem(coolKey);

  if (skipInit) {
    // alreadyUsed → mở nút gửi lại ngay
    sessionStorage.removeItem('otp_cooldown_skip_init');
    coolDeadline = Date.now();
  } else if (storedCool && parseInt(storedCool, 10) > Date.now()) {
    // Còn cooldown: dùng lại
    coolDeadline = parseInt(storedCool, 10);
  } else if (!storedCool) {
    // Chưa có → tạo cooldown 30s lần đầu
    coolDeadline = Date.now() + 30_000;
    sessionStorage.setItem(coolKey, coolDeadline.toString());
  } else {
    // Đã hết hạn trong storage → mở ngay
    coolDeadline = Date.now();
  }

  function coolTick() {
    var remaining = Math.max(0, Math.floor((coolDeadline - Date.now()) / 1000));
    if (resendDisplay) {
      resendDisplay.textContent = remaining > 0 ? '(' + remaining + 's)' : '';
    }
    if (remaining <= 0) {
      if (resendBtn) resendBtn.disabled = false;
      sessionStorage.removeItem(coolKey);
      return;
    }
    setTimeout(coolTick, 500);
  }

  coolTick();
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
      else               { li.classList.remove('ok'); }
    });
    var bar = document.getElementById('fpStrengthBar');
    var txt = document.getElementById('fpStrengthTxt');
    if (bar) { bar.style.width = (score * 20) + '%'; bar.style.background = colors[score] || '#eee'; }
    if (txt) { txt.textContent = labels[score] || ''; txt.style.color = colors[score] || '#aaa'; }
  });

  var form3 = newPwInput.closest('form');
  if (form3) {
    form3.addEventListener('submit', function (e) {
      var pw        = newPwInput.value;
      var confirmPw = document.getElementById('fpConfirmPw');
      if (!pw) {
        e.preventDefault();
        showInlineError('Vui lòng nhập mật khẩu mới.');
        return;
      }
      if (!rules.every(function (r) { return r.test(pw); })) {
        e.preventDefault();
        showInlineError('Mật khẩu chưa đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.');
        return;
      }
      if (confirmPw && pw !== confirmPw.value) {
        e.preventDefault();
        showInlineError('Mật khẩu xác nhận không khớp.');
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