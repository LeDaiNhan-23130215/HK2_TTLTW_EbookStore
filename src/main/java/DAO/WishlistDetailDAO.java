package DAO;
import models.Ebook;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class WishlistDetailDAO {
    public boolean exists(int wishlistId, int bookId) {
        String sql = "SELECT 1 FROM wishlistdetail WHERE wishlistID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addBookToWishlist(int wishlistId, int bookId) {
        String sql = "INSERT INTO wishlistdetail(wishlistID, bookID, addedAt) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeBookInWishlist(int wishlistId, int bookId) {
        String sql = "DELETE FROM wishlistdetail WHERE wishlistID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, wishlistId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Ebook> getBooksByUser(int userId) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Ebook> getBooksByWishlistId(int wishlistId) {
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
