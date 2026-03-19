package DAO;

import models.CartDetail;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartDetailDAO {
    public boolean addBookToCart(int cartId, int bookId, double price) {
        String sql = "INSERT INTO cartdetail(cartID, bookID, price) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, bookId);
            ps.setDouble(3, price);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<CartDetail> getCartDetailsByCartID(int cartId) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean updatePrice(int cartId, int bookId, double price) {
        String sql = "UPDATE cartdetail SET price = ? WHERE cartID = ? AND bookID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, price);
            ps.setInt(2, cartId);
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeItem(int cartId, int bookId) {
        String sql = "DELETE FROM cartdetail WHERE cartID = ? AND bookID = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, bookId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isBookInCart(int cartId, int bookId) {
        String sql = "SELECT 1 FROM cartdetail WHERE cartID=? AND bookID=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, bookId);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeAllItems(int cartId) {
        String sql = "DELETE FROM cartdetail WHERE cartID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
