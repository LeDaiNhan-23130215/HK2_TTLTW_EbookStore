function switchTab(tab) {
    ['ebook', 'category', 'author'].forEach(t => {
        const panel = document.getElementById('tab-' + t);
        if (panel) panel.classList.toggle('show', t === tab);
    });

    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', ['ebook', 'category', 'author'][i] === tab);
    });
}

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

function updateAmountWords() {
    const type = document.getElementById('discountType').value;
    const val = parseFloat(document.getElementById('discountValue').value);
    const words = document.getElementById('amountWords');

    if (isNaN(val) || val <= 0) {
        words.style.display = 'none';
        return;
    }

    if (type === 'FIXED') {
        words.textContent = numberToWords(val);
        words.style.display = 'block';
    } else if (type === 'PERCENT') {
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
    }
}

function updateValueHint() {
    const type = document.getElementById('discountType').value;
    const input = document.getElementById('discountValue');
    const hint = document.getElementById('valueHint');

    if (type === 'PERCENT') {
        input.placeholder = 'VD: 20 → giảm 20%';
        hint.textContent = 'Nhập từ 0.01 đến 100, dùng nút +/− để tăng/giảm 10%';
    } else {
        input.placeholder = 'VD: 50000 → giảm 50.000₫';
        hint.textContent = 'Nhập số tiền giảm, dùng nút +/− để tăng/giảm 50.000₫';
    }

    updateAmountWords();
}

function stepValue(dir) {
    const type = document.getElementById('discountType').value;
    const input = document.getElementById('discountValue');
    const step = type === 'PERCENT' ? 10 : 50000;
    const current = parseFloat(input.value);

    if ((isNaN(current) || current <= 0) && dir < 0) return;

    const base = isNaN(current) || current <= 0 ? 0 : current;
    let next = base + dir * step;

    if (type === 'PERCENT') {
        next = Math.min(100, Math.max(0, next));
    } else {
        next = Math.max(0, next);
    }

    input.value = next;
    updateAmountWords();
}

document.addEventListener('DOMContentLoaded', function () {
    const discountValue = document.getElementById('discountValue');
    const discountForm = document.getElementById('discountForm');
    const discountType = document.getElementById('discountType');

    if (discountValue) {
        discountValue.addEventListener('input', updateAmountWords);
    }

    if (discountType) {
        discountType.addEventListener('change', updateValueHint);
    }

    if (discountForm) {
        discountForm.addEventListener('submit', function (e) {
            const type = document.getElementById('discountType').value;
            const valueRaw = document.getElementById('discountValue').value;
            const value = parseFloat(valueRaw);

            if (!valueRaw || isNaN(value) || value <= 0) {
                alert('Mức giảm phải lớn hơn 0.');
                e.preventDefault();
                return;
            }

            if (type === 'PERCENT' && value > 100) {
                alert('Mức giảm phần trăm không được vượt quá 100%.');
                e.preventDefault();
                return;
            }
        });
    }

    updateValueHint();
});