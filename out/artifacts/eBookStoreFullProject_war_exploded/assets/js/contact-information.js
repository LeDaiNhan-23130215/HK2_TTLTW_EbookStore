const emailRegex = /^[^\s@]+@[^\s@]+\.+[^\s@]+$/;
const phoneRegex = /^0\d{9}$/;
const nameRegex = /^[A-Za-zÃ-ỳ\s]+$/;
const passwordRegex = /^[A-Za-z0-9!@#$%^&*]{6,}$/;
const continueBtn = document.querySelector('.continue-btn')

continueBtn.addEventListener('click', (e) => {
    e.preventDefault();
    const emailInput = document.getElementById('emailInput').value.trim();
    const fullNameInput = document.getElementById('fullNameInput').value.trim();
    const phoneNumInput = document.getElementById('phoneNumInput').value.trim();

    ['emailInput', 'fullNameInput', 'phoneNumInput'].forEach(clearError);

    if(!emailInput || !fullNameInput || !phoneNumInput){
        if(!emailInput) showError('emailInput', "Email không được để trống");
        if(!fullNameInput) showError('fullNameInput', "Họ và tên không được để trống");
        if(!phoneNumInput) showError('phoneNumInput', "Số điện thoại không được để trống");
        return;
    }

    if(!emailRegex.test(emailInput)){
        showError('emailInput', "Sai định dạng email");
    }
    if(!nameRegex.test(fullNameInput)){
        showError('fullNameInput', "Họ và tên không được chứa kí tự đặc biệt hoặc số");
    }
    if(!phoneRegex.test(phoneNumInput)){
        showError('phoneNumInput', "Số điện thoại của bạn không đúng");
    }

    const errors = document.querySelectorAll('.error-msg');
    let hasError = false;
    for(const e of errors){
        if(e.textContent !== ""){
            hasError = true;
            break;
        }
    }
    if(hasError) return;
});

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

document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () =>{
        const id = input.id;
        const value = input.value.trim();

        clearError(id);

        if(id === 'emailInput'){
            if(value && !emailRegex.test(value)) showError(id, "Email không hợp lệ");
        }
        if(id === 'phoneNumInput'){
            if(value && !phoneRegex.test(value)) showError(id, "Số điện thoại không hợp lệ");
        }
        if(id === 'fullNameInput'){
            if(value && !nameRegex.test(value)) showError(id, "Không được chứa ký tự đặc biệt hoặc số");
        }
    });
});