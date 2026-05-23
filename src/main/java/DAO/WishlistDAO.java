package DAO;

import models.Wishlist;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class WishlistDAO {

    private static final Logger logger = LoggerFactory.getLogger(WishlistDAO.class);

    public Wishlist getByUserId(int userId) {
        logger.info("Executing getByUserId for userId: {}", userId);
        String sql = "SELECT * FROM wishlist WHERE userID = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                Wishlist w = new Wishlist(id);
                w.setUserID(userId);
                w.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                logger.info("Successfully fetched wishlist for userId: {}, wishlistId: {}", userId, id);
                return w;
            }
            logger.info("No wishlist found for userId: {}", userId);
        } catch (Exception e) {
            logger.error("Error in getByUserId for userId: {}", userId, e);
            e.printStackTrace();
        }
        return null;
    }

    public int create(int userId) {
        logger.info("Executing create wishlist for userId: {}", userId);
        String sql = "INSERT INTO wishlist(userID, createdAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("Successfully created wishlist for userId: {}, generated ID: {}", userId, generatedId);
                return generatedId;
            }
            logger.warn("Wishlist inserted but no ID was generated for userId: {}", userId);

        } catch (Exception e) {
            logger.error("Error in create wishlist for userId: {}", userId, e);
            e.printStackTrace();
        }
        return -1;
    }

    public int getOrCreate(int userId) {
        logger.info("Executing getOrCreate wishlist for userId: {}", userId);
        Wishlist w = getByUserId(userId);
        if (w != null) {
            return w.getId();
        }
        logger.info("Wishlist not found for userId: {}. Proceeding to create a new one.", userId);
        return create(userId);
    }
}