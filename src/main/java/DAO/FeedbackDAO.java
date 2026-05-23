package DAO;

import DTO.FeedbackAdminView;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackDAO.class);

    public FeedbackAdminView getFeedbackWithUserById(int id) {
        logger.info("Executing getFeedbackWithUserById for id: {}", id);
        String sql = """
        SELECT f.id, f.userID, f.message, f.createdAt, f.status,
               u.userName, u.email
        FROM feedback f
        JOIN users u ON f.userID = u.id
        WHERE f.id = ?
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                logger.info("Successfully fetched feedback for id: {}", id);
                return new FeedbackAdminView(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("message"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status")
                );
            }
            logger.info("No feedback found for id: {}", id);

        } catch (Exception e) {
            logger.error("Error in getFeedbackWithUserById for id: {}", id, e);
            e.printStackTrace();
        }
        return null;
    }

    public List<FeedbackAdminView> getAllFeedbackWithUser() {
        logger.info("Executing getAllFeedbackWithUser");
        List<FeedbackAdminView> list = new ArrayList<>();

        String sql = """
        SELECT 
            f.id, f.userID, f.message, f.createdAt, f.status,
            u.userName, u.email
        FROM feedback f
        JOIN users u ON f.userID = u.id
        ORDER BY f.createdAt DESC
    """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                FeedbackAdminView f = new FeedbackAdminView(
                        rs.getInt("id"),
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("message"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("status")
                );
                list.add(f);
            }
            logger.info("Successfully fetched {} feedback records", list.size());

        } catch (Exception e) {
            logger.error("Error in getAllFeedbackWithUser", e);
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteFeedback(int id) {
        logger.info("Executing deleteFeedback for id: {}", id);
        String sql = "DELETE FROM feedback WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean result = ps.executeUpdate() > 0;
            logger.info("Delete feedback status for id {}: {}", id, result);
            return result;

        } catch (Exception e) {
            logger.error("Error in deleteFeedback for id: {}", id, e);
            e.printStackTrace();
        }

        return false;
    }

    public boolean markAsRead(int id) {
        logger.info("Executing markAsRead for id: {}", id);
        String sql = "UPDATE feedback SET status = 1 WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean result = ps.executeUpdate() > 0;
            logger.info("Mark as read status for id {}: {}", id, result);
            return result;

        } catch (Exception e) {
            logger.error("Error in markAsRead for id: {}", id, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertFeedback(int userID, String message) {
        logger.info("Executing insertFeedback for userID: {}", userID);
        String sql = "INSERT INTO feedback (userID, message) VALUES (?, ?);";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {

            ps.setInt(1,userID);
            ps.setString(2, message);
            ps.executeUpdate();
            logger.info("Successfully inserted feedback for userID: {}", userID);
            return true;

        } catch (Exception e) {
            logger.error("Error in insertFeedback for userID: {}", userID, e);
            e.printStackTrace();
        }
        return false;
    }
}