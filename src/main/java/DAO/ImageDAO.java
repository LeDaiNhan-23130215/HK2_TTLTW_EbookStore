package DAO;

import models.Image;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    /* ================= INSERT IMAGE ================= */
    public int insertAndReturnId(Image image) {
        String sql = """
            INSERT INTO images (imgName, imgLink, imgStatus)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, image.getImgName());
            ps.setString(2, image.getImgLink());
            ps.setString(3, image.getImgStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    /* ================= GET IMAGE BY ID ================= */
    public Image getImageById(int id) {
        String sql = "SELECT * FROM images WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapImage(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* ================= GET IMAGES BY EBOOK ID ================= */
    public List<Image> getByEbookID(int ebookID) {
        List<Image> list = new ArrayList<>();

        String sql = """
            SELECT i.*
            FROM ebookimage ei
            JOIN images i ON ei.imgID = i.id
            WHERE ei.ebookID = ?
              AND i.imgStatus = 'ACTIVE'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapImage(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /* ================= GET FIRST IMAGE BY EBOOK ================= */
    public Image getFirstImageByEbookID(int ebookID) {
        String sql = """
            SELECT i.*
            FROM ebookimage ei
            JOIN images i ON ei.imgID = i.id
            WHERE ei.ebookID = ?
              AND i.imgStatus = 'ACTIVE'
            ORDER BY i.id ASC
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapImage(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /* ================= HELPER ================= */
    private Image mapImage(ResultSet rs) throws SQLException {
        return new Image(
                rs.getInt("id"),
                rs.getString("imgName"),
                rs.getString("imgLink"),
                rs.getString("imgStatus")
        );
    }
}
