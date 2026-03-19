const nameRegex = /^[A-Za-zÀ-ỹ\s]+$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^0\d{9}$/;
const passwordRegex =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,}$/;
function showError(inputId, message) {
    const inputDiv = document.getElementById(inputId).parentElement;
    const errorSpan = inputDiv.querySelector('.error-msg');
    errorSpan.textContent = message;
}
function clearError(inputId){
    const inputDiv = document.getElementById(inputId).parentElement;
    const errorSpan = inputDiv.querySelector('.error-msg');
    errorSpan.textContent = "";
}
//Xử lí lỗi khi người dùng nhập từng ô input.
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        const id = input.id;
        const value = input.value.trim();

        clearError(id);

        if(id === 'lname' || id === 'fname'){
            if(value && !nameRegex.test(value)) showError(id, "Không được chứa ký tự đặc biệt hoặc số");
        }
        if(id === 'userAndEmail'){
            if(value && !emailRegex.test(value)) showError(id, "Email không hợp lệ");
        }
        if(id === 'phoneNumber'){
            if(value && !phoneRegex.test(value)) showError(id, "Số điện thoại không hợp lệ");
        }
        if(id === 'password'){
            if(value && !passwordRegex.test(value)) showError(id, "Mật khẩu yếu hoặc thiếu ký tự đặc biệt.");
        }
        if(id === 'confirmPassword'){
            const password = document.getElementById('password').value.trim();
            if(value && value !== password) showError(id, "Không khớp với mật khẩu trên");
        }
    });
});