const changePasswordBtn = document.querySelector('.changePass-btn');
const passwordRegex = /^[A-Za-z0-9!@#$%^&*]{6,}$/;

changePasswordBtn.addEventListener('click', (e) => {
    e.preventDefault();

    const oldPassword = document.getElementById('password').value.trim();
    const newPassword = document.getElementById('newPassword').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    //Xóa error-msg sau mỗi lần click
    ['password', 'newPassword', 'confirmPassword'].forEach(clearError);

    //Check empty
    if(!oldPassword || !newPassword || !confirmPassword){
        if(!oldPassword) showError('password', "Mật khẩu của bạn không được để trống");
        if(!newPassword) showError('newPassword', "Mật khẩu mới của bạn không được để trống");
        if(!confirmPassword) showError('confirmPassword', "Vui lòng xác nhận mật khẩu mới");
        return;
    }

    const isValidNewPassword = checkValidPassword(newPassword);
    //Check valid new password
    if(!isValidNewPassword){
        showError('newPassword', "Mật khẩu của bạn chưa bảo đảm an toàn");
    }

    //Check confirm password
    if(newPassword !== confirmPassword){
        showError('confirmPassword', "Mật khẩu xác nhận không khớp")
    }

    //Check remain errors
    const errors = document.querySelectorAll('.error-msg');
    let hasError = false;
    for(const e of errors){
        if (e.textContent !== "") {
            hasError = true;
            break;
        }
    }
    if(hasError) return;

    alert("Đổi mật khẩu thành công!")
    window.location.href = "../pages/login.html"
});

function checkValidPassword(password) {
    if(!passwordRegex.test(password)){
        return false;
    }
    return true;
}

function showError(id, content) {
    const input = document.getElementById(id).parentElement;
    const errorSpan = input.querySelector('.error-msg');
    errorSpan.textContent = content;
}

function clearError(id) {
    const input = document.getElementById(id).parentElement;
    const errorSpan = input.querySelector('.error-msg');
    errorSpan.textContent = "";
}

//Xử lí validation trực tiếp trên input.
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        const id = input.id;
        const value = input.value.trim();

        clearError(id);
        
        if(id === 'newPassword'){
            if(value && !passwordRegex.test(value)) showError(id, "Mật khẩu yếu hoặc thiếu ký tự đặc biệt.");
        }
        if (id === 'confirmPassword') {
            const newPass = document.getElementById('newPassword').value.trim();
            if(value && newPass !== value) showError(id, "Không khớp với mật khẩu trên");
        }
    });
});