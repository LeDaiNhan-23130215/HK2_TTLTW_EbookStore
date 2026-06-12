<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>

  <title>${ebook.title}</title>

  <style>

    body{
      margin:0;
      background:#525659;
    }

    #pdf-container{
      display:flex;
      flex-direction:column;
      align-items:center;
      padding:20px;
    }

    canvas{
      margin-bottom:20px;
      box-shadow:0 0 10px rgba(0,0,0,.5);
    }

  </style>

</head>

<body>

<div id="pdf-container"></div>

<script type="module">

  import * as pdfjsLib
    from 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.min.mjs';

  pdfjsLib.GlobalWorkerOptions.workerSrc =
          'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/4.4.168/pdf.worker.min.mjs';

  const pdfUrl =
          "${pdfFile.fileLink}";

  const pdf =
          await pdfjsLib.getDocument(pdfUrl).promise;

  const container =
          document.getElementById("pdf-container");

  for(let pageNum = 1; pageNum <= pdf.numPages; pageNum++){

    const page =
            await pdf.getPage(pageNum);

    const viewport =
            page.getViewport({ scale: 1.4 });

    const canvas =
            document.createElement("canvas");

    const context =
            canvas.getContext("2d");

    canvas.width = viewport.width;
    canvas.height = viewport.height;

    container.appendChild(canvas);

    await page.render({
      canvasContext: context,
      viewport: viewport
    }).promise;
  }

</script>

</body>
</html>