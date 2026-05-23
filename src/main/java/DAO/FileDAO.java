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

    public int insertAndReturnId(File file) {
        logger.info("Executing insertAndReturnId for fileName: {}, format: {}", file.getFileName(), file.getFileFormat());
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
                logger.info("Successfully inserted file, generated ID: {}", generatedId);
                return generatedId;
            }
            logger.info("File inserted but no ID was generated");
        } catch (Exception e) {
            logger.error("Error in insertAndReturnId for fileName: {}", file.getFileName(), e);
            e.printStackTrace();
        }
        return -1;
    }

    public String getPdfPathByEbookId(int ebookId) {
        logger.info("Executing getPdfPathByEbookId for ebookId: {}", ebookId);
        String sql = "SELECT pdf_path FROM ebook_files WHERE ebook_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String path = rs.getString("pdf_path");
                logger.info("Successfully fetched PDF path for ebookId: {}", ebookId);
                return path;
            }
            logger.info("No PDF path found for ebookId: {}", ebookId);

        } catch (Exception e) {
            logger.error("Error in getPdfPathByEbookId for ebookId: {}", ebookId, e);
            e.printStackTrace();
        }
        return null;
    }
}