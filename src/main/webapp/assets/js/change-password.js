(function () {
    var params = new URL(window.location.href).searchParams;

    if (params.get('resent') === 'true') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('cp_otp_deadline_') || k.startsWith('cp_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('cp_otp_cooldown_skip_init', '1');
    }

    if (params.get('error') === 'alreadyUsed') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('cp_otp_deadline_') || k.startsWith('cp_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('cp_otp_cooldown_skip_init', '1');
    }
})();

// ===== STEP VERIFY: ĐẾM NGƯỢC OTP + COOLDOWN NÚT GỬI LẠI =====
document.addEventListener('DOMContentLoaded', function () {
    var params = new URL(window.location.href).searchParams;

    var wrap          = document.getElementById('cpCountdownWrap');
    var otpDisplay    = document.getElementById('cpCountdown');
    var submitBtn     = document.getElementById('cpSubmitBtn');
    var resendBtn     = document.getElementById('cpResendBtn');
    var resendDisplay = document.getElementById('cpResendCooldown');

    if (!otpDisplay) return;

    var email      = wrap ? (wrap.dataset.email || 'default') : 'default';
    var otpKey     = 'cp_otp_deadline_' + email;
    var coolKey    = 'cp_otp_cooldown_' + email;
    var secondsRaw = wrap ? parseInt(wrap.dataset.seconds, 10) : 300;
    if (isNaN(secondsRaw) || secondsRaw < 0) secondsRaw = 0;

    // ===== OTP COUNTDOWN =====
    var otpDeadline;
    var skipExpiredMsg = params.get('error') === 'alreadyUsed';

    if (params.get('error') === 'alreadyUsed') {
        sessionStorage.removeItem(otpKey);
        otpDeadline = Date.now();
    } else {
        var storedOtp = sessionStorage.getItem(otpKey);
        if (storedOtp) {
            otpDeadline = parseInt(storedOtp, 10);
        } else {
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
        otpDisplay.textContent = (m < 10 ? '0' : '') + m + ':' + (s < 10 ? '0' : '') + s;

        if (remaining <= 0) {
            otpDisplay.style.color = '#e74c3c';
            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.style.opacity = '0.5';
            }

            // Hiện thông báo hết hạn (trừ trường hợp alreadyUsed)
            if (!skipExpiredMsg && !document.getElementById('cpOtpExpiredMsg')) {
                var msg = document.createElement('p');
                msg.id = 'cpOtpExpiredMsg';
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
    var skipInit   = sessionStorage.getItem('cp_otp_cooldown_skip_init');
    var coolDeadline;
    var storedCool = sessionStorage.getItem(coolKey);

    if (skipInit) {
        sessionStorage.removeItem('cp_otp_cooldown_skip_init');
        sessionStorage.removeItem(coolKey);
        coolDeadline = Date.now();
    } else if (storedCool && parseInt(storedCool, 10) > Date.now()) {
        coolDeadline = parseInt(storedCool, 10);
    } else if (!storedCool) {
        coolDeadline = Date.now() + 30_000;
        sessionStorage.setItem(coolKey, coolDeadline.toString());
    } else {
        sessionStorage.removeItem(coolKey);
        coolDeadline = Date.now();
    }

    function coolTick() {
        var remaining = Math.max(0, Math.floor((coolDeadline - Date.now()) / 1000));
        if (resendDisplay) {
            resendDisplay.textContent = remaining > 0 ? '(' + remaining + 's)' : '';
        }

        if (resendBtn) {
            resendBtn.disabled = remaining > 0;
        }

        if (remaining <= 0) {
            sessionStorage.removeItem(coolKey);
            return;
        }

        setTimeout(coolTick, 500);
    }

    coolTick();

    // ===== VALIDATE OTP =====
    var cpOtpInput = document.getElementById('cpOtpInput');
    if (cpOtpInput && submitBtn) {
        submitBtn.closest('form') && submitBtn.closest('form').addEventListener('submit', function (e) {
            if (e.submitter && e.submitter.value !== 'verifyOtp') return;
            var v = cpOtpInput.value.trim();
            if (!v || !/^\d{6}$/.test(v)) {
                e.preventDefault();
                cpOtpInput.focus();
            }
        });
    }
});

// ===== TOGGLE EYE (Bước reset) =====
document.querySelectorAll('.fp-eye').forEach(function (icon) {
    icon.addEventListener('click', function () {
        var inp = document.getElementById(this.dataset.target);
        if (!inp) return;
        inp.type = inp.type === 'password' ? 'text' : 'password';
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
});

// ===== CHECKLIST + STRENGTH + VALIDATE (Bước reset) =====
var newPwInput = document.getElementById('cpNewPw');
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
        var bar = document.getElementById('cpStrengthBar');
        var txt = document.getElementById('cpStrengthTxt');
        if (bar) { bar.style.width = (score * 20) + '%'; bar.style.background = colors[score] || '#eee'; }
        if (txt) { txt.textContent = labels[score] || ''; txt.style.color = colors[score] || '#aaa'; }
    });

    var cpForm = document.getElementById('cpForm');
    if (cpForm) {
        cpForm.addEventListener('submit', function (e) {
            // Chỉ validate khi bấm đúng nút đổi mật khẩu / tạo mật khẩu
            var action = e.submitter;
            if (!action || action.value !== 'changePassword') return;

            var pw = newPwInput.value;
            var confirmPw = document.getElementById('cpConfirmPw');

            if (!pw) {
                e.preventDefault();
                showCpInlineError('Vui lòng nhập mật khẩu mới.');
                return;
            }
            if (!rules.every(function (r) { return r.test(pw); })) {
                e.preventDefault();
                showCpInlineError('Mật khẩu chưa đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.');
                return;
            }
            if (confirmPw && pw !== confirmPw.value) {
                e.preventDefault();
                showCpInlineError('Mật khẩu xác nhận không khớp.');
            }
        });
    }
}

function showCpInlineError(msg) {
    var existing = document.querySelector('.cp-inner .fp-error-box');
    if (!existing) {
        existing = document.createElement('p');
        existing.className = 'fp-error-box';
        var icon = document.createElement('i');
        icon.className = 'fa-solid fa-circle-exclamation';
        existing.appendChild(icon);
        existing.appendChild(document.createTextNode(' '));
        var msgSpan = document.createElement('span');
        msgSpan.className = 'cp-err-text';
        existing.appendChild(msgSpan);
        var pwDiv = document.querySelector('.password-input');
        if (pwDiv) pwDiv.parentElement.insertBefore(existing, pwDiv);
    }
    var span = existing.querySelector('.cp-err-text, span');
    if (span) span.textContent = msg;
    existing.style.display = 'flex';
}