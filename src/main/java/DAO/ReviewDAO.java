package DAO;

import models.Review;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private static final Logger logger = LoggerFactory.getLogger(ReviewDAO.class);
    private static final String LOG_PREFIX = "[REVIEW_DAO]";

    public List<Review> findAll() {
        logger.info("{} Executing findAll reviews", LOG_PREFIX);
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM review ORDER BY createdAt DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getInt("ebookID"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("createdAt")
                ));
            }
            logger.info("{} Successfully fetched {} reviews", LOG_PREFIX, list.size());
        } catch (Exception e) {
            logger.error("{} Error in findAll reviews", LOG_PREFIX, e);
        }
        return list;
    }

    public void delete(int id) {
        logger.info("{} Executing delete review for id: {}", LOG_PREFIX, id);
        String sql = "DELETE FROM review WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("{} Successfully deleted review with id: {}", LOG_PREFIX, id);
            } else {
                logger.warn("{} No review found to delete with id: {}", LOG_PREFIX, id);
            }

        } catch (Exception e) {
            logger.error("{} Error in delete review for id: {}", LOG_PREFIX, id, e);
        }
    }
}