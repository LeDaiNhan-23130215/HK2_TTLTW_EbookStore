// ── Đọc số thành chữ tiếng Việt ───────────────────────────────────────
const CH_DV = ['', 'một', 'hai', 'ba', 'bốn', 'năm', 'sáu', 'bảy', 'tám', 'chín'];
const CH_CHC = ['', 'mười', 'hai mươi', 'ba mươi', 'bốn mươi', 'năm mươi',
    'sáu mươi', 'bảy mươi', 'tám mươi', 'chín mươi'];

function readHundred(n) {
    const h = Math.floor(n / 100);
    const t = Math.floor((n % 100) / 10);
    const u = n % 10;
    let s = '';

    if (h > 0) s += CH_DV[h] + ' trăm';

    if (t === 0 && u === 0) return s.trim();

    if (t === 0 && u > 0) {
        s += (h > 0 ? ' linh ' : '') + CH_DV[u];
        return s.trim();
    }

    s += (s ? ' ' : '') + CH_CHC[t];

    if (u === 1 && t > 1) s += ' mốt';
    else if (u === 5 && t > 0) s += ' lăm';
    else if (u > 0) s += ' ' + CH_DV[u];

    return s.trim();
}

function numberToWords(n) {
    n = Math.floor(n);
    if (n === 0) return 'không';

    const units = [
        { div: 1_000_000_000, label: 'tỷ' },
        { div: 1_000_000, label: 'triệu' },
        { div: 1_000, label: 'nghìn' },
        { div: 1, label: '' }
    ];

    let parts = [];

    for (const u of units) {
        const q = Math.floor(n / u.div);
        if (q > 0) {
            parts.push(readHundred(q) + (u.label ? ' ' + u.label : ''));
            n %= u.div;
        }
    }

    const result = parts.join(' ').trim();
    return result.charAt(0).toUpperCase() + result.slice(1) + ' đồng';
}

// ── Nút +/- cho các ô nhập số ──
function stepValue(inputId, dir) {
    const input = document.getElementById(inputId);
    if (!input) return;

    let step = 1;
    if (inputId === 'discountValue') {
        const type = document.getElementById('discountType').value;
        step = type === 'PERCENT' ? 10 : 50000;
    } else if (inputId === 'minOrderValue' || inputId === 'maxDiscount') {
        step = 50000;
    }

    const current = parseFloat(input.value);
    const base = isNaN(current) || current < 0 ? 0 : current;
    let next = base + dir * step;

    if (next < 0) next = 0;
    if (inputId === 'discountValue' && document.getElementById('discountType').value === 'PERCENT') {
        next = Math.min(100, next);
    }

    input.value = next;
    input.dispatchEvent(new Event('input', { bubbles: true }));
}

// ── Hiện chữ đọc số tiền bên dưới ô nhập ──
function updateAmountWordsFor(inputId, wordsId) {
    const input = document.getElementById(inputId);
    const words = document.getElementById(wordsId);
    if (!input || !words) return;

    const val = parseFloat(input.value);
    if (isNaN(val) || val <= 0) {
        words.style.display = 'none';
        return;
    }

    if (inputId === 'discountValue') {
        const type = document.getElementById('discountType').value;
        if (type === 'PERCENT') {
            const intPart = Math.floor(val);
            const decPart = Math.round((val - intPart) * 100);
            let text = numberToWords(intPart).replace(' đồng', '');
            if (decPart > 0) {
                const decWords = numberToWords(decPart).replace(' đồng', '');
                text += ' phẩy ' + decWords.charAt(0).toLowerCase() + decWords.slice(1) + ' phần trăm';
            } else {
                text += ' phần trăm';
            }
            words.textContent = text.charAt(0).toUpperCase() + text.slice(1);
            words.style.display = 'block';
            return;
        }
    }

    words.textContent = numberToWords(val);
    words.style.display = 'block';
}

// ── Khởi tạo lựa chọn ban đầu cho combo khi sửa mã giảm giá ──
function initEditCombos(data) {
    // Số lần sử dụng / user
    const maxUsesPreset = document.getElementById('maxUsesPreset');
    const maxUsesInput  = document.getElementById('maxUsesPerUser');
    const maxUsesWrap   = document.getElementById('maxUsesCustomWrap');

    if (data.maxUsesPerUser === null) {
        maxUsesPreset.value = 'unlimited';
    } else if (data.maxUsesPerUser >= 1 && data.maxUsesPerUser <= 10) {
        maxUsesPreset.value = String(data.maxUsesPerUser);
    } else {
        maxUsesPreset.value = 'custom';
        maxUsesInput.value = data.maxUsesPerUser;
        maxUsesWrap.classList.add('show');
        maxUsesInput.setAttribute('required', 'required');
    }

    // Số lượng tối đa
    const qtyPreset = document.getElementById('quantityPreset');
    const qtyInput  = document.getElementById('quantity');
    const qtyWrap   = document.getElementById('quantityCustomWrap');

    if (data.quantity === -1) {
        qtyPreset.value = 'unlimited';
    } else if (data.quantity >= 10 && data.quantity <= 100 && data.quantity % 10 === 0) {
        qtyPreset.value = String(data.quantity);
    } else {
        qtyPreset.value = 'custom';
        qtyInput.value = data.quantity;
        qtyWrap.classList.add('show');
        qtyInput.setAttribute('required', 'required');
    }
}

document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.body.getAttribute('data-context') || '';

    // ── Generic: combo box có lựa chọn "Khác" hiện input tuỳ chỉnh ──
    function setupComboWithCustom(selectId, customWrapId, customInputId) {
        const select = document.getElementById(selectId);
        const wrap   = document.getElementById(customWrapId);
        const input  = document.getElementById(customInputId);
        if (!select || !wrap || !input) return;

        function toggle() {
            if (select.value === 'custom') {
                wrap.classList.add('show');
                input.setAttribute('required', 'required');
            } else {
                wrap.classList.remove('show');
                input.removeAttribute('required');
            }
        }

        select.addEventListener('change', toggle);
        toggle();
    }

    setupComboWithCustom('discountValuePreset', 'discountValueCustomWrap', 'discountValue');
    setupComboWithCustom('maxUsesPreset', 'maxUsesCustomWrap', 'maxUsesPerUser');
    setupComboWithCustom('quantityPreset', 'quantityCustomWrap', 'quantity');

    // ── Đổi loại giảm giá → cập nhật placeholder & danh sách mức giảm preset ──
    const discountType   = document.getElementById('discountType');
    const presetSelect   = document.getElementById('discountValuePreset');
    const customInput    = document.getElementById('discountValue');

    const PERCENT_PRESETS = [5, 10, 15, 20, 25, 30, 50];
    const FIXED_PRESETS   = [50000, 100000, 150000, 200000, 250000, 300000, 400000, 500000];

    function formatVND(n) {
        return n.toLocaleString('vi-VN');
    }

    function rebuildPresetOptions(selectedValue) {
        if (!presetSelect) return;
        const isPercent = discountType.value === 'PERCENT';
        const presets = isPercent ? PERCENT_PRESETS : FIXED_PRESETS;

        presetSelect.innerHTML = '';
        presets.forEach(p => {
            const opt = document.createElement('option');
            opt.value = String(p);
            opt.textContent = isPercent ? (p + '%') : (formatVND(p) + ' ₫');
            presetSelect.appendChild(opt);
        });
        const customOpt = document.createElement('option');
        customOpt.value = 'custom';
        customOpt.textContent = 'Mức khác...';
        presetSelect.appendChild(customOpt);

        // Xác định lựa chọn ban đầu dựa trên giá trị hiện có
        if (selectedValue !== undefined && selectedValue !== null && selectedValue !== '') {
            const matched = presets.find(p => String(p) === String(selectedValue));
            if (matched !== undefined) {
                presetSelect.value = String(matched);
                customInput.value = matched;
                document.getElementById('discountValueCustomWrap').classList.remove('show');
                customInput.removeAttribute('required');
            } else {
                presetSelect.value = 'custom';
                customInput.value = selectedValue;
                document.getElementById('discountValueCustomWrap').classList.add('show');
                customInput.setAttribute('required', 'required');
            }
        } else {
            presetSelect.selectedIndex = 0;
            customInput.value = presets[0];
        }

        customInput.placeholder = isPercent ? 'VD: 20 (giảm 20%)' : 'VD: 50000 (giảm 50.000₫)';
        customInput.max = isPercent ? '100' : '';
    }

    if (discountType && presetSelect && customInput) {
        const initialValue = customInput.getAttribute('data-initial-value') || '';
        rebuildPresetOptions(initialValue);

        discountType.addEventListener('change', function () {
            rebuildPresetOptions('');
        });

        presetSelect.addEventListener('change', function () {
            if (presetSelect.value !== 'custom') {
                customInput.value = presetSelect.value;
                document.getElementById('discountValueCustomWrap').classList.remove('show');
                customInput.removeAttribute('required');
            } else {
                document.getElementById('discountValueCustomWrap').classList.add('show');
                customInput.setAttribute('required', 'required');
                customInput.focus();
            }
            updateAmountWordsFor('discountValue', 'discountValueWords');
        });
    }

    // ── Hiện chữ đọc số tiền cho các ô tiền/% ──
    const moneyFields = [
        ['discountValue', 'discountValueWords'],
        ['minOrderValue', 'minOrderValueWords'],
        ['maxDiscount', 'maxDiscountWords']
    ];
    moneyFields.forEach(([inputId, wordsId]) => {
        const input = document.getElementById(inputId);
        if (!input) return;
        input.addEventListener('input', () => updateAmountWordsFor(inputId, wordsId));
        updateAmountWordsFor(inputId, wordsId);
    });
    if (discountType) {
        discountType.addEventListener('change', () => updateAmountWordsFor('discountValue', 'discountValueWords'));
    }

    // ── Nút tạo mã tự động ──
    const generateBtn = document.getElementById('btnGenerateCode');
    const codeInput   = document.getElementById('code');
    if (generateBtn && codeInput) {
        generateBtn.addEventListener('click', function () {
            generateBtn.disabled = true;
            generateBtn.textContent = 'Đang tạo...';
            fetch(ctx + '/admin-voucher?action=generateCode')
                .then(r => r.text())
                .then(code => {
                    codeInput.value = code.trim();
                })
                .catch(() => {
                    alert('Không thể tạo mã tự động, vui lòng thử lại.');
                })
                .finally(() => {
                    generateBtn.disabled = false;
                    generateBtn.textContent = 'Tạo mã tự động';
                });
        });
    }

    // ── Đồng bộ giá trị combo (maxUses / quantity) vào input thực khi submit ──
    function syncComboValue(presetId, inputId) {
        const preset = document.getElementById(presetId);
        const input  = document.getElementById(inputId);
        if (!preset || !input) return;

        if (preset.value === 'unlimited') {
            input.value = 'unlimited';
        } else if (preset.value !== 'custom') {
            input.value = preset.value;
        }
        // nếu 'custom', giữ nguyên giá trị người dùng đã nhập trong input
    }

    // ── Validate trước khi submit ──
    const form = document.getElementById('voucherForm');
    if (form) {
        form.addEventListener('submit', function (e) {
            syncComboValue('maxUsesPreset', 'maxUsesPerUser');
            syncComboValue('quantityPreset', 'quantity');

            const startInput = document.getElementById('startedAt');
            const endInput   = document.getElementById('expiredAt');

            if (!endInput.value) {
                alert('Vui lòng chọn thời điểm kết thúc của mã giảm giá.');
                e.preventDefault();
                return;
            }

            if (startInput.value && endInput.value && startInput.value >= endInput.value) {
                alert('Thời điểm kết thúc phải sau thời điểm bắt đầu.');
                e.preventDefault();
                return;
            }

            const valRaw = customInput.value;
            const val = parseFloat(valRaw);
            if (!valRaw || isNaN(val) || val <= 0) {
                alert('Mức giảm giá phải lớn hơn 0.');
                e.preventDefault();
                return;
            }
            if (discountType.value === 'PERCENT' && val > 100) {
                alert('Mức giảm phần trăm không được vượt quá 100%.');
                e.preventDefault();
                return;
            }

            const code = codeInput.value.trim();
            if (code && !/^[A-Za-z0-9]{1,50}$/.test(code)) {
                alert('Mã giảm giá chỉ gồm chữ và số, tối đa 50 ký tự.');
                e.preventDefault();
                return;
            }

            const maxUsesPresetVal = document.getElementById('maxUsesPreset').value;
            const maxUsesVal = document.getElementById('maxUsesPerUser').value;
            if (maxUsesPresetVal === 'custom' && (!/^\d+$/.test(maxUsesVal) || parseInt(maxUsesVal, 10) < 1)) {
                alert('Số lần sử dụng / 1 người dùng phải là số nguyên dương.');
                e.preventDefault();
                return;
            }

            const qtyPresetVal = document.getElementById('quantityPreset').value;
            const qtyVal = document.getElementById('quantity').value;
            if (qtyPresetVal === 'custom' && (!/^\d+$/.test(qtyVal) || parseInt(qtyVal, 10) < 1)) {
                alert('Số lượng sử dụng tối đa phải là số nguyên dương.');
                e.preventDefault();
                return;
            }
        });
    }
});


