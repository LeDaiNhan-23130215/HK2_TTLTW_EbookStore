package DAO;

import DTO.PaymentAdminView;
import models.Checkout;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutDAO {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutDAO.class);
    private static final String LOG_PREFIX = "[CHECKOUT_DAO]";

    public int createCheckout(Connection con, Checkout checkout) throws SQLException {
        logger.info("{} Creating checkout for userID: {}, Amount: {}", LOG_PREFIX, checkout.getUserID(), checkout.getTotalAmount());
        String sql = "INSERT INTO checkout (userID, pmID, totalAmount, checkoutDate, status) VALUES (?, ?, ?, NOW(), ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, checkout.getUserID());
            ps.setInt(2, checkout.getPaymentMethodID());
            ps.setDouble(3, checkout.getTotalAmount());
            ps.setString(4, checkout.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                logger.info("{} Checkout created successfully. ID: {}", LOG_PREFIX, id);
                return id;
            }
        } catch (SQLException e) {
            logger.error("{} SQLException during createCheckout for user {}: {}", LOG_PREFIX, checkout.getUserID(), e.getMessage());
            throw e;
        }
        return -1;
    }

    public boolean updateStatus(Connection conn, int checkoutID, String status) {
        logger.info("{} Updating status for checkoutID: {} to '{}'", LOG_PREFIX, checkoutID, status);
        String sql = "UPDATE checkout SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, checkoutID);

            boolean success = ps.executeUpdate() > 0;
            if (success) {
                logger.info("{} Status updated successfully for ID: {}", LOG_PREFIX, checkoutID);
            }
            return success;
        } catch (SQLException e) {
            logger.error("{} Error updating status for checkoutID {}: {}", LOG_PREFIX, checkoutID, e.getMessage());
        }
        return false;
    }

    public Checkout getCheckoutById(int id) {
        logger.debug("{} Fetching checkout record ID: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM checkout WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Checkout(
                            rs.getInt("id"), rs.getInt("userID"), rs.getInt("pmID"),
                            rs.getDouble("totalAmount"), rs.getTimestamp("checkoutDate"),
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("{} Error fetching checkout by ID {}: {}", LOG_PREFIX, id, e.getMessage());
        }
        return null;
    }

    public List<Checkout> getCheckoutsByUser(int userID) {
        logger.debug("{} Fetching checkout history for userID: {}", LOG_PREFIX, userID);
        List<Checkout> list = new ArrayList<>();
        String sql = "SELECT * FROM checkout WHERE userID = ? ORDER BY checkoutDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Checkout(
                            rs.getInt("id"), rs.getInt("userID"), rs.getInt("pmID"),
                            rs.getDouble("totalAmount"), rs.getTimestamp("checkoutDate"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("{} Error fetching checkouts for user {}: {}", LOG_PREFIX, userID, e.getMessage());
        }
        return list;
    }

    public List<Checkout> getCheckouts() {
        logger.info("{} Fetching all checkout records", LOG_PREFIX);
        List<Checkout> list = new ArrayList<>();
        String sql = "SELECT * FROM checkout ORDER BY checkoutDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Checkout(
                        rs.getInt("id"), rs.getInt("userID"), rs.getInt("pmID"),
                        rs.getDouble("totalAmount"), rs.getTimestamp("checkoutDate"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            logger.error("{} Error fetching all checkouts: {}", LOG_PREFIX, e.getMessage());
        }
        return list;
    }

    public double getMonthlyRevenue() {
        logger.debug("{} Calculating monthly revenue", LOG_PREFIX);
        String sql = """
            SELECT COALESCE(SUM(totalAmount), 0) FROM checkout 
            WHERE status = 'success' 
            AND MONTH(checkoutDate) = MONTH(CURDATE()) 
            AND YEAR(checkoutDate) = YEAR(CURDATE())
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException e) {
            logger.error("{} Error calculating monthly revenue: {}", LOG_PREFIX, e.getMessage());
        }
        return 0;
    }

    public Map<Integer, Double> getMonthlyRevenue(int year) {
        logger.info("{} Fetching monthly revenue report for year: {}", LOG_PREFIX, year);
        Map<Integer, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT MONTH(checkoutDate) AS month, SUM(totalAmount) AS revenue
            FROM checkout WHERE YEAR(checkoutDate) = ? AND status = 'success'
            GROUP BY MONTH(checkoutDate) ORDER BY month
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("month"), rs.getDouble("revenue"));
                }
            }
        } catch (Exception e) {
            logger.error("{} Error in yearly revenue report: {}", LOG_PREFIX, e.getMessage());
        }
        return result;
    }

    public int countSuccessOrderThisMonth() {
        String sql = "SELECT COUNT(*) FROM checkout WHERE status = 'success' " +
                "AND MONTH(checkoutDate) = MONTH(CURDATE()) AND YEAR(checkoutDate) = YEAR(CURDATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            logger.error("{} Error counting monthly success orders: {}", LOG_PREFIX, e.getMessage());
        }
        return 0;
    }

    public int countSuccessOrder() {
        String sql = "SELECT COUNT(*) FROM checkout WHERE status = 'success'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            logger.error("{} Error counting total success orders: {}", LOG_PREFIX, e.getMessage());
        }
        return 0;
    }

    public PaymentAdminView getPaymentWithUserById(int id) {
        logger.debug("{} Fetching PaymentAdminView for ID: {}", LOG_PREFIX, id);
        String sql = """
            SELECT c.id, u.username, pm.name AS method, c.totalAmount AS amount, c.checkoutDate, c.status
            FROM checkout c
            JOIN users u ON c.userID = u.id
            JOIN paymentmethod pm ON c.pmID = pm.id
            WHERE c.id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PaymentAdminView(
                            rs.getInt("id"), rs.getString("username"), rs.getString("method"),
                            rs.getDouble("amount"), rs.getTimestamp("checkoutDate"), rs.getString("status")
                    );
                }
            }
        } catch (Exception e) {
            logger.error("{} Error in getPaymentWithUserById: {}", LOG_PREFIX, e.getMessage());
        }
        return null;
    }

    public List<PaymentAdminView> getAllPaymentWithUser() {
        logger.info("{} Fetching all payments for Admin View", LOG_PREFIX);
        List<PaymentAdminView> list = new ArrayList<>();
        String sql = """
            SELECT c.id, u.username, pm.name AS method, c.totalAmount AS amount, c.checkoutDate, c.status
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
                        rs.getInt("id"), rs.getString("username"), rs.getString("method"),
                        rs.getDouble("amount"), rs.getTimestamp("checkoutDate"), rs.getString("status")
                ));
            }
        } catch (Exception e) {
            logger.error("{} Error in getAllPaymentWithUser: {}", LOG_PREFIX, e.getMessage());
        }
        return list;
    }

    public Map<Integer, Integer> checkoutPerMonth(int year) {
        logger.debug("{} Fetching checkout count per month", LOG_PREFIX);
        Map<Integer, Integer> result = new LinkedHashMap<>();
        String sql = """
            SELECT MONTH(checkoutDate) AS month, COUNT(*) AS total
            FROM checkout WHERE YEAR(checkoutDate) = ?
            GROUP BY MONTH(checkoutDate) ORDER BY month
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
            ) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getInt("month"), rs.getInt("total"));
            }
        } catch (Exception e) {
            logger.error("{} Error in checkoutPerMonth: {}", LOG_PREFIX, e.getMessage());
        }
        return result;
    }

    public Map<String, Double> revenueByCategory(int year) {
        logger.info("{} Fetching revenue distribution by category", LOG_PREFIX);
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT cat.categoryName, SUM(cd.price) AS revenue
            FROM checkout co
            JOIN checkoutdetail cd ON co.id = cd.checkoutID
            JOIN ebook e ON cd.bookID = e.id
            JOIN category cat ON e.categoryID = cat.id
            WHERE co.status = 'success'
            AND YEAR(co.checkoutDate) = ?
            GROUP BY cat.id
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("categoryName"), rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            logger.error("{} Error in revenueByCategory: {}", LOG_PREFIX, e.getMessage());
        }
        return result;
    }

    public Map<String, Double> top5Ebook(int year) {
        logger.info("{} Fetching top 5 ebooks by revenue", LOG_PREFIX);
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT e.title, SUM(cd.price) AS revenue
            FROM checkoutdetail cd
            JOIN ebook e ON cd.bookID = e.id
            JOIN checkout c ON cd.checkoutID = c.id
            WHERE c.status = 'success'
            AND YEAR(c.checkoutDate) = ?
            GROUP BY e.id ORDER BY revenue DESC LIMIT 5
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("title"), rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            logger.error("{} Error in top5Ebook: {}", LOG_PREFIX, e.getMessage());
        }
        return result;
    }

    public Map<String, Double> bot5Ebook(int year) {
        logger.info("{} Fetching bot 5 ebooks by revenue", LOG_PREFIX);
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = """
            SELECT e.title, SUM(cd.price) AS revenue
            FROM checkoutdetail cd
            JOIN ebook e ON cd.bookID = e.id
            JOIN checkout c ON cd.checkoutID = c.id
            WHERE c.status = 'success'
            AND YEAR(c.checkoutDate) = ?
            GROUP BY e.id ORDER BY revenue ASC LIMIT 5
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("title"), rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            logger.error("{} Error in bot5Ebook: {}", LOG_PREFIX, e.getMessage());
        }
        return result;
    }

    public boolean hasPurchased(int userId, int ebookId) {
        logger.debug("{} Checking purchase: User {} -> Ebook {}", LOG_PREFIX, userId, ebookId);
        String sql = """
            SELECT 1 FROM checkoutdetail cd
            JOIN checkout c ON cd.checkoutID = c.id
            WHERE c.userID = ? AND cd.ebookID = ? AND c.status = 'success'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, ebookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            logger.error("{} Error in hasPurchased: {}", LOG_PREFIX, e.getMessage());
        }
        return false;
    }

    public List<Integer> getAvailableYears() {
        String sql = """
        SELECT DISTINCT YEAR(checkoutDate) AS year
        FROM checkout
        ORDER BY year
    """;

        List<Integer> years = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                years.add(rs.getInt("year"));
            }

        } catch (Exception e) {
            logger.error("{} Can't find available year", LOG_PREFIX, e);
        }

        return years;
    }
}