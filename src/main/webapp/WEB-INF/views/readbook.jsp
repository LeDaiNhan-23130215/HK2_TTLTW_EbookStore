<%@ page contentType="text/html;charset=UTF-8" %>
<%
  String ebookId = (String) request.getAttribute("ebookId");
  String type = (String) request.getAttribute("type");

  String pdfUrl;
  if ("sample".equals(type)) {
    pdfUrl = request.getContextPath() + "/read-sample?id=" + ebookId;
  } else {
    pdfUrl = request.getContextPath() + "/read-full?id=" + ebookId;
  }
%>

<!DOCTYPE html>
<html>
<head>
  <title>Đọc sách</title>
  <style>
    body { margin: 0 }
    iframe { width: 100%; height: 100vh; border: none }
  </style>
</head>
<body>

<iframe src="<%= pdfUrl %>"></iframe>

</body>
</html>
