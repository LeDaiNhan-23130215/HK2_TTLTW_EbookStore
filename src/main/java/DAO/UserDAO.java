package DAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import DTO.LoginOutcome;
import enums.LoginResult;
import models.User;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserDAO {

    private static final Logger logger     = LoggerFactory.getLogger(UserDAO.class);
    private static final String LOG_PREFIX = "[USER_DAO]";

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("id"));
        user.setUsername(rs.getString("userName"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNum(rs.getString("phoneNum"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        try {
            user.setProvider(rs.getString("provider"));
            user.setProviderId(rs.getString("provider_id"));
        } catch (SQLException ignored) {}
        return user;
    }


    public LoginOutcome attemptLogin(String usernameOrEmail, String password) {
        logger.info("{} attemptLogin for: {}", LOG_PREFIX, usernameOrEmail);
        String sql = "SELECT * FROM users WHERE (userName = ? OR email = ?) LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                logger.warn("{} Login: user not found for '{}'", LOG_PREFIX, usernameOrEmail);
                return LoginOutcome.of(LoginResult.USER_NOT_FOUND);
            }

            String hashed = rs.getString("password");

            if (hashed == null || hashed.isBlank()) {
                logger.warn("{} Login: OAuth-only account for '{}'", LOG_PREFIX, usernameOrEmail);
                return LoginOutcome.of(LoginResult.OAUTH_ACCOUNT);
            }

            BCrypt.Result bcr = BCrypt.verifyer().verify(password.toCharArray(), hashed);
            if (bcr.verified) {
                logger.info("{} Login success for '{}'", LOG_PREFIX, usernameOrEmail);
                return LoginOutcome.success(mapUser(rs));
            }

            logger.warn("{} Login: wrong password for '{}'", LOG_PREFIX, usernameOrEmail);
            return LoginOutcome.of(LoginResult.WRONG_PASSWORD);

        } catch (Exception e) {
            logger.error("{} Error in attemptLogin for '{}': ", LOG_PREFIX, usernameOrEmail, e);
            return LoginOutcome.of(LoginResult.ERROR);
        }
    }

    public User login(String usernameOrEmail, String password) {
        LoginOutcome outcome = attemptLogin(usernameOrEmail, password);
        return outcome.isSuccess() ? outcome.getUser() : null;
    }


    public boolean checkAdminLogin(String usernameOrEmail, String password) {
        logger.info("{} checkAdminLogin for: {}", LOG_PREFIX, usernameOrEmail);
        String sql = "SELECT password FROM users WHERE (userName=? OR email=?) AND role='admin'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, usernameOrEmail);
            stm.setString(2, usernameOrEmail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashed);
                logger.info("{} Admin verification for '{}': {}", LOG_PREFIX, usernameOrEmail, result.verified);
                return result.verified;
            }
            logger.warn("{} Admin not found for: {}", LOG_PREFIX, usernameOrEmail);
        } catch (SQLException e) {
            logger.error("{} Error in checkAdminLogin for: {}", LOG_PREFIX, usernameOrEmail, e);
        }
        return false;
    }

    public boolean signUp(String userName, String email,
                          String phoneNum, String password) {
        logger.info("{} signUp for userName={}, email={}", LOG_PREFIX, userName, email);
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) "
                + "VALUES(?, ?, ?, ?, 'user')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            String hash = BCrypt.withDefaults().hashToString(10, password.toCharArray());
            stm.setString(1, userName);
            stm.setString(2, email);
            stm.setString(3, phoneNum);
            stm.setString(4, hash);
            boolean result = stm.executeUpdate() > 0;
            logger.info("{} signUp status for '{}': {}", LOG_PREFIX, userName, result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in signUp for userName: {}", LOG_PREFIX, userName, e);
        }
        return false;
    }

    public User findByProvider(String provider, String providerId) {
        logger.info("{} findByProvider: provider={}, providerId={}", LOG_PREFIX, provider, providerId);
        String sql = "SELECT * FROM users WHERE provider = ? AND provider_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, provider);
            ps.setString(2, providerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("{} Found user by provider={}", LOG_PREFIX, provider);
                return mapUser(rs);
            }
            logger.info("{} No user found for provider={}, providerId={}", LOG_PREFIX, provider, providerId);
        } catch (Exception e) {
            logger.error("{} Error in findByProvider: ", LOG_PREFIX, e);
        }
        return null;
    }

    public User createOAuthUser(String displayName, String email,
                                String provider, String providerId) {
        logger.info("{} createOAuthUser: displayName='{}', email={}, provider={}",
                LOG_PREFIX, displayName, email, provider);

        String username = generateUniqueUsername(email);

        String sql = "INSERT INTO users(userName, email, phoneNum, password, role, provider, provider_id) "
                + "VALUES(?, ?, NULL, NULL, 'user', ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, provider);
            ps.setString(4, providerId);
            boolean inserted = ps.executeUpdate() > 0;
            if (!inserted) {
                logger.error("{} INSERT returned 0 rows for email={}", LOG_PREFIX, email);
                return null;
            }
            logger.info("{} createOAuthUser succeeded: username='{}', email={}", LOG_PREFIX, username, email);
            return findUserByEmail(email);
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.error("{} UNIQUE constraint violation creating OAuth user for email={}: {}",
                    LOG_PREFIX, email, e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("{} Error in createOAuthUser for email={}: ", LOG_PREFIX, email, e);
            return null;
        }
    }

    private String generateUniqueUsername(String email) {
        String base = email.contains("@")
                ? email.substring(0, email.indexOf('@'))
                : email;
        base = base.replaceAll("[^a-zA-Z0-9_]", "").toLowerCase();
        if (base.isEmpty()) base = "user";
        if (base.length() > 20) base = base.substring(0, 20);

        if (!existsByUsername(base)) {
            logger.debug("{} Generated username (no suffix needed): '{}'", LOG_PREFIX, base);
            return base;
        }

        Random rng = new Random();
        for (int i = 0; i < 10; i++) {
            String candidate = base + (1000 + rng.nextInt(9000));
            if (!existsByUsername(candidate)) {
                logger.debug("{} Generated username with suffix: '{}'", LOG_PREFIX, candidate);
                return candidate;
            }
        }

        String fallback = base + System.currentTimeMillis();
        logger.warn("{} All suffix attempts failed, using timestamp fallback: '{}'", LOG_PREFIX, fallback);
        return fallback;
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE userName = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (Exception e) {
            logger.error("{} Error in existsByUsername for '{}': ", LOG_PREFIX, username, e);
            return false;
        }
    }

    public boolean linkOAuthAccount(int userId, String provider, String providerId) {
        logger.info("{} linkOAuthAccount: userId={}, provider={}", LOG_PREFIX, userId, provider);
        String sql = "UPDATE users SET provider = ?, provider_id = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, provider);
            ps.setString(2, providerId);
            ps.setInt(3, userId);
            boolean result = ps.executeUpdate() > 0;
            logger.info("{} linkOAuthAccount status for userId {}: {}", LOG_PREFIX, userId, result);
            return result;
        } catch (Exception e) {
            logger.error("{} Error in linkOAuthAccount for userId: {}", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public User getUserByID(int id) {
        logger.info("{} getUserByID for id: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            logger.error("{} Error in getUserByID for id: {}", LOG_PREFIX, id, e);
        }
        return null;
    }

    public User findUserByEmail(String email) {
        logger.info("{} findUserByEmail for: {}", LOG_PREFIX, email);
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (SQLException e) {
            logger.error("{} Error in findUserByEmail for email: {}", LOG_PREFIX, email, e);
        }
        return null;
    }

    public List<User> getAllUsers() {
        logger.info("{} getAllUsers", LOG_PREFIX);
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) list.add(mapUser(rs));
            logger.info("{} Fetched {} users", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Error in getAllUsers", LOG_PREFIX, e);
        }
        return list;
    }

    public boolean checkAvailableUserNameOrEmail(String userNameOrEmail) {
        String sql = "SELECT 1 FROM users WHERE userName = ? OR email = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, userNameOrEmail);
            stm.setString(2, userNameOrEmail);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            logger.error("{} Error in checkAvailableUserNameOrEmail for: {}", LOG_PREFIX, userNameOrEmail, e);
        }
        return false;
    }

    public boolean checkPhoneExists(String phoneNum) {
        String sql = "SELECT 1 FROM users WHERE phoneNum = ? LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, phoneNum);
            return stm.executeQuery().next();
        } catch (SQLException e) {
            logger.error("{} Error in checkPhoneExists for: {}", LOG_PREFIX, phoneNum, e);
        }
        return false;
    }

    public int countTotalUser() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'user'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            logger.error("{} Error in countTotalUser", LOG_PREFIX, e);
        }
        return 0;
    }

    public boolean addUser(User user) {
        logger.info("{} addUser for userName: {}", LOG_PREFIX, user.getUsername());
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) "
                + "VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray()));
            stm.setString(5, user.getRole());
            boolean result = stm.executeUpdate() > 0;
            logger.info("{} addUser status for '{}': {}", LOG_PREFIX, user.getUsername(), result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in addUser for: {}", LOG_PREFIX, user.getUsername(), e);
        }
        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=?, role=? WHERE id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, user.getRole());
            stm.setInt(5, user.getId());
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("{} Error in updateUser for id: {}", LOG_PREFIX, user.getId(), e);
        }
        return false;
    }

    public boolean updateUserInfo(User user) {
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNum());
            ps.setInt(4, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("{} Error in updateUserInfo for id: {}", LOG_PREFIX, user.getId(), e);
        }
        return false;
    }

    public boolean updateUsername(int userId, String newUsername) {
        String sql = "UPDATE users SET userName=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newUsername);
            ps.setInt(2, userId);
            boolean result = ps.executeUpdate() > 0;
            if (result) logger.info("{} Username updated for userId={}", LOG_PREFIX, userId);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updateUsername for userId={}: ", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean updatePhone(int userId, String newPhone) {
        String sql = "UPDATE users SET phoneNum=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPhone);
            ps.setInt(2, userId);
            boolean result = ps.executeUpdate() > 0;
            if (result) logger.info("{} Phone updated for userId={}", LOG_PREFIX, userId);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updatePhone for userId={}: ", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean updateEmail(int userId, String newEmail) {
        String sql = "UPDATE users SET email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, userId);
            boolean result = ps.executeUpdate() > 0;
            if (result) logger.info("{} Email updated for userId={}", LOG_PREFIX, userId);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updateEmail for userId={}: ", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean unlinkOAuth(int userId) {
        String sql = "UPDATE users SET provider=NULL, provider_id=NULL WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            boolean result = ps.executeUpdate() > 0;
            if (result) logger.info("{} OAuth unlinked for userId={}", LOG_PREFIX, userId);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in unlinkOAuth for userId={}: ", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("{} Error in deleteUser for id: {}", LOG_PREFIX, id, e);
        }
        return false;
    }

    public boolean verifyPassword(int userId, String oldPassword) {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (hashed == null) return false; // OAuth user chưa có password
                return BCrypt.verifyer().verify(oldPassword.toCharArray(), hashed).verified;
            }
        } catch (SQLException e) {
            logger.error("{} Error in verifyPassword for userId: {}", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String hashed = BCrypt.withDefaults().hashToString(10, newPassword.toCharArray());
            ps.setString(1, hashed);
            ps.setInt(2, userId);
            boolean result = ps.executeUpdate() > 0;
            if (result) logger.info("{} Password updated for userId: {}", LOG_PREFIX, userId);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updatePassword for userId: {}", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE userName = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            logger.error("{} Error in checkUsernameExists for: {}", LOG_PREFIX, username, e);
        }
        return false;
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            logger.error("{} Error in checkEmailExists for: {}", LOG_PREFIX, email, e);
        }
        return false;
    }
}