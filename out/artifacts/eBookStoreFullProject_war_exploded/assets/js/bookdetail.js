// // --- 1. GIẢ LẬP DỮ LIỆU TỪ DATABASE ---
// // Đây là mô phỏng dữ liệu bạn lấy từ backend (eBook, Author, Image, v.v.)
// const dbData = {
//   bookDetail: {
//     id: 101,
//     title: "Lập Trình Tư Duy - The Pragmatic Programmer",
//     authorName: "Andy Hunt & Dave Thomas", // Join từ bảng Author
//     categoryName: "Công nghệ thông tin",   // Join từ bảng Category
//     price: 350000,                         // Bảng eBook
//     discountPercent: 20,                   // Giả lập tính toán từ bảng Discount
//     rating: 4.5,                           // Tính từ bảng Review
//     reviewCount: 128,
//     status: "Còn hàng",
//     description: `
//             <p>Cuốn sách này là kim chỉ nam cho các lập trình viên muốn nâng cao tay nghề.</p>
//             <p>Nội dung bao gồm các phương pháp thực hành tốt nhất, cách tránh bẫy phổ biến và tư duy hệ thống.</p>
//             <br>
//             <strong>Đặc điểm nổi bật:</strong>
//             <ul>
//                 <li>Kiến thức thực tế, không lý thuyết suông.</li>
//                 <li>Phù hợp cho cả Junior và Senior.</li>
//             </ul>
//         `,
//     images: [
//       "https://marketplace.canva.com/EAFaQ8qaArw/1/0/1003w/canva-brown-rusty-mystery-novel-book-cover-hG1QhA7BiBU.jpg", // Ảnh chính (eBook.img)
//       "https://d1csarkz8obe9u.cloudfront.net/posterpreviews/action-thriller-book-cover-design-template-3675ae3e3ac7ee095fc793ab61b812cc_screen.jpg?ts=1637008457", // Ảnh phụ 1 (Table Image)
//       "https://edit.org/images/cat/book-covers-big-2019101610.jpg"  // Ảnh phụ 2 (Table Image)
//     ]
//   },
//   // Danh sách đề xuất (Cột phải)
//   relatedBooks: [
//     { title: "Clean Code", price: 300000, img: "https://m.media-amazon.com/images/I/41xShlnTZTL._AC_SY200_.jpg" },
//     { title: "Head First Java", price: 420000, img: "https://m.media-amazon.com/images/I/51w64A6G04L._AC_SY200_.jpg" },
//     { title: "Design Patterns", price: 280000, img: "https://m.media-amazon.com/images/I/51szD9HC9pL._AC_SY200_.jpg" }
//   ]
// };
//
// // --- 2. HÀM RENDER DỮ LIỆU RA HTML ---
// document.addEventListener("DOMContentLoaded", () => {
//   const book = dbData.bookDetail;
//
//   // A. Render thông tin cơ bản
//   document.getElementById("bookTitle").textContent = book.title;
//   document.getElementById("authorName").textContent = book.authorName;
//   document.getElementById("categoryName").textContent = book.categoryName;
//   document.getElementById("bookID").textContent = `#${book.id}`;
//   document.getElementById("reviewCount").textContent = book.reviewCount;
//   document.getElementById("fullDescription").innerHTML = book.description;
//
//   // Render giá (Tính toán giảm giá)
//   const finalPrice = book.price * (1 - book.discountPercent / 100);
//   document.getElementById("finalPrice").textContent = finalPrice.toLocaleString('vi-VN') + " đ";
//   document.getElementById("originalPrice").textContent = book.price.toLocaleString('vi-VN') + " đ";
//   document.getElementById("discountTag").textContent = `-${book.discountPercent}%`;
//
//   // Render Sao (Rating)
//   const starContainer = document.getElementById("starRating");
//   for (let i = 1; i <= 5; i++) {
//     if (i <= Math.round(book.rating)) {
//       starContainer.innerHTML += '<i class="fas fa-star"></i>'; // Sao vàng
//     } else {
//       starContainer.innerHTML += '<i class="far fa-star"></i>'; // Sao rỗng
//     }
//   }
//
//   // B. Render Ảnh & Thư viện ảnh
//   const mainImg = document.getElementById("mainImg");
//   const thumbContainer = document.getElementById("thumbnailContainer");
//
//   // Đặt ảnh đầu tiên
//   mainImg.src = book.images[0];
//
//   // Tạo các ảnh nhỏ
//   book.images.forEach((imgUrl, index) => {
//     let img = document.createElement("img");
//     img.src = imgUrl;
//     img.classList.add("thumb-img");
//     if (index === 0) img.classList.add("active");
//
//     // Sự kiện click đổi ảnh chính
//     img.onclick = function () {
//       mainImg.src = imgUrl;
//       // Xóa class active cũ
//       document.querySelectorAll(".thumb-img").forEach(el => el.classList.remove("active"));
//       // Thêm class active mới
//       this.classList.add("active");
//     };
//     thumbContainer.appendChild(img);
//   });
//
//   // C. Render Sách đề xuất (Sidebar)
//   const relatedContainer = document.getElementById("relatedProducts");
//   dbData.relatedBooks.forEach(item => {
//     const html = `
//             <div class="related-item">
//                 <img src="${item.img}" alt="${item.title}">
//                 <div class="related-info">
//                     <h4>${item.title}</h4>
//                     <p class="related-price">${item.price.toLocaleString('vi-VN')} đ</p>
//                 </div>
//             </div>
//         `;
//     relatedContainer.innerHTML += html;
//   });
// });
//
// // --- 3. CÁC HÀM XỬ LÝ SỰ KIỆN (INTERACTION) ---
//
// // Tăng giảm số lượng
// function updateQty(change) {
//   const input = document.getElementById("qtyInput");
//   let newVal = parseInt(input.value) + change;
//   if (newVal >= 1) {
//     input.value = newVal;
//   }
// }
//
// // Chuyển Tab (Mô tả / Hướng dẫn)
// function openTab(evt, tabName) {
//   // Ẩn tất cả nội dung tab
//   var tabContent = document.getElementsByClassName("tab-content");
//   for (var i = 0; i < tabContent.length; i++) {
//     tabContent[i].style.display = "none";
//     tabContent[i].classList.remove("active");
//   }
//
//   // Xóa class active ở các button
//   var tabLinks = document.getElementsByClassName("tab-btn");
//   for (var i = 0; i < tabLinks.length; i++) {
//     tabLinks[i].className = tabLinks[i].className.replace(" active", "");
//   }
//
//   // Hiển thị tab hiện tại
//   document.getElementById(tabName).style.display = "block";
//   document.getElementById(tabName).classList.add("active");
//   evt.currentTarget.className += " active";
// }