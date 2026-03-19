package DAO;

import models.Bookshelf;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookshelfDAO {
    public Bookshelf getOrCreateBookShelf(int userId) {
        Bookshelf bs = getByUserId(userId);
        if (bs != null) return bs;

        int newId = create(userId);
        if (newId > 0) {
            return getByUserId(userId);
        }
        return null;
    }


    public Bookshelf getByUserId(int userId) {
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
            e.printStackTrace();
        }
        return null;
    }


    public List<Bookshelf> getAllBookshelf() {
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookshelves;
    }

    public boolean exists(int bookshelfId, int ebookId) {
        String sql = "SELECT 1 FROM bookshelfdetail WHERE bsID = ? AND eBookID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addBookToBookshelf(int bookshelfId, int ebookId) {
        String sql = "INSERT IGNORE INTO bookshelfdetail (bsID, eBookID) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookshelfId);
            ps.setInt(2, ebookId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int create(int userId) {
        String sql = "INSERT INTO bookshelf(userID, addedAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // trả về bookshelfId
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean userOwnsEbook(int userId, int ebookId) {
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

            return ps.executeQuery().next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
