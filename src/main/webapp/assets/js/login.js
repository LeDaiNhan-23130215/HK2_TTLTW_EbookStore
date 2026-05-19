const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

function showError(input, message) {
    const err = input.parentElement.querySelector('.error-msg');
    if (err) err.textContent = message;
}
function clearError(input) {
    const err = input.parentElement.querySelector('.error-msg');
    if (err) err.textContent = '';
}

const userInput     = document.getElementById('userAndEmail');
const passwordInput = document.getElementById('loginPassword');

// Realtime check email
if (userInput) {
    userInput.addEventListener('input', () => {
        const value = userInput.value.trim();
        clearError(userInput);
        if (value.includes('@') && !emailRegex.test(value)) {
            showError(userInput, 'Sai định dạng email');
        }
    });
}

// Realtime check password
if (passwordInput) {
    passwordInput.addEventListener('input', () => {
        const value = passwordInput.value.trim();
        clearError(passwordInput);
        if (value && !passwordRegex.test(value)) {
            showError(passwordInput, 'Mật khẩu tối thiểu 8 ký tự, cần chữ hoa, số, ký tự đặc biệt.');
        }
    });
}

// ===== Toggle eye — dùng class .login-eye =====
document.querySelectorAll('.login-eye').forEach(function(icon) {
    icon.addEventListener('click', function() {
        var inp = document.getElementById(this.dataset.target);
        if (!inp) return;
        inp.type = inp.type === 'password' ? 'text' : 'password';
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });
});