<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <c:choose>
        <c:when test="${isOwned}">
            <title>${ebook.title}</title>
        </c:when>
        <c:otherwise>
            <title>${ebook.title} - ĐỌC THỬ</title>
        </c:otherwise>
    </c:choose>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <c:set var="fmt" value="${fn:toLowerCase(format)}" />

    <c:if test="${fmt eq 'epub'}">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/epubjs/dist/epub.min.js"></script>
    </c:if>

    <c:if test="${fmt eq 'pdf'}">
        <script type="module">
            import * as pdfjsLib from 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.min.mjs';
            window.pdfjsLib = pdfjsLib;
            pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.worker.min.mjs';
        </script>
    </c:if>

    <style>
        *{
            margin:0;
            padding:0;
            box-sizing:border-box;
        }

        body{
            background:#1e1e1e;
            color:white;
            font-family:Arial,sans-serif;
            overflow:hidden;
        }

        .reader-header{
            height:60px;
            background:#111;
            display:flex;
            align-items:center;
            justify-content:space-between;
            padding:0 20px;
            border-bottom:1px solid #333;
        }

        .reader-title{
            font-size:18px;
            font-weight:bold;
            max-width: 40%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .reader-controls {
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .page-info {
            font-size: 14px;
            color: #ccc;
        }

        .reader-actions{
            display:flex;
            gap:10px;
        }

        .reader-btn{
            padding:8px 14px;
            border:none;
            border-radius:6px;
            cursor:pointer;
            background:#2d7ef7;
            color:white;
            font-weight: bold;
        }

        .reader-btn:disabled {
            background: #555;
            cursor: not-allowed;
        }

        .reader-btn.back-btn {
            background: #444;
        }

        .reader-container{
            width:100%;
            height:calc(100vh - 60px);
            overflow:auto;
            position: relative;
        }

        #pdf-container{
            display:flex;
            flex-direction:column;
            align-items:center;
            padding:20px;
            height: 100%;
            overflow: auto;
        }

        canvas{
            background:white;
            box-shadow:0 0 10px rgba(0,0,0,.5);
            max-width: 100%;
            height: auto !important;
        }

        #epub-viewer{
            width:100%;
            height:100%;
            overflow:hidden;
            background: #fafafa;
        }

        .preview-lock-overlay {
            text-align: center;
            padding: 40px;
            background: rgba(42, 42, 42, 0.95);
            border-radius: 8px;
            border: 1px solid #444;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            z-index: 9999;
            box-shadow: 0 4px 15px rgba(0,0,0,0.5);
        }

        .preview-lock-overlay p {
            margin-bottom: 15px;
            font-size: 16px;
            color: #fff;
        }
    </style>
</head>
<body>

<c:set var="previewPages" value="${isOwned ? -1 : 10}" />

<div class="reader-header">
    <div class="reader-title" title="${ebook.title} ${isOwned ? '' : '- ĐỌC THỬ'}">
        ${ebook.title} ${isOwned ? "" : "- ĐỌC THỬ"}
    </div>

    <div class="reader-controls">
        <button id="prev-btn" class="reader-btn" disabled>◀ Trước</button>
        <span id="page-info" class="page-info">Đang tải tài liệu...</span>
        <button id="next-btn" class="reader-btn" disabled>Sau ▶</button>
    </div>

    <div class="reader-actions">
        <a href="${pageContext.request.contextPath}/bookdetail?id=${ebook.id}">
            <button class="reader-btn back-btn">Quay lại</button>
        </a>
    </div>
</div>

<div class="reader-container" id="main-container">

    <div id="lock-overlay" class="preview-lock-overlay" style="display: none;">
        <p>Bạn đã đọc hết phần xem thử mẫu.</p>
        <a href="${pageContext.request.contextPath}/bookdetail?id=${ebook.id}">
            <button class="reader-btn">Mua ngay để đọc tiếp</button>
        </a>
    </div>

    <c:choose>
        <c:when test="${fmt eq 'pdf'}">
            <div id="pdf-container">
                <canvas id="pdf-canvas"></canvas>
            </div>

            <script type="module">
                const pdfUrl = '${pageContext.request.contextPath}/stream-book?id=${ebook.id}&format=${format}';
                let pdfDoc = null,
                    pageNum = 1,
                    pageRendering = false,
                    pageNumPending = null,
                    scale = 1.5,
                    canvas = document.getElementById('pdf-canvas'),
                    ctx = canvas.getContext('2d');

                const previewLimit = ${previewPages};

                async function renderPage(num) {
                    pageRendering = true;
                    try {
                        const page = await pdfDoc.getPage(num);
                        const viewport = page.getViewport({ scale: scale });

                        canvas.height = viewport.height;
                        canvas.width = viewport.width;

                        const renderContext = { canvasContext: ctx, viewport: viewport };
                        await page.render(renderContext).promise;
                    } catch(e) {
                        console.error("Lỗi kết xuất trang:", e);
                    }

                    pageRendering = false;

                    if (pageNumPending !== null) {
                        renderPage(pageNumPending);
                        pageNumPending = null;
                    }

                    document.getElementById('page-info').textContent = `Trang \${num} / \${pdfDoc.numPages}`;
                    document.getElementById('prev-btn').disabled = (num <= 1);

                    document.getElementById('next-btn').disabled = (num >= pdfDoc.numPages);
                }

                function queueRenderPage(num) {
                    if (previewLimit > 0 && num > previewLimit) {
                        document.getElementById('lock-overlay').style.display = 'block';
                        document.getElementById('pdf-container').style.display = 'none';
                        document.getElementById('next-btn').disabled = true;
                        document.getElementById('page-info').textContent = `Trang \${previewLimit} / \${pdfDoc.numPages}`;
                        return;
                    }

                    document.getElementById('lock-overlay').style.display = 'none';
                    document.getElementById('pdf-container').style.display = 'flex';

                    if (pageRendering) {
                        pageNumPending = num;
                    } else {
                        renderPage(num);
                    }
                }

                document.getElementById('prev-btn').addEventListener('click', () => {
                    if (pageNum <= 1) return;
                    pageNum--;
                    queueRenderPage(pageNum);
                });

                document.getElementById('next-btn').addEventListener('click', () => {
                    if (!pdfDoc || pageNum >= pdfDoc.numPages) return;
                    pageNum++;
                    queueRenderPage(pageNum);
                });

                (async function initPDF() {
                    try {
                        if (!window.pdfjsLib) {
                            document.getElementById('page-info').textContent = "Thư viện PDF chưa tải xong";
                            return;
                        }
                        pdfDoc = await window.pdfjsLib.getDocument(pdfUrl).promise;
                        renderPage(pageNum);
                    } catch (error) {
                        document.getElementById('page-info').textContent = "Không thể tải tập tin PDF";
                        console.error("PDFJS Error: ", error);
                    }
                })();
            </script>
        </c:when>

        <c:when test="${fmt eq 'epub'}">
            <div id="epub-viewer"></div>

            <script>
                let rendition = null;
                let isRedirecting = false;
                const previewLimit = ${previewPages};

                async function loadBook() {
                    try {
                        document.getElementById("page-info").textContent = "Đang tải...";

                        const response = await fetch('${file.fileLink}');
                        if(!response.ok) throw new Error("HTTP error " + response.status);

                        const buffer = await response.arrayBuffer();
                        const book = ePub(buffer);

                        rendition = book.renderTo("epub-viewer", {
                            width: "100%",
                            height: "100%",
                            spread: "none",
                            flow: "paginated",
                            manager: "default"
                        });

                        await book.ready;
                        const spineItems = book.spine.spineItems;
                        const firstSection = book.spine.get(0);
                        await rendition.display(firstSection.href);

                        rendition.on("relocated", (location) => {
                            if (isRedirecting) {
                                isRedirecting = false;
                                return;
                            }

                            const currentIndex = book.spine.get(location.start.href).index;
                            document.getElementById("page-info").textContent = `Chương \${currentIndex + 1} / \${spineItems.length}`;

                            if (previewLimit > 0 && currentIndex >= previewLimit) {
                                isRedirecting = true;
                                document.getElementById('lock-overlay').style.display = 'block';
                                document.getElementById('epub-viewer').style.display = 'none';
                                document.getElementById('next-btn').disabled = true;

                                rendition.display(spineItems[previewLimit - 1].href);
                                return;
                            }

                            // Mở khóa bình thường nếu lùi lại
                            document.getElementById('lock-overlay').style.display = 'none';
                            document.getElementById('epub-viewer').style.display = 'block';
                            document.getElementById('prev-btn').disabled = location.atStart;

                            if (previewLimit > 0) {
                                document.getElementById('next-btn').disabled = (currentIndex >= previewLimit - 1);
                            } else {
                                document.getElementById('next-btn').disabled = location.atEnd;
                            }
                        });

                    } catch (e) {
                        console.error("EPUB Error: ", e);
                        document.getElementById("page-info").textContent = "Không thể đọc tập tin EPUB";
                    }
                }

                window.addEventListener('DOMContentLoaded', loadBook);

                document.getElementById('prev-btn').addEventListener('click', () => {
                    if (rendition) rendition.prev();
                });

                document.getElementById('next-btn').addEventListener('click', () => {
                    if (rendition) rendition.next();
                });
            </script>
        </c:when>

        <c:otherwise>
            <div style="padding:40px; text-align:center; color: #ff6b6b;">
                Định dạng tập tin (${format}) không được hệ thống hỗ trợ.
            </div>
            <script>
                document.getElementById('page-info').textContent = "Lỗi định dạng";
            </script>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>