package DAO;

import models.Cart;
import utils.DBConnection;

import java.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDAO.class);
    private static final String LOG_PREFIX = "[CART_DAO]";

    public Cart getCartByUserId(int userID){
        logger.debug("{} Fetching cart for userID: {}", LOG_PREFIX, userID);
        String sql = "SELECT * FROM cart WHERE userID = ? LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                Cart c = new Cart(id);
                c.setUserID(userID);
                c.setCreatedAt(Timestamp.valueOf(rs.getTimestamp("createdAt").toLocalDateTime()));

                logger.debug("{} Cart found with ID: {} for userID: {}", LOG_PREFIX, id, userID);
                return c;
            }

            logger.info("{} No active cart found for userID: {}", LOG_PREFIX, userID);
        } catch (Exception e) {
            logger.error("{} Error retrieving cart for userID: {}", LOG_PREFIX, userID, e);
        }
        return null;
    }

    public int createCart(int userID){
        logger.info("{} Attempting to create new cart for userID: {}", LOG_PREFIX, userID);
        String sql = "INSERT INTO cart(userID, createdAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userID);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logger.info("{} Successfully created cart with ID: {} for userID: {}", LOG_PREFIX, generatedId, userID);
                return generatedId;
            }

        } catch (Exception e) {
            logger.error("{} Failed to create cart for userID: {}", LOG_PREFIX, userID, e);
        }
        return -1;
    }
}