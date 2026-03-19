const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;

function showError(input, message) {
    input.parentElement.querySelector('.error-msg').textContent = message;
}

function clearError(input) {
    input.parentElement.querySelector('.error-msg').textContent = "";
}

// Chỉ bắt 2 input cần thiết
const userInput = document.getElementById('userAndEmail');
const passwordInput = document.getElementById('password');

// Realtime check username / email
userInput.addEventListener('input', () => {
    const value = userInput.value.trim();
    clearError(userInput);

    if (!value) return;

    // Nếu có @ thì bắt buộc là email đúng
    if (value.includes('@') && !emailRegex.test(value)) {
        showError(userInput, "Sai định dạng email");
    }
});

// Realtime check password
passwordInput.addEventListener('input', () => {
    const value = passwordInput.value.trim();
    clearError(passwordInput);

    if (!value) return;

    if (!passwordRegex.test(value)) {
        showError(passwordInput, "Mật khẩu tối thiểu 8 ký tự hoặc thiếu ký tự đặc biệt.");
    }
});