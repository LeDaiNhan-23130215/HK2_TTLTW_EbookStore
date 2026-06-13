<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Ebook Manager</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-ebook.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-form.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-delete-modal.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>

    <!-- Bootstrap + DataTable -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#ebookTable').DataTable({
                pageLength: 5,
                lengthMenu: [5, 10, 20, 50],
                ordering: true,
                searching: true,
                language: {
                    lengthMenu: "Hiển thị _MENU_ dòng",
                    search: "Tìm kiếm:",
                    info: "Trang _PAGE_ / _PAGES_",
                    paginate: {
                        first: "Đầu",
                        last: "Cuối",
                        next: "Tiếp",
                        previous: "Trước"
                    },
                    zeroRecords: "Không tìm thấy dữ liệu"
                }
            });
        });
    </script>
</head>

<body>

<!-- Sidebar -->
<aside class="sidebar">
    <div class="sidebar-logo"><h2>Ebook Admin</h2></div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/admin-dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin-ebook" class="active">Ebook</a>
        <a href="${pageContext.request.contextPath}/admin-author">Tác giả</a>
        <a href="${pageContext.request.contextPath}/admin-category">Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin-user">Người dùng</a>
        <a href="${pageContext.request.contextPath}/admin-payment">Thanh toán</a>
        <a href="${pageContext.request.contextPath}/admin-banner">Banner</a>
        <a href="${pageContext.request.contextPath}/admin-news">Tin tức</a>
        <a href="${pageContext.request.contextPath}/admin-review">Review</a>
        <a href="${pageContext.request.contextPath}/admin-feedback">Feedback</a>
        <a href="${pageContext.request.contextPath}/admin-discount">Giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-voucher">Mã giảm giá</a>
        <a href="${pageContext.request.contextPath}/admin-logs">
            System Logs
        </a>
        <hr>
        <a href="${pageContext.request.contextPath}/admin-login" class="logout">Đăng xuất</a>
    </nav>
</aside>

<!-- Main -->
<div class="main-content">

    <header class="topbar">
        <div class="topbar-title">Quản lý eBook</div>
        <button id="toggle-theme">🌙 Dark Mode</button>
    </header>

    <!-- Form thêm ebook -->
    <section class="dashboard">
        <div class="add-form">
            <h2><i class="fa-solid fa-plus"></i> Thêm eBook</h2>

            <form action="${pageContext.request.contextPath}/admin-ebook"
                  method="post">

                <input type="hidden" name="action" value="add"/>

                <div class="form-row">
                    <label>Tên sách:</label>
                    <input type="text" name="title" required>
                </div>

                <div class="form-row">
                    <label>Tác giả:</label>
                    <select name="authorId" required>
                        <option value="">-- Chọn tác giả --</option>
                        <c:forEach var="a" items="${authors}">
                            <option value="${a.id}">${a.authorName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-row">
                    <label>Thể loại:</label>
                    <select name="categoryId" required>
                        <option value="">-- Chọn thể loại --</option>
                        <c:forEach var="c" items="${categories}">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-row">
                    <label>Giá:</label>
                    <input type="number" name="price" required>
                </div>

                <div class="form-row">
                    <label>Ảnh bìa:</label>

                    <div id="image-container">
                        <small class="text-muted">
                            Nếu không nhập ảnh, hệ thống sẽ dùng ảnh mặc định
                        </small>
                        <div class="image-input">
                            <input type="text"
                                   name="coverUrls[]"
                                   placeholder="URL ảnh bìa"
                                   oninput="previewImage(this)">

                            <img class="img-preview"
                                 style="display:none"/>

                            <button type="button"
                                    class="btn btn-danger btn-sm"
                                    onclick="removeImage(this)">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </div>

                    </div>

                    <button type="button"
                            class="btn btn-secondary btn-sm mt-2"
                            onclick="addImage()">
                        <i class="fa-solid fa-plus"></i> Thêm ảnh
                    </button>
                </div>

                <div class="form-row">
                    <label>File ebook:</label>
                    <input type="text" name="filePath">
                </div>

                <div class="form-row">
                    <label>Mô tả:</label>
                    <textarea name="description"></textarea>
                </div>

                <button type="submit" class="btn-addEbook">
                    Thêm sách
                </button>
            </form>
        </div>
    </section>

    <!-- Table -->
    <section class="table-section">
        <table id="ebookTable" class="table table-striped table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên sách</th>
                <th>Mã sách</th>
                <th>Thể loại</th>
                <th>Giá</th>
                <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="e" items="${ebooks}">
                <tr>
                    <td>${e.id}</td>
                    <td>${e.title}</td>
                    <td>${e.EBookCode}</td>
                    <td>${categoryMap[e.categoryID]}</td>
                    <td>${e.price}</td>
                    <td id="btn-place">
                        <a class="btn-Edit"
                           href="${pageContext.request.contextPath}/admin-ebook?action=edit&id=${e.id}">
                            <i class="fa-solid fa-pen-to-square"></i> Sửa
                        </a>

                        <a class="btn-Del admin-delete-trigger"
                           href="${pageContext.request.contextPath}/admin-ebook?action=delete&id=${e.id}"
                           data-title="Xoá ebook?"
                           data-desc="Bạn có chắc muốn xoá ebook '${e.title}'? Hành động này không thể hoàn tác.">
                            <i class="fa-solid fa-trash"></i> Xóa
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>

</div>

<!-- Delete Confirmation Modal -->
<div id="adminDeleteModal" class="ui-modal-overlay" style="display:none;" aria-modal="true" role="dialog">
    <div class="ui-modal-box">
        <div class="ui-modal-icon"><i class="fa-solid fa-trash-can"></i></div>
        <h3 class="ui-modal-title" id="adminDeleteModalTitle">Xác nhận xoá</h3>
        <p class="ui-modal-desc" id="adminDeleteModalDesc"></p>
        <div class="ui-modal-actions">
            <button type="button" class="ui-modal-cancel" id="adminDeleteModalCancel">Quay lại</button>
            <button type="button" class="ui-modal-confirm" id="adminDeleteModalConfirm">
                <i class="fa-solid fa-trash-can"></i> Xoá
            </button>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin-delete-modal.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/admin-darkmode.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/showForm.js"></script>
<script>
    function addImage() {
        const container = document.getElementById("image-container");

        const div = document.createElement("div");
        div.className = "image-input";
        div.innerHTML = `
            <input type="text"
                   name="coverUrls[]"
                   placeholder="URL ảnh bìa"
                   oninput="previewImage(this)">

            <img class="img-preview"
                 style="display:none"/>

            <button type="button"
                    class="btn btn-danger btn-sm"
                    onclick="removeImage(this)">
                <i class="fa-solid fa-trash"></i>
            </button>
        `;

        container.appendChild(div);
    }

    function removeImage(btn) {
        btn.parentElement.remove();
    }

    function previewImage(input) {
        const url = input.value.trim();
        const img = input.parentElement.querySelector(".img-preview");

        if (!url) {
            img.style.display = "none";
            img.src = "";
            return;
        }

        img.src = url;
        img.style.display = "block";

        // nếu link lỗi → ẩn ảnh
        img.onerror = () => {
            img.style.display = "none";
        };
    }

    document.addEventListener("DOMContentLoaded", function () {

        const authorSelect = document.querySelector("select[name='authorId']");
        const categorySelect = document.querySelector("select[name='categoryId']");

        if (!authorSelect || !categorySelect) {
            console.error("Không tìm thấy select author/category");
            return;
        }

        fetch("${pageContext.request.contextPath}/admin-ebook/form-data")
            .then(res => res.json())
            .then(data => {

                console.log("FORM DATA:", data);

                authorSelect.innerHTML = `<option value="">-- Chọn tác giả --</option>`;
                categorySelect.innerHTML = `<option value="">-- Chọn thể loại --</option>`;

                data.authors.forEach(a => {
                    authorSelect.add(new Option(a.label, a.id));
                });

                data.categories.forEach(c => {
                    categorySelect.add(new Option(c.label, c.id));
                });

            })
            .catch(err => console.error("Load form data error:", err));
    });
</script>
</body>
</html>