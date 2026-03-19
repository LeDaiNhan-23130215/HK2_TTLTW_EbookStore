package DAO;

import models.File;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FileDAO {

    // ===================== INSERT FILE =====================
    public int insertAndReturnId(File file) {
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
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ===================== GET PDF PATH =====================
    public String getPdfPathByEbookId(int ebookId) {
        String sql = "SELECT pdf_path FROM ebook_files WHERE ebook_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("pdf_path");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


