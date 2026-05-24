package DAO;

import models.Image;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    private static final Logger logger = LoggerFactory.getLogger(ImageDAO.class);
    private static final String LOG_PREFIX = "[IMAGE_DAO]";

    public int insertAndReturnId(Image image) {
        logger.info("{} Executing insertAndReturnId for imgName: {}", LOG_PREFIX, image.getImgName());
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
                int generatedId = rs.getInt(1);
                logger.info("{} Successfully inserted image, generated ID: {}", LOG_PREFIX,generatedId);
                return generatedId;
            }
            logger.warn("{} Image inserted but no ID was generated for imgName: {}", LOG_PREFIX, image.getImgName());

        } catch (SQLException e) {
            logger.error("{} Error in insertAndReturnId for imgName: {}", LOG_PREFIX, image.getImgName(), e);
            throw new RuntimeException(e);
        }
        return -1;
    }

    public Image getImageById(int id) {
        logger.info("{} Executing getImageById for id: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM images WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("{} uccessfully fetched image for id: {}", LOG_PREFIX, id);
                return mapImage(rs);
            }
            logger.info("{} No image found for id: {}", LOG_PREFIX, id);

        } catch (SQLException e) {
            logger.error("{} Error in getImageById for id: {}", LOG_PREFIX, id, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Image> getByEbookID(int ebookID) {
        logger.info("{} Executing getByEbookID for ebookID: {}", LOG_PREFIX, ebookID);
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
            logger.info("{} Successfully fetched {} active images for ebookID: {}", LOG_PREFIX, list.size(), ebookID);

        } catch (SQLException e) {
            logger.error("{} Error in getByEbookID for ebookID: {}", LOG_PREFIX, ebookID, e);
            throw new RuntimeException(e);
        }
        return list;
    }

    public Image getFirstImageByEbookID(int ebookID) {
        logger.info("{} Executing getFirstImageByEbookID for ebookID: {}", LOG_PREFIX, ebookID);
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
                logger.info("{} Successfully fetched first active image for ebookID: {}", LOG_PREFIX, ebookID);
                return mapImage(rs);
            }
            logger.info("{} No active image found for ebookID: {}", LOG_PREFIX, ebookID);

        } catch (SQLException e) {
            logger.error("{} Error in getFirstImageByEbookID for ebookID: {}", LOG_PREFIX, ebookID, e);
            throw new RuntimeException(e);
        }
        return null;
    }

    private Image mapImage(ResultSet rs) throws SQLException {
        return new Image(
                rs.getInt("id"),
                rs.getString("imgName"),
                rs.getString("imgLink"),
                rs.getString("imgStatus")
        );
    }
}