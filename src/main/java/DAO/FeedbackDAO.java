package DAO;

import DTO.FeedbackAdminView;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    public FeedbackAdminView getFeedbackWithUserById(int id) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FeedbackAdminView> getAllFeedbackWithUser() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deleteFeedback(int id) {
        String sql = "DELETE FROM feedback WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean markAsRead(int id) {
        String sql = "UPDATE feedback SET status = 1 WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertFeedback(int userID, String message) {
        String sql = "INSERT INTO feedback (userID, message) VALUES (?, ?);";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {

            ps.setInt(1,userID);
            ps.setString(2, message);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
