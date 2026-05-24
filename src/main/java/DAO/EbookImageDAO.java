package DAO;
import models.Image;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EbookImageDAO {

    private static final Logger logger = LoggerFactory.getLogger(EbookImageDAO.class);
    private static final String LOG_PREFIX = "[EBOOK_IMAGE_DAO]";

    public void insert(int ebookId, int imageId) {
        logger.info("{} Executing insert for ebookId: {}, imageId: {}", LOG_PREFIX, ebookId, imageId);
        String sql = "INSERT INTO ebookimage (ebookID, imgID) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ps.setInt(2, imageId);
            ps.executeUpdate();

        } catch (Exception e) {
            logger.error("Error in insert for ebookId: {}, imageId: {}", ebookId, imageId, e);
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getImageIdsByEbook(int ebookId) {
        logger.info("{} Executing getImageIdsByEbook for ebookId: {}", LOG_PREFIX, ebookId);
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT imgID FROM ebookimage WHERE ebookID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ebookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("imgID"));
            }
            logger.info("{} Successfully fetched {} image IDs for ebookId: {}", LOG_PREFIX, list.size(), ebookId);

        } catch (Exception e) {
            logger.error("{} Error in getImageIdsByEbook for ebookId: {}", LOG_PREFIX, ebookId, e);
        }
        return list;
    }

    public List<Image> getImagesByEbookID(int ebookID) {
        logger.info("{} Executing getImagesByEbookID for ebookID: {}", LOG_PREFIX, ebookID);
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
            logger.info("{} Successfully fetched {} images for ebookID: {}", LOG_PREFIX, images.size(), ebookID);
        } catch (Exception e) {
            logger.error("{} Error in getImagesByEbookID for ebookID: {}", LOG_PREFIX, ebookID, e);
        }
        return images;
    }

    public void linkImageToEbook(int ebookID, int imageID) {
        logger.info("{} Executing linkImageToEbook for ebookID: {}, imageID: {}", LOG_PREFIX, ebookID, imageID);
        String sql = "INSERT INTO ebookimage (ebookID, imgID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.setInt(2, imageID);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error in linkImageToEbook for ebookID: {}, imageID: {}", ebookID, imageID, e);
            throw new RuntimeException(e);
        }
    }

    public void removeByEbookID(int ebookID) {
        logger.info("{} Executing removeByEbookID for ebookID: {}", LOG_PREFIX,ebookID);
        String sql = "DELETE FROM ebookimage WHERE ebookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ebookID);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error in removeByEbookID for ebookID: {}", ebookID, e);
            throw new RuntimeException(e);
        }
    }
}