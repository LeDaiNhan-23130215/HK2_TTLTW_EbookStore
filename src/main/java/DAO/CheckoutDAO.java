package DAO;

import DTO.PaymentAdminView;
import models.Checkout;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckoutDAO {
    // 1. Tạo checkout và trả về checkoutID
    public int createCheckout(Connection con, Checkout checkout) throws SQLException {
        String sql =
                "INSERT INTO checkout (userID, pmID, totalAmount, checkoutDate, status) " +
                        "VALUES (?, ?, ?, NOW(), ?)";

        try (PreparedStatement ps =
                     con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, checkout.getUserID());
            ps.setInt(2, checkout.getPaymentMethodID());
            ps.setDouble(3, checkout.getTotalAmount());
            ps.setString(4, checkout.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }


    // 2. Update trạng thái checkout
    public boolean updateStatus(Connection conn,int checkoutID, String status) {
        String sql = "UPDATE checkout SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, checkoutID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Lấy checkout theo ID
    public Checkout getCheckoutById(int id) {
        String sql = "SELECT * FROM checkout WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Checkout(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getInt("pmID"),
                        rs.getDouble("totalAmount"),
                        rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Lấy danh sách checkout theo user
    public List<Checkout> getCheckoutsByUser(int userID) {
        List<Checkout> list = new ArrayList<>();
        String sql =
                "SELECT * FROM checkout WHERE userID = ? ORDER BY checkoutDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Checkout(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getInt("pmID"),
                        rs.getDouble("totalAmount"),
                        rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Checkout> getCheckouts() {
        List<Checkout> list = new ArrayList<>();
        String sql =
                "SELECT * FROM checkout";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Checkout(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getInt("pmID"),
                        rs.getDouble("totalAmount"),
                        rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getMonthlyRevenue() {
        String sql =
                "SELECT COALESCE(SUM(totalAmount), 0) " +
                        "FROM checkout " +
                        "WHERE status = 'success' " +
                        "AND MONTH(checkoutDate) = MONTH(CURDATE()) " +
                        "AND YEAR(checkoutDate) = YEAR(CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Map<Integer, Double> getMonthlyRevenue(int year) {
        Map<Integer, Double> result = new LinkedHashMap<>();

        String sql = """
        SELECT MONTH(checkoutDate) AS month,
               SUM(totalAmount) AS revenue
        FROM checkout
        WHERE YEAR(checkoutDate) = ?
        GROUP BY MONTH(checkoutDate)
        ORDER BY month
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(
                        rs.getInt("month"),
                        rs.getDouble("revenue")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int countSuccessOrderThisMonth() {
        String sql =
                "SELECT COUNT(*) FROM checkout " +
                        "WHERE status = 'success' " +
                        "AND MONTH(checkoutDate) = MONTH(CURDATE()) " +
                        "AND YEAR(checkoutDate) = YEAR(CURDATE())";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countSuccessOrder() {
        String sql = "SELECT COUNT(*) FROM checkout WHERE status = 'success'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public PaymentAdminView getPaymentWithUserById(int id) {
        String sql = """
        SELECT 
            c.id AS id,
            u.username,
            pm.name AS method,
            c.totalAmount AS amount,
            c.checkoutDate,
            c.status
        FROM checkout c
        JOIN users u ON c.userID = u.id
        JOIN paymentmethod pm ON c.pmID = pm.id
        WHERE c.id = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PaymentAdminView(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("method"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<PaymentAdminView> getAllPaymentWithUser() {
        List<PaymentAdminView> list = new ArrayList<>();

        String sql = """
        SELECT 
            c.id AS id,
            u.username,
            pm.name AS method,
            c.totalAmount AS amount,
            c.checkoutDate,
            c.status
        FROM checkout c
        JOIN users u ON c.userID = u.id
        JOIN paymentmethod pm ON c.pmID = pm.id
        ORDER BY c.checkoutDate DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PaymentAdminView(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("method"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<Integer, Integer> checkoutPerMonth() {
        Map<Integer, Integer> result = new LinkedHashMap<>();

        String sql = """
        SELECT MONTH(checkoutDate) AS month, COUNT(*) AS total
        FROM checkout
        WHERE YEAR(checkoutDate) = YEAR(CURDATE())
        GROUP BY MONTH(checkoutDate)
        ORDER BY month;
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(
                        rs.getInt("month"),
                        rs.getInt("total")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, Double> revenueByCategory() {
        Map<String, Double> result = new LinkedHashMap<>();

        String sql = """
        SELECT c.categoryName, SUM(co.totalAmount) AS revenue
        FROM checkout co
        JOIN checkoutdetail cd ON co.id = cd.checkoutID
        JOIN ebook e ON cd.bookID = e.id
        JOIN category c ON e.categoryID = c.id
        GROUP BY c.id;
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(
                        rs.getString("categoryName"),
                        rs.getDouble("revenue")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, Double> top5Ebook() {
        Map<String, Double> result = new LinkedHashMap<>();

        String sql = """
        SELECT e.title, SUM(cd.price) AS revenue
        FROM checkoutdetail cd
        JOIN ebook e ON cd.bookID = e.id
        GROUP BY e.id
        ORDER BY revenue DESC
        LIMIT 5;
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(
                        rs.getString("title"),
                        rs.getDouble("revenue")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public boolean hasPurchased(int userId, int ebookId) {
        String sql = """
        SELECT 1
        FROM checkoutdetail cd
        JOIN checkout c ON cd.checkoutID = c.id
        WHERE c.userID = ? AND cd.ebookID = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, ebookId);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
