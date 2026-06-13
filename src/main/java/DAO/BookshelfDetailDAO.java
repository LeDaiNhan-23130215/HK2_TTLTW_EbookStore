package DAO;

import models.Ebook;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookshelfDetailDAO {

    private static final Logger logger = LoggerFactory.getLogger(BookshelfDetailDAO.class);
    private static final String LOG_PREFIX = "[BOOKSHELF_DETAIL_DAO]";

    public boolean addBook(int bookshelfId, int ebookId) {
        logger.info("{} Attempting to add ebookId {} to bookshelfId {}", LOG_PREFIX, ebookId, bookshelfId);
        String sql = """
            INSERT INTO bookshelfdetail (bsID, eBookID, addedAt)
            VALUES (?, ?, CURRENT_TIMESTAMP)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully added ebook {} to bookshelf {}", LOG_PREFIX, ebookId, bookshelfId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("{} Error adding ebook {} to bookshelf {}: {}", LOG_PREFIX, ebookId, bookshelfId, e.getMessage());
        }
        return false;
    }

    public boolean exists(int bookshelfId, int ebookId) {
        logger.debug("{} Checking if ebookId {} exists in bookshelfId {}", LOG_PREFIX, ebookId, bookshelfId);
        String sql = """
            SELECT 1 FROM bookshelfdetail
            WHERE bsID = ? AND eBookID = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);

            ResultSet rs = ps.executeQuery();
            boolean exists = rs.next();
            logger.debug("{} Existence check for ebookId {}: {}", LOG_PREFIX, ebookId, exists);
            return exists;

        } catch (SQLException e) {
            logger.error("{} Error checking existence for ebookId {} in bookshelf {}: {}", LOG_PREFIX, ebookId, bookshelfId, e.getMessage());
        }
        return false;
    }

    public List<Ebook> getBooksByBookshelfId(int bookshelfId) {
        logger.info("{} Fetching books for bookshelfId: {}", LOG_PREFIX, bookshelfId);
        List<Ebook> list = new ArrayList<>();

        String sql = """
            SELECT e.id, e.title, e.price
            FROM bookshelfdetail bd
            JOIN ebook e ON bd.eBookID = e.id
            WHERE bd.bsID = ?
            ORDER BY bd.addedAt DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                Ebook e = new Ebook(id);
                e.setTitle(rs.getString("title"));
                e.setPrice(rs.getDouble("price"));
                list.add(e);
            }
            logger.debug("{} Retrieved {} books for bookshelfId {}", LOG_PREFIX, list.size(), bookshelfId);

        } catch (SQLException e) {
            logger.error("{} Error fetching books for bookshelfId {}: {}", LOG_PREFIX, bookshelfId, e.getMessage());
        }
        return list;
    }

    public List<Ebook> getBooksByUser(int userId) {
        logger.info("{} Fetching books for userId: {}", LOG_PREFIX, userId);
        List<Ebook> books = new ArrayList<>();

        String sql = """
                SELECT e.id, e.title, e.price
                FROM bookshelfdetail bd
                JOIN bookshelf b ON bd.bsID = b.id
                JOIN ebook e ON bd.ebookID = e.id
                WHERE b.userID = ?
                ORDER BY bd.addedAt DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Ebook ebook = new Ebook(id);
                    ebook.setTitle(rs.getString("title"));
                    ebook.setPrice(rs.getDouble("price"));

                    books.add(ebook);
                }
            }
            logger.debug("{} Retrieved {} books for userId {}", LOG_PREFIX, books.size(), userId);

        } catch (Exception e) {
            logger.error("{} Error fetching books for userId {}: {}", LOG_PREFIX, userId, e.getMessage());
        }

        return books;
    }

    public boolean removeBook(int bookshelfId, int ebookId) {
        logger.warn("{} Attempting to remove ebookId {} from bookshelfId {}", LOG_PREFIX, ebookId, bookshelfId);
        String sql = "DELETE FROM bookshelfdetail WHERE bsID = ? AND eBookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully removed ebook {} from bookshelf {}", LOG_PREFIX, ebookId, bookshelfId);
            } else {
                logger.warn("{} Failed to remove ebook {} (not found in bookshelf {})", LOG_PREFIX, ebookId, bookshelfId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("{} Error removing ebook {} from bookshelf {}: {}", LOG_PREFIX, ebookId, bookshelfId, e.getMessage());
        }
        return false;
    }
}