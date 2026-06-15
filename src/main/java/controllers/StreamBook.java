package controllers;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.File;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.BookshelfService;
import services.FileServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpResponse;

@WebServlet(name = "StreamBook", value = "/stream-book")
public class StreamBook extends HttpServlet {
    private FileServices fileServices;
    private BookshelfService bookshelfService;

    private final Logger logger = LoggerFactory.getLogger(StreamBook.class);
    private final String LOG_PREFIX = "[STREAM_BOOK_LOG]";

    private static final int BUFFER_SIZE = 8192;

    @Override
    public void init() throws ServletException {
        fileServices = new FileServices();
        bookshelfService = new BookshelfService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            logger.error("{} invalid user", LOG_PREFIX);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int ebookId;

        try {
            ebookId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        String format = request.getParameter("format");

        if (format == null || format.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean isOwned = bookshelfService.userOwnsBook(user.getId(), ebookId);

        File file = fileServices.getFileByFormat(ebookId, format);

        if (file == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        URL url = new URL(file.getFileLink());

        URLConnection connection = url.openConnection();

        long fileSize = connection.getContentLengthLong();

        if (fileSize <= 0) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        long limit;

        if (fileSize < 5L * 1024 * 1024) {
            limit = fileSize * 40 / 100;
        } else if (fileSize < 20L * 1024 * 1024) {
            limit = fileSize * 25 / 100;
        } else {
            limit = fileSize * 15 / 100;
        }

        limit = Math.max(limit, 2L * 1024 * 1024);

        String rangeHeader = request.getHeader("Range");

        long start = 0;
        long end = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String range = rangeHeader.substring(6);
            String parts[] = range.split("-");

            try {
                start = Long.parseLong(parts[0]);
                if (parts.length > 1 && parts[1].isBlank()) {
                    end = Long.parseLong(parts[1]);
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        if (!isOwned) {
            if (start >= limit) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Preview limit reached");
                return;
            }

            if (end >= limit) {
                end = limit - 1;
            }
        }

        long contentLong = end - start + 1;

        if (format.equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
//        } else if (format.equalsIgnoreCase("epub")) {
//            response.setContentType("application/epub+zip");
        } else {
            response.setContentType("applicaion/ocet-stream");
        }

        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        response.setHeader("Accept-Ranges", "bytes");

        response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);

        response.setHeader("Content-Length", String.valueOf(contentLong));


        //Streaming
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestProperty(
                "Range",
                "bytes=" + start + "-" + end
        );


        try (InputStream input = conn.getInputStream();
             OutputStream output = response.getOutputStream();) {
            input.skip(start);

            byte[] buffer = new byte[BUFFER_SIZE];

            long remaining = contentLong;

            int bytesRead;

            while (remaining > 0 && (bytesRead =
                    input.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                output.write(buffer, 0, bytesRead);
                remaining -= bytesRead;
            }

            output.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}