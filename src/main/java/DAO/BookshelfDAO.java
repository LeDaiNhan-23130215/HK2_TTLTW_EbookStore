package DAO;

import models.Bookshelf;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookshelfDAO {

    private static final Logger logger = LoggerFactory.getLogger(BookshelfDAO.class);
    private static final String LOG_PREFIX = "[BOOKSHELF_DAO]";

    public Bookshelf getOrCreateBookShelf(int userId) {
        logger.debug("{} getOrCreate for userId: {}", LOG_PREFIX, userId);
        Bookshelf bs = getByUserId(userId);
        if (bs != null) {
            return bs;
        }

        logger.info("{} Bookshelf not found, creating new one for userId: {}", LOG_PREFIX, userId);
        int newId = create(userId);
        if (newId > 0) {
            return getByUserId(userId);
        }

        logger.error("{} Failed to get or create bookshelf for userId: {}", LOG_PREFIX, userId);
        return null;
    }

    public Bookshelf getByUserId(int userId) {
        logger.debug("{} Fetching bookshelf for userId: {}", LOG_PREFIX, userId);
        String sql = "SELECT id, userID, addedAt FROM bookshelf WHERE userID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Bookshelf bs = new Bookshelf();
                bs.setId(rs.getInt("id"));
                bs.setUserId(rs.getInt("userID"));
                bs.setAddedAt(rs.getTimestamp("addedAt"));
                return bs;
            }

        } catch (SQLException e) {
            logger.error("{} Error in getByUserId for userId: {}", LOG_PREFIX, userId, e);
        }
        return null;
    }

    public List<Bookshelf> getAllBookshelf() {
        logger.info("{} Fetching all bookshelves", LOG_PREFIX);
        List<Bookshelf> bookshelves = new ArrayList<>();
        String sql = "SELECT id, userID, addedAt FROM bookshelf";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bookshelf bs = new Bookshelf();
                bs.setId(rs.getInt("id"));
                bs.setUserId(rs.getInt("userID"));
                bs.setAddedAt(rs.getTimestamp("addedAt"));
                bookshelves.add(bs);
            }
            logger.debug("{} Successfully retrieved {} bookshelves", LOG_PREFIX, bookshelves.size());
        } catch (SQLException e) {
            logger.error("{} Error in getAllBookshelf", LOG_PREFIX, e);
        }
        return bookshelves;
    }

    public boolean exists(int bookshelfId, int ebookId) {
        logger.debug("{} Checking if ebookId {} exists in bookshelfId {}", LOG_PREFIX, ebookId, bookshelfId);
        String sql = "SELECT 1 FROM bookshelfdetail WHERE bsID = ? AND eBookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            logger.error("{} Error checking existence for bsID: {}, ebookID: {}", LOG_PREFIX, bookshelfId, ebookId, e);
        }
        return false;
    }

    public boolean addBookToBookshelf(int bookshelfId, int ebookId) {
        logger.info("{} Adding ebookId {} to bookshelfId {}", LOG_PREFIX, ebookId, bookshelfId);
        String sql = "INSERT IGNORE INTO bookshelfdetail (bsID, eBookID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);
            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Successfully added ebook {} to bookshelf {}", LOG_PREFIX, ebookId, bookshelfId);
            } else {
                logger.warn("{} Ebook {} already exists or could not be added to bookshelf {}", LOG_PREFIX, ebookId, bookshelfId);
            }
            return success;

        } catch (SQLException e) {
            logger.error("{} Error adding book to bookshelf. bsID: {}, ebookID: {}", LOG_PREFIX, bookshelfId, ebookId, e);
        }
        return false;
    }

    public int create(int userId) {
        logger.info("{} Creating new bookshelf record for userId: {}", LOG_PREFIX, userId);
        String sql = "INSERT INTO bookshelf(userID, addedAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                logger.info("{} Successfully created bookshelf with ID: {} for userId: {}", LOG_PREFIX, newId, userId);
                return newId;
            }

        } catch (SQLException e) {
            logger.error("{} Error creating bookshelf for userId: {}", LOG_PREFIX, userId, e);
        }
        return -1;
    }

    public boolean userOwnsEbook(int userId, int ebookId) {
        logger.debug("{} Checking ownership: userId {} -> ebookId {}", LOG_PREFIX, userId, ebookId);
        String sql = """
        SELECT 1
        FROM bookshelf b
        JOIN bookshelfdetail bd ON b.id = bd.bsID
        WHERE b.userID = ? AND bd.eBookID = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, ebookId);

            boolean owns = ps.executeQuery().next();
            logger.debug("{} Ownership result for userId {}: {}", LOG_PREFIX, userId, owns);
            return owns;

        } catch (SQLException e) {
            logger.error("{} Error checking ownership for userId: {}, ebookID: {}", LOG_PREFIX, userId, ebookId, e);
        }
        return false;
    }
}