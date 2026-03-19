const textarea = document.getElementById("message");

// Auto-expand khi nhập nội dung
textarea.addEventListener("input", function() {
    this.style.height = "auto"; // reset để tính toán lại
    this.style.height = this.scrollHeight + "px"; // set lại theo nội dung
});

document.getElementById("contactForm").addEventListener("submit", function(e) {
    e.preventDefault(); 

    const textarea = document.getElementById("message").value.trim();
    const topic1 = document.getElementById("topic1").value.trim();

    // Validate
    if (textarea === "") {
        alert("Nội dung message không được để trống!");
        textarea.focus();
        return;
    }

    if (topic1 === "") {
        alert("Topic 1 không được để trống!");
        document.getElementById("topic1").focus();
        return;
    }

    if (topic1.length > 20) {
        alert("Topic 1 không được vượt quá 20 ký tự!");
        document.getElementById("topic1").focus();
        return;
    }

    // Nếu hợp lệ
    alert("Message đã được gửi (demo, frontend). Backend sẽ xử lý sau.");
    this.reset();
});

