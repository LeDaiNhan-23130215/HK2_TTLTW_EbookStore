const SU_EMAIL_REGEX = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,}$/;
const SU_PHONE_REGEX = /^0\d{9}$/;

function suShowBoxErr(id, msg) {
    var el = document.getElementById(id);
    var txt = document.getElementById(id + 'Txt');
    if (!el) return;
    if (msg) { if (txt) txt.textContent = msg; el.style.display = 'flex'; }
    else       { el.style.display = 'none'; }
}

function suShowFieldErr(errId, msg) {
    var el = document.getElementById(errId);
    if (el) el.textContent = msg || '';
}

(function () {
    var form = document.getElementById('suForm');
    if (!form) return;
    var fnameInput  = document.getElementById('fname');
    var emailInput  = document.getElementById('userAndEmail');
    var phoneInput  = document.getElementById('phoneNumber');
    if (!fnameInput && !emailInput) return;

    [
        { input: 'fname',        err: 'err-fname' },
        { input: 'userAndEmail', err: 'err-email' },
        { input: 'phoneNumber',  err: 'err-phone' }
    ].forEach(function (pair) {
        var el = document.getElementById(pair.input);
        if (!el) return;
        el.addEventListener('input', function () {
            suShowFieldErr(pair.err, '');
            suShowBoxErr('suStep1Err', '');
        });
        el.addEventListener('blur', function () {
            suValidateStep1Field(pair.input);
        });
    });

    function suValidateStep1Field(id) {
        var el = document.getElementById(id);
        var value = el ? el.value.trim() : '';
        if (id === 'fname') {
            if (!value)           { suShowFieldErr('err-fname', 'Vui lòng nhập tên người dùng.'); return false; }
            if (value.length < 3) { suShowFieldErr('err-fname', 'Tên phải có ít nhất 3 ký tự.'); return false; }
            suShowFieldErr('err-fname', '');
            return true;
        }
        if (id === 'userAndEmail') {
            if (!value)                      { suShowFieldErr('err-email', 'Vui lòng nhập email.'); return false; }
            if (!SU_EMAIL_REGEX.test(value)) { suShowFieldErr('err-email', 'Email không hợp lệ (vd: ten@example.com).'); return false; }
            suShowFieldErr('err-email', '');
            return true;
        }
        if (id === 'phoneNumber') {
            if (value && !SU_PHONE_REGEX.test(value)) {
                suShowFieldErr('err-phone', 'Số điện thoại không hợp lệ (10 số, bắt đầu 0).');
                return false;
            }
            suShowFieldErr('err-phone', '');
            return true;
        }
        return true;
    }

    form.addEventListener('submit', function (e) {
        var btn = e.submitter;
        if (!btn || btn.value !== 'sendInfo') return;

        var valid = true;
        ['fname', 'userAndEmail', 'phoneNumber'].forEach(function (id) {
            if (!suValidateStep1Field(id)) valid = false;
        });
        if (!valid) e.preventDefault();
    });
})();

(function () {
    var params = new URL(window.location.href).searchParams;

    if (params.get('resent') === 'true') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('su_otp_deadline_') || k.startsWith('su_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('su_otp_cooldown_skip_init', '1');
    }

    if (params.get('error') === 'alreadyUsed') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('su_otp_deadline_') || k.startsWith('su_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('su_otp_cooldown_skip_init', '1');
    }

    document.addEventListener('DOMContentLoaded', function () {
        var wrap          = document.getElementById('suCountdownWrap');
        var otpDisplay    = document.getElementById('suCountdown');
        var submitBtn     = document.getElementById('suSubmitBtn');
        var resendBtn     = document.getElementById('suResendBtn');
        var resendDisplay = document.getElementById('suResendCooldown');
        if (!otpDisplay) return;

        var email      = wrap ? (wrap.dataset.email || 'default') : 'default';
        var otpKey     = 'su_otp_deadline_' + email;
        var coolKey    = 'su_otp_cooldown_' + email;
        var secondsRaw = wrap ? parseInt(wrap.dataset.seconds, 10) : 300;
        if (isNaN(secondsRaw) || secondsRaw < 0) secondsRaw = 0;

        var otpDeadline;
        var skipExpiredMsg = params.get('error') === 'alreadyUsed';

        if (params.get('error') === 'alreadyUsed') {
            sessionStorage.removeItem(otpKey);
            otpDeadline = Date.now();
        } else {
            var stored = sessionStorage.getItem(otpKey);
            if (stored) {
                otpDeadline = parseInt(stored, 10);
            } else {
                otpDeadline = Date.now() + secondsRaw * 1000;
                sessionStorage.setItem(otpKey, String(otpDeadline));
            }
        }

        function fmt(s) {
            var m = Math.floor(s / 60), sec = s % 60;
            return (m < 10 ? '0' : '') + m + ':' + (sec < 10 ? '0' : '') + sec;
        }

        function tickOtp() {
            var remaining = Math.max(0, Math.round((otpDeadline - Date.now()) / 1000));
            otpDisplay.textContent = fmt(remaining);

            if (remaining <= 0) {
                clearInterval(otpTimer);
                otpDisplay.textContent = '00:00';

                if (!skipExpiredMsg) {
                    if (submitBtn) { submitBtn.disabled = true; submitBtn.style.opacity = '0.5'; }
                    if (!document.getElementById('suOtpExpiredMsg')) {
                        var msg = document.createElement('p');
                        msg.id = 'suOtpExpiredMsg';
                        msg.className = 'su-error-box';
                        msg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i>'
                            + '<span>Mã OTP đã hết hạn. Vui lòng nhấn gửi lại để nhận mã mới.</span>';
                        var sentTo = document.querySelector('.su-sent-to');
                        if (sentTo) sentTo.parentNode.insertBefore(msg, sentTo);
                    }
                }
            }
        }

        tickOtp();
        var otpTimer = setInterval(tickOtp, 1000);

        var COOLDOWN = 30;

        var skipInit   = sessionStorage.getItem('su_otp_cooldown_skip_init');
        var storedCool = sessionStorage.getItem(coolKey);
        var coolDeadline;

        if (skipInit) {
            sessionStorage.removeItem('su_otp_cooldown_skip_init');
            sessionStorage.removeItem(coolKey);
            coolDeadline = Date.now();
        } else if (storedCool && parseInt(storedCool, 10) > Date.now()) {
            coolDeadline = parseInt(storedCool, 10);
        } else if (!storedCool) {
            coolDeadline = Date.now() + COOLDOWN * 1000;
            sessionStorage.setItem(coolKey, coolDeadline.toString());
        } else {
            sessionStorage.removeItem(coolKey);
            coolDeadline = Date.now();
        }

        function tickCool() {
            var rem = Math.max(0, Math.round((coolDeadline - Date.now()) / 1000));
            if (resendDisplay) resendDisplay.textContent = rem > 0 ? '(' + rem + 's)' : '';

            if (resendBtn) {
                resendBtn.disabled = rem > 0;
                if (rem <= 0) {
                    clearInterval(coolTimer);
                    sessionStorage.removeItem(coolKey);
                }
            }
        }

        tickCool();
        var coolTimer = setInterval(tickCool, 1000);

        var suOtpInput = document.getElementById('suOtpInput');
        if (suOtpInput && submitBtn) {
            submitBtn.closest('form') && submitBtn.closest('form').addEventListener('submit', function (e) {
                if (e.submitter && e.submitter.value !== 'verifyCode') return;
                var v = suOtpInput.value.trim();
                if (!v || !/^\d{6}$/.test(v)) {
                    e.preventDefault();
                    suOtpInput.focus();
                }
            });
        }
    });
})();

(function () {
    document.addEventListener('DOMContentLoaded', function () {
        var newPwInput = document.getElementById('suNewPw');
        if (!newPwInput) return;

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
            var bar = document.getElementById('suStrengthBar');
            var txt = document.getElementById('suStrengthTxt');
            if (bar) { bar.style.width = (score * 20) + '%'; bar.style.background = colors[score] || '#eee'; }
            if (txt) { txt.textContent = labels[score] || ''; txt.style.color = colors[score] || '#aaa'; }
        });

        var form3 = newPwInput.closest('form');
        if (form3) {
            form3.addEventListener('submit', function (e) {
                if (!e.submitter || e.submitter.value !== 'createPassword') return;

                var pw = newPwInput.value;
                var confirmPw = document.getElementById('suConfirmPw');

                if (!pw) {
                    e.preventDefault();
                    suShowInlineErr('Vui lòng nhập mật khẩu mới.');
                    return;
                }
                if (!rules.every(function (r) { return r.test(pw); })) {
                    e.preventDefault();
                    suShowInlineErr('Mật khẩu chưa đủ mạnh. Vui lòng kiểm tra các yêu cầu bên dưới.');
                    return;
                }
                if (confirmPw && pw !== confirmPw.value) {
                    e.preventDefault();
                    suShowInlineErr('Mật khẩu xác nhận không khớp.');
                }
            });
        }
    });

    function suShowInlineErr(msg) {
        var existing = document.querySelector('.su-error-box');
        if (!existing) {
            existing = document.createElement('p');
            existing.className = 'su-error-box';
            var icon = document.createElement('i');
            icon.className = 'fa-solid fa-circle-exclamation';
            existing.appendChild(icon);
            existing.appendChild(document.createTextNode(' '));
            var msgSpan = document.createElement('span');
            existing.appendChild(msgSpan);
            var pwInput = document.querySelector('.su-password-input');
            if (pwInput) pwInput.parentElement.insertBefore(existing, pwInput);
        }
        var span = existing.querySelector('span');
        if (span) span.textContent = msg;
        existing.style.display = 'flex';
    }
})();

document.querySelectorAll('.su-eye').forEach(function (icon) {
    icon.addEventListener('click', function () {
        var input = document.getElementById(this.dataset.target);
        if (!input) return;
        var isPassword = input.type === 'password';
        input.type = isPassword ? 'text' : 'password';
        this.classList.toggle('fa-eye', !isPassword);
        this.classList.toggle('fa-eye-slash', isPassword);
    });
});