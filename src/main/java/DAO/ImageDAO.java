package DAO;

import DTO.EbookProductCardView;
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
            SELECT
                i.id,
                i.imgName,
                CASE
                    WHEN i.migration_status = 'MIGRATED'
                         AND i.cloudinary_url IS NOT NULL
                         AND i.cloudinary_url <> ''
                    THEN i.cloudinary_url
                    ELSE i.imgLink
                END AS imgLink,
                i.imgStatus
            FROM ebookimage ei
            JOIN images i ON ei.imgID = i.id
            WHERE ei.ebookID = ?
              AND i.imgStatus = 'ACTIVE'
            ORDER BY ei.isCover DESC, i.id ASC
            LIMIT 1
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
                SELECT
                    i.id,
                    i.imgName,
                    CASE
                        WHEN i.migration_status = 'MIGRATED'
                             AND i.cloudinary_url IS NOT NULL
                             AND i.cloudinary_url <> ''
                        THEN i.cloudinary_url
                        ELSE i.imgLink
                    END AS imgLink,
                    i.imgStatus
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

    public List<Integer> getImageIdsForMigration() {
        List<Integer> ids = new ArrayList<>();
                String sql = """
                SELECT id
                FROM images
                WHERE migration_status <> 'MIGRATED'
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ids;
    }

    public String getOriginalUrl(int imageId) {
                String sql = """
                SELECT imgLink
                FROM images
                WHERE id = ?
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, imageId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("imgLink");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void markMigrated(int imageId, String cloudinaryUrl) {

        String sql = """
        UPDATE images
        SET cloudinary_url = ?,
            migration_status = 'MIGRATED'
        WHERE id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cloudinaryUrl);
            ps.setInt(2, imageId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void markFailed(int imageId) {

        String sql = """
        UPDATE images
        SET migration_status = 'FAILED'
        WHERE id = ?
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, imageId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getThumbnailByEbookId (int id ) {
        String sql = """
                SELECT
                COALESCE(
                   MIN(CASE
                        WHEN i.migration_status = 'MIGRATED'
                             AND i.cloudinary_url IS NOT NULL
                             AND i.cloudinary_url <> ''
                        THEN i.cloudinary_url
                   END),
                   MIN(i.imgLink),
                   '/assets/img/no-image.png'
                ) AS thumbnail
                FROM ebook e
            LEFT JOIN ebookimage ei ON e.id = ei.ebookID
            LEFT JOIN images i ON ei.imgID = i.id
            WHERE e.status = 'ACTIVE'
            AND ei.isCover = 1
            AND e.id = ?
            GROUP BY e.id, e.title, e.price
            LIMIT 1
               """;

        try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getString("thumbnail");
            }
            rs.close();
        } catch (Exception e) {
            logger.error("{} Can't get thumbnail for ebook id: {}", LOG_PREFIX, id, e);
            throw new RuntimeException("Can't get thumbnail for ebook id: " + id, e);
        }
        return null;
    }
}