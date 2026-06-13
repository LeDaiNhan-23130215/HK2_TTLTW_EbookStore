package DAO;

import models.File;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FileDAO {

    private static final Logger logger = LoggerFactory.getLogger(FileDAO.class);
    private static final String LOG_PREFIX = "[FILE_DAO]";

    public int insertAndReturnId(File file) {
        logger.info("{} Executing insertAndReturnId for fileName: {}, format: {}", LOG_PREFIX, file.getFileName(), file.getFileFormat());
        String sql = """
            INSERT INTO files (fileName, fileFormat, fileSize, fileLink, fileStatus)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, file.getFileName());
            ps.setString(2, file.getFileFormat());
            ps.setLong(3, file.getFileSize());
            ps.setString(4, file.getFileLink());
            ps.setString(5, file.getFileStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("{} Successfully inserted file, generated ID: {}", LOG_PREFIX, generatedId);
                return generatedId;
            }
            logger.info("{} File inserted but no ID was generated", LOG_PREFIX);
        } catch (Exception e) {
            logger.error("{} Error in insertAndReturnId for fileName: {}", LOG_PREFIX, file.getFileName(), e);
        }
        return -1;
    }

    public String getPdfPathByEbookId(int ebookId) {
        logger.info("{} Executing getPdfPathByEbookId for ebookId: {}", LOG_PREFIX, ebookId);
        String sql = "SELECT pdf_path FROM ebook_files WHERE ebook_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String path = rs.getString("pdf_path");
                logger.info("{} Successfully fetched PDF path for ebookId: {}", LOG_PREFIX, ebookId);
                return path;
            } else {
                logger.error("{} No PDF path found for ebookId: {}", LOG_PREFIX, ebookId);
            }

        } catch (Exception e) {
            logger.error("{} Error in getPdfPathByEbookId for ebookId: {}", LOG_PREFIX, ebookId, e);
        }
        return null;
    }

    public static int insertAndReturnIdForPdf(File file) {
        logger.info("{} Executing insertAndReturnIdForf for fileName: {}, format: {}", LOG_PREFIX, file.getFileName(), file.getFileFormat());
        String sql = """
            INSERT INTO files (fileName, fileFormat, fileSize, fileLink, fileStatus)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, file.getFileName());
            ps.setString(2, file.getFileFormat());
            ps.setLong(3, file.getFileSize());
            ps.setString(4, file.getFileLink());
            ps.setString(5, file.getFileStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("{} Successfully inserted PDF file, generated ID: {}", LOG_PREFIX, generatedId);
                return generatedId;
            }
            logger.info("{} PDF file inserted but no ID was generated", LOG_PREFIX);
        } catch (Exception e) {
            logger.error("{} Error in insertAndReturnId for fileName: {}", LOG_PREFIX, file.getFileName(), e);
        }
        return -1;
    }

    public File getFileById(int id) {
        String sql = """
                    SELECT id, fileName, fileFormat, fileSize, fileLink, fileStatus
                    FROM files
                    WHERE id = ?
                    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new File(
                        rs.getInt("id"),
                        rs.getString("fileName"),
                        rs.getString("fileFormat"),
                        rs.getLong("fileSize"),
                        rs.getString("fileLink"),
                        rs.getString("fileStatus")
                );
            }
        } catch (Exception e) {
            logger.error("{} Error getFileById", LOG_PREFIX, e);
        }
        return null;
    }

    public static void main(String[] args) {
        FileDAO fileDAO = new FileDAO();

        System.out.println(fileDAO.getFileById(1).getFileLink());
    }
}

