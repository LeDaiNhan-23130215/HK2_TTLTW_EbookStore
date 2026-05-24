package DAO;

import models.PasswordReset;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.util.Optional;

public class PasswordResetDAO {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetDAO.class);
    private static final String LOG_PREFIX = "[PASSWORD_RESET_DAO]";

    public void createToken(int userID, String tokenHash, Timestamp expiresAt) {
        logger.info("{} Executing createToken for userID: {}, expiresAt: {}", LOG_PREFIX, userID, expiresAt);
        String sql = "INSERT INTO passwordreset (userID, token, expiresAt) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setString(2, tokenHash);
            ps.setTimestamp(3, expiresAt);
            ps.executeUpdate();
            logger.info("{} Successfully created password reset token for userID: {}", LOG_PREFIX, userID);
        } catch (Exception e) {
            logger.error("{} Error in createToken for userID: {}", LOG_PREFIX, userID, e);
            
        }
    }

    public Optional<PasswordReset> findValidToken(String tokenHash, int userId) {
        logger.info("{} Executing findValidToken for userID: {}", LOG_PREFIX, userId);
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
                if (expiresAt.toInstant().isBefore(Instant.now())) {
                    logger.info("{} Token found for userID: {} but it has expired", LOG_PREFIX, userId);
                    return Optional.empty();
                }
                PasswordReset pr = new PasswordReset(rs.getInt("id"));
                pr.setUserID(rs.getInt("userID"));
                pr.setTokenHash(rs.getString("token"));
                pr.setCreatedAt(rs.getTimestamp("createdAt"));
                pr.setExpiresAt(expiresAt);
                logger.info("{} Successfully found valid token for userID: {}", LOG_PREFIX, userId);
                return Optional.of(pr);
            }
            logger.info("{} No token found for tokenHash and userID: {}", LOG_PREFIX, userId);
        } catch (Exception e) {
            logger.error("{} Error in findValidToken for userID: {}", LOG_PREFIX, userId, e);
        }
        return Optional.empty();
    }

    public boolean isTokenExpired(String tokenHash, int userId) {
        logger.info("{} Executing isTokenExpired for userID: {}", LOG_PREFIX, userId);
        String sql = "SELECT expiresAt FROM passwordreset "
                + "WHERE token = ? AND userID = ? "
                + "ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tokenHash);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean expired = rs.getTimestamp("expiresAt").toInstant().isBefore(Instant.now());
                logger.info("{} Token expiry check result for userID {}: expired = {}", LOG_PREFIX, userId, expired);
                return expired;
            }
            logger.info("{} No token found to check expiry for userID: {}", LOG_PREFIX, userId);
        } catch (Exception e) {
            logger.error("{} Error in isTokenExpired for userID: {}", LOG_PREFIX, userId, e);
            
        }
        return false;
    }

    public void markTokenUsed(int id) {
        logger.info("{} Executing markTokenUsed for token registration ID: {}", LOG_PREFIX, id);
        String sql = "DELETE FROM passwordreset WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("{} Successfully marked token used (deleted) for record ID: {}", LOG_PREFIX, id);
        } catch (Exception e) {
            logger.error("{} Error in markTokenUsed for record ID: {}", LOG_PREFIX, id, e);
        }
    }

    public void deleteByUser(int userID) {
        logger.info("{} Executing deleteByUser for userID: {}", LOG_PREFIX, userID);
        String sql = "DELETE FROM passwordreset WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
            logger.info("{} Successfully cleared all password reset records for userID: {}", LOG_PREFIX, userID);
        } catch (Exception e) {
            logger.error("{} Error in deleteByUser for userID: {}", LOG_PREFIX, userID, e);
            
        }
    }

    public boolean isLocked(int userId) {
        logger.info("{} Executing isLocked checking for userID: {}", LOG_PREFIX, userId);
        String sql = "SELECT lockedUntil FROM passwordreset WHERE userID = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Timestamp locked = rs.getTimestamp("lockedUntil");
                boolean lockedState = locked != null && locked.toInstant().isAfter(Instant.now());
                logger.info("{} Lock check status for userID {}: locked = {}", LOG_PREFIX, userId, lockedState);
                return lockedState;
            }
            logger.info("{} No lock record found for userID: {}", LOG_PREFIX, userId);
        } catch (Exception e) {
            logger.error("{} Error in isLocked for userID: {}", LOG_PREFIX, userId, e);
            
        }
        return false;
    }

    public String getLockRemainingTime(int userId) {
        logger.info("{} Executing getLockRemainingTime for userID: {}", LOG_PREFIX, userId);
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
                String remaining = h > 0 ? h + " giờ " + m + " phút" : m + " phút";
                logger.info("{} Lock remaining time for userID {}: {}", LOG_PREFIX, userId, remaining);
                return remaining;
            }
        } catch (Exception e) {
            logger.error("{} Error in getLockRemainingTime for userID: {}", LOG_PREFIX, userId, e);
        }
        return "0";
    }

    public long getSecondsRemaining(int userId) {
        logger.info("{} Executing getSecondsRemaining for userID: {}", LOG_PREFIX, userId);
        String sql = "SELECT expiresAt FROM passwordreset WHERE userID = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long diff = rs.getTimestamp("expiresAt").toInstant().getEpochSecond()
                        - Instant.now().getEpochSecond();
                long remaining = Math.max(diff, 0);
                logger.info("{} Seconds remaining for token expiration of userID {}: {}s", LOG_PREFIX, userId, remaining);
                return remaining;
            }
        } catch (Exception e) {
            logger.error("{} Error in getSecondsRemaining for userID: {}", LOG_PREFIX, userId, e);
            
        }
        return 0;
    }

    public boolean incrementAttempts(int userId) {
        logger.info("{} Executing incrementAttempts for userID: {}", LOG_PREFIX, userId);
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

            logger.info("{} Current continuous otpAttempts for userID {}: {}", LOG_PREFIX, userId, current);

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
                logger.warn("{} UserID {} has exceeded max attempts. Locked until: {}", LOG_PREFIX, userId, lockUntil);
                return true;
            }

        } catch (Exception e) {
            logger.error("{} Error in incrementAttempts for userID: {}", LOG_PREFIX, userId, e);
            
        }
        return false;
    }

    public void resetAttempts(int userId) {
        logger.info("{} Executing resetAttempts for userID: {}", LOG_PREFIX, userId);
        String sql = "UPDATE passwordreset SET otpAttempts = 0, lockedUntil = NULL WHERE userID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            logger.info("{} Successfully reset OTP attempts and cleared lock status for userID: {}", LOG_PREFIX, userId);
        } catch (Exception e) {
            logger.error("{} Error in resetAttempts for userID: {}", LOG_PREFIX, userId, e);
        }
    }
}