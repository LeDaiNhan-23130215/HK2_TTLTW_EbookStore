package DAO;

import models.CartDetail;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(CartDetailDAO.class);
    private static final String LOG_PREFIX = "[CART_DETAIL_DAO]";

    public boolean addBookToCart(int cartId, int bookId, double price) {
        logger.info("{} Adding bookID: {} to cartID: {} with price: {}", LOG_PREFIX, bookId, cartId, price);
        String sql = "INSERT INTO cartdetail(cartID, bookID, price) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, bookId);
            ps.setDouble(3, price);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully added bookID: {} to cartID: {}", LOG_PREFIX, bookId, cartId);
            }
            return success;

        } catch (Exception e) {
            logger.error("{} Error adding book to cart - cartID: {}, bookID: {}", LOG_PREFIX, cartId, bookId, e);
        }
        return false;
    }

    public List<CartDetail> getCartDetailsByCartID(int cartId) {
        logger.debug("{} Fetching cart details for cartID: {}", LOG_PREFIX, cartId);
        List<CartDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM cartdetail WHERE cartID = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CartDetail cd = new CartDetail(rs.getInt("id"));
                cd.setCartID(rs.getInt("cartID"));
                cd.setBookID(rs.getInt("bookID"));
                cd.setPrice(rs.getDouble("price"));
                list.add(cd);
            }
            logger.debug("{} Retrieved {} items for cartID: {}", LOG_PREFIX, list.size(), cartId);
        } catch (Exception e) {
            logger.error("{} Error retrieving cart details for cartID: {}", LOG_PREFIX, cartId, e);
        }
        return list;
    }


    public boolean updatePrice(int cartId, int bookId, double price) {
        logger.info("{} Updating price for bookID: {} in cartID: {} to {}", LOG_PREFIX, bookId, cartId, price);
        String sql = "UPDATE cartdetail SET price = ? WHERE cartID = ? AND bookID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setInt(2, cartId);
            ps.setInt(3, bookId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully updated price for bookID: {} in cartID: {}", LOG_PREFIX, bookId, cartId);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error updating price for cartID: {}, bookID: {}", LOG_PREFIX, cartId, bookId, e);
        }
        return false;
    }

    public boolean removeItem(int cartId, int bookId) {
        logger.warn("{} Attempting to remove bookID: {} from cartID: {}", LOG_PREFIX, bookId, cartId);
        String sql = "DELETE FROM cartdetail WHERE cartID = ? AND bookID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, bookId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully removed bookID: {} from cartID: {}", LOG_PREFIX, bookId, cartId);
            } else {
                logger.warn("{} No item found to remove - cartID: {}, bookID: {}", LOG_PREFIX, cartId, bookId);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error removing item from cartID: {}, bookID: {}", LOG_PREFIX, cartId, bookId, e);
        }
        return false;
    }

    public boolean isBookInCart(int cartId, int bookId) {
        logger.debug("{} Checking existence of bookID: {} in cartID: {}", LOG_PREFIX, bookId, cartId);
        String sql = "SELECT 1 FROM cartdetail WHERE cartID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, bookId);
            boolean exists = ps.executeQuery().next();
            logger.debug("{} Existence check result for bookID {}: {}", LOG_PREFIX, bookId, exists);
            return exists;

        } catch (Exception e) {
            logger.error("{} Error checking book existence in cartID: {}", LOG_PREFIX, cartId, e);
        }
        return false;
    }

    public boolean removeAllItems(int cartId) {
        logger.warn("{} Clearing all items from cartID: {}", LOG_PREFIX, cartId);
        String sql = "DELETE FROM cartdetail WHERE cartID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);

            int rowsDeleted = ps.executeUpdate();
            logger.info("{} Successfully cleared cartID: {}. Total items removed: {}", LOG_PREFIX, cartId, rowsDeleted);
            return rowsDeleted > 0;
        } catch (SQLException e) {
            logger.error("{} Error clearing cartID: {}", LOG_PREFIX, cartId, e);
        }
        return false;
    }
}