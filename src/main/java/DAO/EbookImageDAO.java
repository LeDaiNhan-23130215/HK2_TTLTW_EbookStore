package DAO;
import models.Image;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookImageDAO {

    public void insert(int ebookId, int imageId) {
        String sql = "INSERT INTO ebookimage (ebookID, imgID) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, imageId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getImageIdsByEbook(int ebookId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT imgID FROM ebookimage WHERE ebookID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("imgID"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Image> getImagesByEbookID(int ebookID) {
        List<Image> images = new ArrayList<>();

        String sql = """
        SELECT i.id, i.imgName, i.imgLink, i.imgStatus
        FROM ebookimage ei
        JOIN images i ON ei.imgID = i.id
        WHERE ei.ebookID = ?
        ORDER BY i.id
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int imageId = rs.getInt("id");
                Image img = new Image(imageId);
                img.setImgName(rs.getString("imgName"));
                img.setImgLink(rs.getString("imgLink"));
                img.setImgStatus(rs.getString("imgStatus"));
                images.add(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }

    public void linkImageToEbook(int ebookID, int imageID) {
        String sql = "INSERT INTO ebookimage (ebookID, imgID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.setInt(2, imageID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeByEbookID(int ebookID) {
        String sql = "DELETE FROM ebookimage WHERE ebookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}