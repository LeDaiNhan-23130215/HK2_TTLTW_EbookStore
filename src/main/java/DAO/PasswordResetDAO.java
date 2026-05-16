package DAO;

import models.PasswordReset;
import utils.DBConnection;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class PasswordResetDAO {
    public void createToken(int userID, String tokenHash, Timestamp expiresAt) {
        String sql = """
            INSERT INTO passwordreset (userID, token, expiresAt)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setString(2, tokenHash);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<PasswordReset> findValidToken(String tokenHash) {
        String sql = """
        SELECT * FROM passwordreset
        WHERE token = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tokenHash);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Timestamp expiresAt = rs.getTimestamp("expiresAt");
                if (expiresAt.toInstant().isBefore(Instant.now())) {
                    return Optional.empty(); // Đã hết hạn
                }
                int id = rs.getInt("id");
                PasswordReset pr = new PasswordReset(id);
                pr.setUserID(rs.getInt("userID"));
                pr.setTokenHash(rs.getString("token"));
                pr.setCreatedAt(rs.getTimestamp("createdAt"));
                pr.setExpiresAt(expiresAt);
                return Optional.of(pr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void markTokenUsed(int id) {

        String sql =
                "DELETE FROM passwordreset WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByUser(int userID) {
        String sql = "DELETE FROM passwordreset WHERE userID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
