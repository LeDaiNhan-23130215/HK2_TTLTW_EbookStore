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

    // Lấy số lần nhập sai và thời gian khoá theo email
    public int getOtpAttempts(int userId) {
        String sql = "SELECT otpAttempts FROM passwordreset "
                + "WHERE userID = ? AND isUsed = 0 "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("otpAttempts");
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // Kiểm tra email đang bị khoá
    public boolean isLocked(int userId) {
        String sql = "SELECT lockedUntil FROM passwordreset "
                + "WHERE userID = ? AND isUsed = 0 "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp locked = rs.getTimestamp("lockedUntil");
                return locked != null
                        && locked.toInstant().isAfter(Instant.now());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public String getLockRemainingTime(int userId) {
        String sql = "SELECT lockedUntil FROM passwordreset "
                + "WHERE userID = ? AND isUsed = 0 "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp locked = rs.getTimestamp("lockedUntil");
                if (locked == null) return "0";
                long seconds = locked.toInstant().getEpochSecond()
                        - Instant.now().getEpochSecond();
                if (seconds <= 0) return "0";
                long h = seconds / 3600;
                long m = (seconds % 3600) / 60;
                return h > 0 ? h + " giờ " + m + " phút" : m + " phút";
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "0";
    }

    public void incrementAttempts(int userId) {
        String sql = "UPDATE passwordreset "
                + "SET otpAttempts = otpAttempts + 1, "
                + "    lockedUntil = CASE "
                + "        WHEN otpAttempts + 1 >= 5 "
                + "        THEN DATE_ADD(NOW(), INTERVAL 12 HOUR) "
                + "        ELSE lockedUntil END "
                + "WHERE userID = ? AND isUsed = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void resetAttempts(int userId) {
        String sql = "UPDATE passwordreset "
                + "SET otpAttempts = 0, lockedUntil = NULL "
                + "WHERE userID = ? AND isUsed = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean isTokenExpired(String tokenHash) {
        String sql = "SELECT expiresAt FROM passwordreset "
                + "WHERE tokenHash = ? AND isUsed = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("expiresAt")
                        .toInstant().isBefore(Instant.now());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
