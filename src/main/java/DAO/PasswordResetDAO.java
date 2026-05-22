package DAO;

import models.PasswordReset;
import utils.DBConnection;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class PasswordResetDAO {

    public void createToken(int userID, String tokenHash, Timestamp expiresAt) {
        String sql = "INSERT INTO passwordreset (userID, token, expiresAt) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setString(2, tokenHash);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Optional<PasswordReset> findValidToken(String tokenHash, int userId) {
        // Thêm AND userID = ? để chắc chắn đúng user
        String sql = "SELECT * FROM passwordreset "
                + "WHERE token = ? AND userID = ? "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp expiresAt = rs.getTimestamp("expiresAt");
                if (expiresAt.toInstant().isBefore(Instant.now())) return Optional.empty();
                PasswordReset pr = new PasswordReset(rs.getInt("id"));
                pr.setUserID(rs.getInt("userID"));
                pr.setTokenHash(rs.getString("token"));
                pr.setCreatedAt(rs.getTimestamp("createdAt"));
                pr.setExpiresAt(expiresAt);
                return Optional.of(pr);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    public boolean isTokenExpired(String tokenHash, int userId) {
        String sql = "SELECT expiresAt FROM passwordreset "
                + "WHERE token = ? AND userID = ? "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("expiresAt").toInstant().isBefore(Instant.now());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void markTokenUsed(int id) {
        String sql = "DELETE FROM passwordreset WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteByUser(int userID) {
        String sql = "DELETE FROM passwordreset WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean isLocked(int userId) {
        String sql = "SELECT lockedUntil FROM passwordreset WHERE userID = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp locked = rs.getTimestamp("lockedUntil");
                return locked != null && locked.toInstant().isAfter(Instant.now());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public String getLockRemainingTime(int userId) {
        String sql = "SELECT lockedUntil FROM passwordreset WHERE userID = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp locked = rs.getTimestamp("lockedUntil");
                if (locked == null) return "0";
                long seconds = locked.toInstant().getEpochSecond() - Instant.now().getEpochSecond();
                if (seconds <= 0) return "0";
                long h = seconds / 3600;
                long m = (seconds % 3600) / 60;
                return h > 0 ? h + " giờ " + m + " phút" : m + " phút";
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "0";
    }

    public long getSecondsRemaining(int userId) {
        String sql = "SELECT expiresAt FROM passwordreset WHERE userID = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long diff = rs.getTimestamp("expiresAt").toInstant().getEpochSecond()
                        - Instant.now().getEpochSecond();
                return Math.max(diff, 0);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public boolean incrementAttempts(int userId) {
        String sql = "UPDATE passwordreset "
                + "SET otpAttempts = otpAttempts + 1 "
                + "WHERE userID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            int current = 0;
            String selectSql = "SELECT otpAttempts FROM passwordreset "
                    + "WHERE userID = ? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) current = rs.getInt("otpAttempts");
            }

            if (current >= 5) {
                Timestamp lockUntil = Timestamp.from(
                        Instant.now().plus(12, java.time.temporal.ChronoUnit.HOURS));
                String lockSql = "UPDATE passwordreset "
                        + "SET lockedUntil = ? WHERE userID = ?";
                try (PreparedStatement ps = conn.prepareStatement(lockSql)) {
                    ps.setTimestamp(1, lockUntil);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
                return true;
            }

        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void resetAttempts(int userId) {
        String sql = "UPDATE passwordreset SET otpAttempts = 0, lockedUntil = NULL WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}