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

    public List<Review> findAll() {
        logger.info("Executing findAll reviews");
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
            logger.info("Successfully fetched {} reviews", list.size());
        } catch (Exception e) {
            logger.error("Error in findAll reviews", e);
            e.printStackTrace();
        }
        return list;
    }

    public void delete(int id) {
        logger.info("Executing delete review for id: {}", id);
        String sql = "DELETE FROM review WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Successfully deleted review with id: {}", id);
            } else {
                logger.warn("No review found to delete with id: {}", id);
            }

        } catch (Exception e) {
            logger.error("Error in delete review for id: {}", id, e);
            e.printStackTrace();
        }
    }
}