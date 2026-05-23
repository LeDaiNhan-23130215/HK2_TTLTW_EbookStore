package DAO;

import models.Ebook;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(WishlistDetailDAO.class);

    public boolean exists(int wishlistId, int bookId) {
        logger.info("Executing exists check for wishlistId: {}, bookId: {}", wishlistId, bookId);
        String sql = "SELECT 1 FROM wishlistdetail WHERE wishlistID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            boolean hasNext = ps.executeQuery().next();
            logger.info("Wishlist item existence for wishlistId {} and bookId {}: {}", wishlistId, bookId, hasNext);
            return hasNext;

        } catch (Exception e) {
            logger.error("Error in exists for wishlistId: {}, bookId: {}", wishlistId, bookId, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean addBookToWishlist(int wishlistId, int bookId) {
        logger.info("Executing addBookToWishlist for wishlistId: {}, bookId: {}", wishlistId, bookId);
        String sql = "INSERT INTO wishlistdetail(wishlistID, bookID, addedAt) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            boolean result = ps.executeUpdate() > 0;
            logger.info("Add book to wishlist status for wishlistId {} and bookId {}: {}", wishlistId, bookId, result);
            return result;

        } catch (Exception e) {
            logger.error("Error in addBookToWishlist for wishlistId: {}, bookId: {}", wishlistId, bookId, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeBookInWishlist(int wishlistId, int bookId) {
        logger.info("Executing removeBookInWishlist for wishlistId: {}, bookId: {}", wishlistId, bookId);
        String sql = "DELETE FROM wishlistdetail WHERE wishlistID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            boolean result = ps.executeUpdate() > 0;
            logger.info("Remove book from wishlist status for wishlistId {} and bookId {}: {}", wishlistId, bookId, result);
            return result;

        } catch (Exception e) {
            logger.error("Error in removeBookInWishlist for wishlistId: {}, bookId: {}", wishlistId, bookId, e);
            e.printStackTrace();
        }
        return false;
    }

    public List<Ebook> getBooksByUser(int userId) {
        logger.info("Executing getBooksByUser for userId: {}", userId);
        List<Ebook> list = new ArrayList<>();

        String sql = """
            SELECT e.*
            FROM wishlist w
            JOIN wishlistdetail wd ON w.id = wd.wishlistID
            JOIN ebook e ON wd.bookID = e.id
            WHERE w.userID = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Ebook e = new Ebook(id);
                e.setTitle(rs.getString("title"));
                e.setPrice(rs.getDouble("price"));
                list.add(e);
            }
            logger.info("Successfully fetched {} books from wishlist for userId: {}", list.size(), userId);
        } catch (Exception e) {
            logger.error("Error in getBooksByUser for userId: {}", userId, e);
            e.printStackTrace();
        }
        return list;
    }

    public List<Ebook> getBooksByWishlistId(int wishlistId) {
        logger.info("Executing getBooksByWishlistId for wishlistId: {}", wishlistId);
        List<Ebook> list = new ArrayList<>();
        String sql = """
            SELECT e.*
            FROM wishlistdetail wd
            JOIN ebook e ON wd.bookID = e.id
            WHERE wd.wishlistID = ?
        """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, wishlistId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Ebook e = new Ebook(id);
                e.setTitle(rs.getString("title"));
                e.setPrice(rs.getDouble("price"));
                e.setBookCode(rs.getString("eBookCode"));
                list.add(e);
            }
            logger.info("Successfully fetched {} books for wishlistId: {}", list.size(), wishlistId);
        } catch (Exception e) {
            logger.error("Error in getBooksByWishlistId for wishlistId: {}", wishlistId, e);
            e.printStackTrace();
        }
        return list;
    }
}