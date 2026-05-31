const UE_EMAIL_REGEX = /^[a-zA-Z0-9]([a-zA-Z0-9._%+\-]*[a-zA-Z0-9])?@[a-zA-Z0-9]([a-zA-Z0-9\-]*[a-zA-Z0-9])?(\.[a-zA-Z]{2,})+$/;
const UE_USERNAME_REGEX = /^.{3,30}$/;
const UE_PHONE_REGEX = /^0\d{9}$/;

function uiToggleEdit(formId) {
    var form = document.getElementById(formId);
    if (!form) return;

    var hidden = form.style.display === 'none' || getComputedStyle(form).display === 'none';
    form.style.display = hidden ? 'block' : 'none';
}

function uiShowInlineError(msg, scopeSelector) {
    var scope = scopeSelector ? document.querySelector(scopeSelector) : document;
    var existing = scope ? scope.querySelector('.ui-error-box') : null;

    if (!existing) {
        existing = document.createElement('p');
        existing.className = 'ui-error-box';

        var icon = document.createElement('i');
        icon.className = 'fa-solid fa-circle-exclamation';
        existing.appendChild(icon);
        existing.appendChild(document.createTextNode(' '));

        var msgSpan = document.createElement('span');
        msgSpan.className = 'ui-err-text';
        existing.appendChild(msgSpan);

        var anchor =
            (scope && scope.querySelector('.ui-step-form')) ||
            (scope && scope.querySelector('.ui-edit-form')) ||
            (scope && scope.querySelector('.password-input')) ||
            (scope && scope.querySelector('.ui-step-actions'));

        if (anchor && anchor.parentElement) {
            anchor.parentElement.insertBefore(existing, anchor);
        } else if (scope) {
            scope.insertBefore(existing, scope.firstChild);
        }
    }

    var span = existing.querySelector('.ui-err-text');
    if (span) span.textContent = msg || '';
    existing.style.display = 'flex';
}

function uiClearInlineError(scopeSelector) {
    var scope = scopeSelector ? document.querySelector(scopeSelector) : document;
    var existing = scope ? scope.querySelector('.ui-error-box') : null;
    if (existing) existing.style.display = 'none';
}

document.querySelectorAll('.ue-eye').forEach(function (icon) {
    icon.addEventListener('click', function () {
        var input = document.getElementById(this.dataset.target);
        if (!input) return;

        var isPassword = input.type === 'password';
        input.type = isPassword ? 'text' : 'password';

        this.classList.toggle('fa-eye', !isPassword);
        this.classList.toggle('fa-eye-slash', isPassword);
    });
});

(function () {
    var params = new URL(window.location.href).searchParams;

    if (params.get('resent') === 'true') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('ue_otp_deadline_') || k.startsWith('ue_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('ue_otp_cooldown_skip_init', '1');
    }

    if (params.get('error') === 'alreadyUsed') {
        Object.keys(sessionStorage).forEach(function (k) {
            if (k.startsWith('ue_otp_deadline_') || k.startsWith('ue_otp_cooldown_')) {
                sessionStorage.removeItem(k);
            }
        });
        sessionStorage.setItem('ue_otp_cooldown_skip_init', '1');
    }

    document.addEventListener('DOMContentLoaded', function () {
        var wrap          = document.getElementById('ueCountdownWrap');
        var otpDisplay    = document.getElementById('ueCountdown');
        var submitBtn     = document.getElementById('ueSubmitBtn');
        var resendBtn     = document.getElementById('ueResendBtn');
        var resendDisplay = document.getElementById('ueResendCooldown');

        if (!wrap || !otpDisplay) return;

        var email      = wrap.dataset.email || 'default';
        var otpKey     = 'ue_otp_deadline_' + email;
        var coolKey    = 'ue_otp_cooldown_' + email;
        var secondsRaw = parseInt(wrap.dataset.seconds, 10);
        if (isNaN(secondsRaw) || secondsRaw < 0) secondsRaw = 300;

        var skipExpiredMsg = params.get('error') === 'alreadyUsed';

        // ===== OTP COUNTDOWN =====
        var otpDeadline;

        if (params.get('error') === 'alreadyUsed') {
            sessionStorage.removeItem(otpKey);
            otpDeadline = Date.now();
        } else {
            var storedOtp = sessionStorage.getItem(otpKey);
            if (storedOtp) {
                otpDeadline = parseInt(storedOtp, 10);
            } else {
                otpDeadline = Date.now() + secondsRaw * 1000;
                sessionStorage.setItem(otpKey, String(otpDeadline));
            }
        }

        function fmt(s) {
            var m = Math.floor(s / 60);
            var sec = s % 60;
            return (m < 10 ? '0' : '') + m + ':' + (sec < 10 ? '0' : '') + sec;
        }

        function tickOtp() {
            var remaining = Math.max(0, Math.floor((otpDeadline - Date.now()) / 1000));
            otpDisplay.textContent = fmt(remaining);

            if (remaining <= 0) {
                otpDisplay.style.color = '#e74c3c';

                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.style.opacity = '0.5';
                }

                if (!skipExpiredMsg && !document.getElementById('ueOtpExpiredMsg')) {
                    var msg = document.createElement('p');
                    msg.id = 'ueOtpExpiredMsg';
                    msg.className = 'ui-error-box';
                    msg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i>'
                        + '<span>Mã OTP đã hết hạn. Vui lòng nhấn gửi lại để nhận mã mới.</span>';

                    var sentTo = document.querySelector('.ui-sent-to');
                    if (!sentTo) sentTo = document.querySelector('.fp-sent-to');
                    if (sentTo) sentTo.parentNode.insertBefore(msg, sentTo);
                }

                return;
            }

            setTimeout(tickOtp, 500);
        }

        tickOtp();

        document.addEventListener('visibilitychange', function () {
            if (!document.hidden) tickOtp();
        });

        // ===== COOLDOWN NÚT GỬI LẠI =====
        var skipInit   = sessionStorage.getItem('ue_otp_cooldown_skip_init');
        var storedCool = sessionStorage.getItem(coolKey);
        var coolDeadline;
        var COOLDOWN = 30;

        if (skipInit) {
            sessionStorage.removeItem('ue_otp_cooldown_skip_init');
            sessionStorage.removeItem(coolKey);
            coolDeadline = Date.now();
        } else if (storedCool && parseInt(storedCool, 10) > Date.now()) {
            coolDeadline = parseInt(storedCool, 10);
        } else if (!storedCool) {
            coolDeadline = Date.now() + COOLDOWN * 1000;
            sessionStorage.setItem(coolKey, String(coolDeadline));
        } else {
            sessionStorage.removeItem(coolKey);
            coolDeadline = Date.now();
        }

        function tickCool() {
            var rem = Math.max(0, Math.floor((coolDeadline - Date.now()) / 1000));

            if (resendDisplay) {
                resendDisplay.textContent = rem > 0 ? '(' + rem + 's)' : '';
            }

            if (resendBtn) {
                resendBtn.disabled = rem > 0;
            }

            if (rem <= 0) {
                sessionStorage.removeItem(coolKey);
                return;
            }

            setTimeout(tickCool, 500);
        }

        tickCool();

        // ===== VALIDATE OTP SUBMIT =====
        var otpInput = document.getElementById('ueOtpInput');
        if (otpInput && submitBtn) {
            var otpForm = submitBtn.closest('form');
            if (otpForm) {
                otpForm.addEventListener('submit', function (e) {
                    if (e.submitter && e.submitter.value !== 'verifyEmailOtp') return;

                    var v = otpInput.value.trim();
                    if (!v || !/^\d{6}$/.test(v)) {
                        e.preventDefault();
                        otpInput.focus();
                    }
                });
            }
        }
    });
})();

// ===== VALIDATE INLINE EDIT FIELDS =====
(function () {
    function bindFieldValidation(inputSelector, errSelector, rule, message) {
        var input = document.querySelector(inputSelector);
        if (!input) return;

        var errBox = document.querySelector(errSelector);

        input.addEventListener('blur', function () {
            var v = input.value.trim();
            if (!v) {
                if (errBox) errBox.textContent = message.empty || 'Vui lòng nhập thông tin.';
                return;
            }
            if (!rule.test(v)) {
                if (errBox) errBox.textContent = message.invalid;
                return;
            }
            if (errBox) errBox.textContent = '';
        });

        input.addEventListener('input', function () {
            if (errBox) errBox.textContent = '';
        });
    }

    bindFieldValidation(
        'input[name="newUsername"]',
        '#ue-username-err',
        UE_USERNAME_REGEX,
        {
            empty: 'Vui lòng nhập tên người dùng.',
            invalid: 'Tên phải từ 3 đến 30 ký tự.'
        }
    );

    bindFieldValidation(
        'input[name="newPhone"]',
        '#ue-phone-err',
        UE_PHONE_REGEX,
        {
            empty: 'Vui lòng nhập số điện thoại.',
            invalid: 'Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0).'
        }
    );

    bindFieldValidation(
        'input[name="newEmail"]',
        '#ue-email-err',
        UE_EMAIL_REGEX,
        {
            empty: 'Vui lòng nhập email mới.',
            invalid: 'Email không đúng định dạng.'
        }
    );
})();

// ===== VALIDATE SUBMIT THEO ACTION =====
(function () {
    function getValue(form, selectors) {
        for (var i = 0; i < selectors.length; i++) {
            var el = form.querySelector(selectors[i]);
            if (el) return el;
        }
        return null;
    }

    document.addEventListener('submit', function (e) {
        var form = e.target;
        if (!(form instanceof HTMLFormElement)) return;

        var action = e.submitter ? e.submitter.value : '';
        if (!action) return;

        if (action === 'updateUsername') {
            var usernameInput = getValue(form, ['input[name="newUsername"]', '#ueNewUsername']);
            if (!usernameInput) return;

            var v = usernameInput.value.trim();
            if (!v) {
                e.preventDefault();
                uiShowInlineError('Vui lòng nhập tên người dùng.', '.ui-edit-form');
                usernameInput.focus();
                return;
            }
            if (!UE_USERNAME_REGEX.test(v)) {
                e.preventDefault();
                uiShowInlineError('Tên phải từ 3 đến 30 ký tự.', '.ui-edit-form');
                usernameInput.focus();
                return;
            }
        }

        if (action === 'updatePhone') {
            var phoneInput = getValue(form, ['input[name="newPhone"]', '#ueNewPhone']);
            if (!phoneInput) return;

            var v2 = phoneInput.value.trim();
            if (!v2) {
                e.preventDefault();
                uiShowInlineError('Vui lòng nhập số điện thoại.', '.ui-edit-form');
                phoneInput.focus();
                return;
            }
            if (!UE_PHONE_REGEX.test(v2)) {
                e.preventDefault();
                uiShowInlineError('Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0).', '.ui-edit-form');
                phoneInput.focus();
                return;
            }
        }

        if (action === 'verifyPassword') {
            var pwInput = getValue(form, ['input[name="password"]', '#uePassword']);
            if (!pwInput) return;

            var pw = pwInput.value.trim();
            if (!pw) {
                e.preventDefault();
                uiShowInlineError('Vui lòng nhập mật khẩu.', '.ui-step-form');
                pwInput.focus();
                return;
            }
        }

        if (action === 'submitNewEmail') {
            var emailInput = getValue(form, ['input[name="newEmail"]', '#ueNewEmail']);
            if (!emailInput) return;

            var em = emailInput.value.trim();
            if (!em) {
                e.preventDefault();
                uiShowInlineError('Vui lòng nhập email mới.', '.ui-step-form');
                emailInput.focus();
                return;
            }
            if (!UE_EMAIL_REGEX.test(em)) {
                e.preventDefault();
                uiShowInlineError('Email không đúng định dạng.', '.ui-step-form');
                emailInput.focus();
                return;
            }
        }

        if (action === 'verifyEmailOtp') {
            var otpInput = getValue(form, ['input[name="confirmCode"]', '#ueOtpInput']);
            if (!otpInput) return;

            var code = otpInput.value.trim();
            if (!code || !/^\d{6}$/.test(code)) {
                e.preventDefault();
                uiShowInlineError('Vui lòng nhập mã OTP 6 chữ số.', '.ui-step-form');
                otpInput.focus();
                return;
            }
        }
    }, true);
})();
// ===== CUSTOM MODAL: Huỷ liên kết Google =====
function uiShowUnlinkModal() {
    var modal = document.getElementById('uiUnlinkModal');
    if (modal) {
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    }
}

function uiHideUnlinkModal() {
    var modal = document.getElementById('uiUnlinkModal');
    if (modal) {
        modal.style.display = 'none';
        document.body.style.overflow = '';
    }
}

// Đóng modal khi click ra ngoài
document.addEventListener('DOMContentLoaded', function () {
    var modal = document.getElementById('uiUnlinkModal');
    if (modal) {
        modal.addEventListener('click', function (e) {
            if (e.target === modal) uiHideUnlinkModal();
        });
    }
});