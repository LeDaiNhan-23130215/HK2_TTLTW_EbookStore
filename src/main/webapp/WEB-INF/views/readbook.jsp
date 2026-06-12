<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>${ebook.title}</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <c:if test="${file.fileFormat eq 'epub'}">
    <script src="https://cdn.jsdelivr.net/npm/epubjs/dist/epub.min.js"></script>
  </c:if>

  <c:if test="${file.fileFormat eq 'pdf'}">
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
      background: #fafafa;
    }
  </style>
</head>
<body>

<div class="reader-header">
  <div class="reader-title" title="${ebook.title}">
    ${ebook.title}
  </div>

  <div class="reader-controls">
    <button id="prev-btn" class="reader-btn">◀ Trước</button>
    <span id="page-info" class="page-info">Đang tải...</span>
    <button id="next-btn" class="reader-btn">Sau ▶</button>
  </div>

  <div class="reader-actions">
    <a href="${pageContext.request.contextPath}/bookdetail?id=${ebook.id}">
      <button class="reader-btn back-btn">Quay lại</button>
    </a>
  </div>
</div>

<div class="reader-container">
  <c:choose>

    <c:when test="${file.fileFormat eq 'pdf'}">
      <div id="pdf-container">
        <canvas id="pdf-canvas"></canvas>
      </div>

      <script type="module">
        const pdfUrl = '<c:out value="${file.fileLink}" />';
        let pdfDoc = null,
                pageNum = 1,
                pageRendering = false,
                pageNumPending = null,
                scale = 1.5,
                canvas = document.getElementById('pdf-canvas'),
                ctx = canvas.getContext('2d');

        async function renderPage(num) {
          pageRendering = true;
          const page = await pdfDoc.getPage(num);
          const viewport = page.getViewport({ scale: scale });

          canvas.height = viewport.height;
          canvas.width = viewport.width;

          const renderContext = {
            canvasContext: ctx,
            viewport: viewport
          };

          const renderTask = page.render(renderContext);

          await renderTask.promise;
          pageRendering = false;

          if (pageNumPending !== null) {
            renderPage(pageNumPending);
            pageNumPending = null;
          }

          document.getElementById('page-info').textContent = `Trang ${num} / ${pdfDoc.numPages}`;
          document.getElementById('prev-btn').disabled = (num <= 1);
          document.getElementById('next-btn').disabled = (num >= pdfDoc.numPages);
        }

        function queueRenderPage(num) {
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
          if (pageNum >= pdfDoc.numPages) return;
          pageNum++;
          queueRenderPage(pageNum);
        });

        try {
          pdfDoc = await window.pdfjsLib.getDocument(pdfUrl).promise;
          renderPage(pageNum);
        } catch (error) {
          document.getElementById('page-info').textContent = "Lỗi tải file PDF";
          console.error(error);
        }
      </script>
    </c:when>

    <c:when test="${file.fileFormat eq 'epub'}">
      <div id="epub-viewer"></div>

      <script>
        const book = ePub('${file.fileLink}');
        const rendition = book.renderTo("epub-viewer", {
          width: "100%",
          height: "100%",
          spread: "always"
        });

        rendition.display();

        book.ready.then(() => {
          document.getElementById('page-info').textContent = "Sách đã tải xong";
          return book.locations.generate(1024);
        }).then(locations => {
          rendition.on('relocated', location => {
            const percent = book.locations.percentageFromCfi(location.start.cfi);
            const percentageRounded = Math.round(percent * 100);
            document.getElementById('page-info').textContent = `Tiến độ: ${percentageRounded}%`;

            // Bật/Tắt nút bấm dựa vào vị trí đầu/cuối sách
            document.getElementById('prev-btn').disabled = location.atStart;
            document.getElementById('next-btn').disabled = location.atEnd;
          });
        });

        document.getElementById('prev-btn').addEventListener('click', () => {
          rendition.prev();
        });

        document.getElementById('next-btn').addEventListener('click', () => {
          rendition.next();
        });
      </script>
    </c:when>

    <c:otherwise>
      <div style="padding:40px; text-align:center;">
        Định dạng file không được hỗ trợ.
      </div>
      <script>
        document.querySelector('.reader-controls').style.display = 'none';
      </script>
    </c:otherwise>
  </c:choose>
</div>

</body>
</html>