// ===== VALIDATE EMAIL (Step 1) =====
const EMAIL_REGEX = /^[a-zA-Z0-9]([a-zA-Z0-9._%+\-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\-]*[a-zA-Z0-9])?(\.[a-zA-Z]{2,})+$/;

const emailInput = document.querySelector('input[name="email"]');
if (emailInput) {
  const errEl = document.createElement('span');
  errEl.style.cssText = 'color:red;font-size:12px;display:block;margin-top:4px';
  emailInput.parentElement.appendChild(errEl);

  emailInput.addEventListener('blur', () => {
    const v = emailInput.value.trim();
    errEl.textContent = !v ? 'Vui lòng nhập email.'
        : !EMAIL_REGEX.test(v) ? 'Email không đúng định dạng. Ví dụ: abc@gmail.com'
            : '';
  });
  emailInput.closest('form').addEventListener('submit', e => {
    const v = emailInput.value.trim();
    if (!v || !EMAIL_REGEX.test(v)) {
      errEl.textContent = !v ? 'Vui lòng nhập email.' : 'Email không đúng định dạng.';
      e.preventDefault();
    }
  });
}

// ===== TOGGLE EYE (Step 3) =====
document.querySelectorAll('.fp-eye').forEach(function(icon) {
  icon.addEventListener('click', function() {
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

  // --- Định nghĩa các điều kiện ---
  var rules = [
    { id: 'ck-len',   test: function(pw){ return pw.length >= 10; } },
    { id: 'ck-upper', test: function(pw){ return /[A-Z]/.test(pw); } },
    { id: 'ck-lower', test: function(pw){ return /[a-z]/.test(pw); } },
    { id: 'ck-digit', test: function(pw){ return /[0-9]/.test(pw); } },
    { id: 'ck-spec',  test: function(pw){ return /[^A-Za-z0-9]/.test(pw); } }
  ];

  var colors = ['', '#e74c3c', '#e67e22', '#f1c40f', '#2ecc71', '#27ae60'];
  var labels = ['', 'Rất yếu', 'Yếu', 'Trung bình', 'Mạnh', 'Rất mạnh'];

  newPwInput.addEventListener('input', function() {
    var pw = this.value;

    // Cập nhật checklist
    var score = 0;
    rules.forEach(function(rule) {
      var li = document.getElementById(rule.id);
      if (!li) return;
      if (rule.test(pw)) {
        li.classList.add('ok');
        score++;
      } else {
        li.classList.remove('ok');
      }
    });

    // Cập nhật thanh strength
    var bar = document.getElementById('fpStrengthBar');
    var txt = document.getElementById('fpStrengthTxt');
    if (bar) {
      bar.style.width = (score * 20) + '%';
      bar.style.background = colors[score] || '#eee';
    }
    if (txt) {
      txt.textContent = labels[score] || '';
      txt.style.color = colors[score] || '#aaa';
    }
  });

  // --- Chặn submit nếu không đủ điều kiện ---
  var form = newPwInput.closest('form');
  if (form) {
    form.addEventListener('submit', function(e) {
      var pw = newPwInput.value;
      var confirmPw = document.getElementById('fpConfirmPw');

      // Chưa nhập mật khẩu mới
      if (!pw) {
        e.preventDefault();
        // Redirect về server để hiện lỗi — hoặc hiện inline
        showInlineError('Vui lòng nhập mật khẩu mới.');
        return;
      }

      // Chưa đủ tất cả 5 điều kiện
      var allPassed = rules.every(function(rule){ return rule.test(pw); });
      if (!allPassed) {
        e.preventDefault();
        showInlineError('Mật khẩu chưa đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.');
        return;
      }

      // Không khớp
      if (confirmPw && pw !== confirmPw.value) {
        e.preventDefault();
        showInlineError('Mật khẩu xác nhận không khớp.');
      }
    });
  }
}

function showInlineError(msg) {
  // Hiện lỗi ngay tại trang, không reload
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
    // Chèn trước .password-input
    var pwInput = document.querySelector('.password-input');
    if (pwInput) pwInput.parentElement.insertBefore(existing, pwInput);
  }
  var span = existing.querySelector('.fp-err-text');
  if (span) span.textContent = msg;
  existing.style.display = 'flex';
}