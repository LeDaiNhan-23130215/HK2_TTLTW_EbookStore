package DAO;

import models.EbookFile;
import models.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EbookFileDAO {
    private static final Logger logger = LoggerFactory.getLogger(EbookFileDAO.class);
    private static final String LOG_PREFIX = "[EBOOK_FILE_DAO]";
    public void insert(EbookFile ebookFile) {
        String sql = """
                INSERT INTO ebook_files (
                ebookID,
                fileID,
                isDefault
                )
                VALUES (?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ebookFile.getEbookId());
            ps.setInt(2, ebookFile.getFileId());
            ps.setBoolean(3, ebookFile.isDefault());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("{} Error on insert into ebook_files", LOG_PREFIX, e);
            throw new RuntimeException("Error on insert into ebook_files", e);
        }
    }
    public static File getFileByFormat(int EbookId, String format) {
        String sql = """
                SELECT f.*
                FROM ebook_files ef
                JOIN files f
                    ON ef.fileID = f.id
                WHERE ef.ebookID = ?
                AND LOWER(f.fileFormat) = ?
                LIMIT 1
                """;
        try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, EbookId);
            ps.setString(2, format.toLowerCase());

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return new File(
                        rs.getInt("id"),
                        rs.getString("fileName"),
                        rs.getString("fileFormat"),
                        rs.getInt("fileSize"),
                        rs.getString("fileLink"),
                        rs.getString("fileStatus")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<File> getFilesByEbook(
            int ebookId
    ){

        List<File> files =
                new ArrayList<>();

        String sql = """
            SELECT f.*
            FROM ebook_files ef
            JOIN files f
                ON ef.fileID = f.id
            WHERE ef.ebookID = ?
        """;

        try(
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ){

            ps.setInt(1, ebookId);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()){

                files.add(
                        new File(
                                rs.getInt("id"),
                                rs.getString("fileName"),
                                rs.getString("fileFormat"),
                                rs.getLong("fileSize"),
                                rs.getString("fileLink"),
                                rs.getString("fileStatus")
                        )
                );
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return files;
    }
}
