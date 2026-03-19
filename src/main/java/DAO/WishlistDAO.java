package DAO;

import models.Wishlist;
import utils.DBConnection;

import java.sql.*;

public class WishlistDAO {
    public Wishlist getByUserId(int userId) {
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
                return w;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int create(int userId) {
        String sql = "INSERT INTO wishlist(userID, createdAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getOrCreate(int userId) {
        Wishlist w = getByUserId(userId);
        if (w != null) return w.getId();
        return create(userId);
    }
}
