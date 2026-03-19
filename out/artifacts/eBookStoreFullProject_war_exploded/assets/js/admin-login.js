const signInBtn = document.querySelector('.signIn-btn');
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const passwordRegex = /^[A-Za-z0-9!@#$%^&*]{6,}$/;

signInBtn.addEventListener('click', (e) => {
    e.preventDefault();
    //Lấy dữ liệu từ input email và password
    const email = document.getElementById('userAndEmail').value.trim();
    const password = document.getElementById('password').value.trim();

    //Clear old errors
    ['userAndEmail', 'password'].forEach(clearError);

    //Check empty
    if (!email || !password) {
        if(!email) showError('userAndEmail', "Email không được để trống");
        if(!password) showError('password', "Mật khẩu không được để trống");
        return;
    }
    //Check valid email and Alert if wrong format
    const isValid = checkValidEmail(email);
    if (!isValid) {
        showError('userAndEmail', "Email không hợp lệ");
        return;
    }
    //Change page
    if(isValid){
        alert("Đăng nhập thành công")
        window.location.href = "../pages/admin-dashboard.html";
    }
    else{
        showError('userAndEmail', "Sai email hoặc mật khẩu");
        showError('password', "Sai email hoặc mật khẩu");
    }
});
function checkValidEmail(email){
    if(!emailRegex.test(email)){
        return false;
    }
    else{
        return true;
    }
}
function checkValidPassword(password){
    if (!passwordRegex.test(password)) {
        return false;
    }
    else{
        return true;
    }
}
function showError(inputId, message) {
    const inputDiv = document.getElementById(inputId).parentElement;
    const errorSpan = inputDiv.querySelector('.error-msg');
    errorSpan.textContent = message;
}
function clearError(inputId) {
    const inputDiv = document.getElementById(inputId).parentElement;
    const errorSpan = inputDiv.querySelector('.error-msg');
    errorSpan.textContent = "";
}
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        const id = input.id;
        const value = input.value.trim();

        clearError(id);

        if(id === 'userAndEmail'){
            if(value && !emailRegex.test(value)) showError(id, "Sai định dạng email");
        }
        if(id === 'password'){
            if(value && !passwordRegex.test(value)) showError(id, "Mật khẩu yếu hoặc thiếu ký tự đặc biệt");
        }
    });
});