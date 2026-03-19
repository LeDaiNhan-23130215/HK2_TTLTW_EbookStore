package DAO;

import models.Cart;
import utils.DBConnection;

import java.sql.*;

public class CartDAO {
    public Cart getCartByUserId(int userID){
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
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int createCart(int userID){
        String sql = "INSERT INTO cart(userID, createdAt) VALUES (?, CURRENT_TIMESTAMP)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userID);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
