package DAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import models.User;
import utils.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final Logger logger    = LoggerFactory.getLogger(UserDAO.class);
    private static final String LOG_PREFIX = "[USER_DAO]";

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getInt("id"));
        user.setUsername(rs.getString("userName"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNum(rs.getString("phoneNum"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        // provider + provider_id có thể NULL nếu chưa migrate DB
        try {
            user.setProvider(rs.getString("provider"));
            user.setProviderId(rs.getString("provider_id"));
        } catch (SQLException ignored) {
        }
        return user;
    }


    public User login(String usernameOrEmail, String password) {
        logger.info("{} Executing login for usernameOrEmail: {}", LOG_PREFIX, usernameOrEmail);
        String sql = "SELECT * FROM users WHERE (userName = ? OR email = ?) LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                // Tài khoản OAuth không có password
                if (hashed == null) {
                    logger.warn("{} Login failed: OAuth account has no password for: {}",
                            LOG_PREFIX, usernameOrEmail);
                    return null;
                }
                BCrypt.Result result = BCrypt.verifyer()
                        .verify(password.toCharArray(), hashed);
                if (result.verified) {
                    logger.info("{} Login successful for: {}", LOG_PREFIX, usernameOrEmail);
                    return mapUser(rs);
                }
                logger.warn("{} Login failed: wrong password for: {}", LOG_PREFIX, usernameOrEmail);
            } else {
                logger.warn("{} Login failed: user not found for: {}", LOG_PREFIX, usernameOrEmail);
            }
        } catch (Exception e) {
            logger.error("{} Error in login for: {}", LOG_PREFIX, usernameOrEmail, e);
        }
        return null;
    }

    public boolean checkAdminLogin(String usernameOrEmail, String password) {
        logger.info("{} Executing checkAdminLogin for: {}", LOG_PREFIX, usernameOrEmail);
        String sql = "SELECT password FROM users WHERE (userName=? OR email=?) AND role='admin'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, usernameOrEmail);
            stm.setString(2, usernameOrEmail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer()
                        .verify(password.toCharArray(), hashed);
                logger.info("{} Admin verification status for '{}': {}",
                        LOG_PREFIX, usernameOrEmail, result.verified);
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
        logger.info("{} Executing signUp for userName: {}, email: {}",
                LOG_PREFIX, userName, email);
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
            logger.info("{} SignUp status for '{}': {}", LOG_PREFIX, userName, result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in signUp for userName: {}", LOG_PREFIX, userName, e);
        }
        return false;
    }

    public User findByProvider(String provider, String providerId) {
        logger.info("{} Executing findByProvider: provider={}, providerId={}",
                LOG_PREFIX, provider, providerId);
        String sql = "SELECT * FROM users WHERE provider = ? AND provider_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, provider);
            ps.setString(2, providerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("{} Found user by provider: {}", LOG_PREFIX, provider);
                return mapUser(rs);
            }
            logger.info("{} No user found for provider={}, providerId={}",
                    LOG_PREFIX, provider, providerId);
        } catch (Exception e) {
            logger.error("{} Error in findByProvider", LOG_PREFIX, e);
        }
        return null;
    }

    public boolean createOAuthUser(String username, String email,
                                   String provider, String providerId) {
        logger.info("{} Executing createOAuthUser: username={}, email={}, provider={}",
                LOG_PREFIX, username, email, provider);
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role, "
                + "provider, provider_id) VALUES(?, ?, NULL, NULL, 'user', ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, provider);
            ps.setString(4, providerId);
            boolean result = ps.executeUpdate() > 0;
            logger.info("{} createOAuthUser status for '{}': {}", LOG_PREFIX, username, result);
            return result;
        } catch (Exception e) {
            logger.error("{} Error in createOAuthUser for: {}", LOG_PREFIX, username, e);
        }
        return false;
    }

    public boolean linkOAuthAccount(int userId, String provider, String providerId) {
        logger.info("{} Executing linkOAuthAccount: userId={}, provider={}",
                LOG_PREFIX, userId, provider);
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
        logger.info("{} Executing getUserByID for id: {}", LOG_PREFIX, id);
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                logger.info("{} Successfully fetched user for id: {}", LOG_PREFIX, id);
                return mapUser(rs);
            }
            logger.info("{} No user found for id: {}", LOG_PREFIX, id);
        } catch (SQLException e) {
            logger.error("{} Error in getUserByID for id: {}", LOG_PREFIX, id, e);
        }
        return null;
    }

    public User findUserByEmail(String email) {
        logger.info("{} Executing findUserByEmail for email: {}", LOG_PREFIX, email);
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logger.info("{} Successfully found user for email: {}", LOG_PREFIX, email);
                return mapUser(rs);
            }
            logger.info("{} No user found for email: {}", LOG_PREFIX, email);
        } catch (SQLException e) {
            logger.error("{} Error in findUserByEmail for email: {}", LOG_PREFIX, email, e);
        }
        return null;
    }

    public List<User> getAllUsers() {
        logger.info("{} Executing getAllUsers", LOG_PREFIX);
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            while (rs.next()) list.add(mapUser(rs));
            logger.info("{} Successfully fetched {} users", LOG_PREFIX, list.size());
        } catch (SQLException e) {
            logger.error("{} Error in getAllUsers", LOG_PREFIX, e);
        }
        return list;
    }

    public String getUserNameByEmail(String userAndEmail) {
        logger.info("{} Executing getUserNameByEmail for: {}", LOG_PREFIX, userAndEmail);
        String query = "SELECT userName FROM users WHERE (email = ? OR userName = ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, userAndEmail);
            stm.setString(2, userAndEmail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                logger.info("{} Found username for input '{}'", LOG_PREFIX, userAndEmail);
                return rs.getString(1);
            }
            logger.info("{} No user found for input '{}'", LOG_PREFIX, userAndEmail);
        } catch (SQLException e) {
            logger.error("{} Error in getUserNameByEmail for: {}", LOG_PREFIX, userAndEmail, e);
        }
        return "Error";
    }


    public boolean checkAvailableUserNameOrEmail(String userNameOrEmail) {
        logger.info("{} Executing checkAvailableUserNameOrEmail for: {}",
                LOG_PREFIX, userNameOrEmail);
        String sql = "SELECT userName, email FROM users WHERE userName = ? OR email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, userNameOrEmail);
            stm.setString(2, userNameOrEmail);
            ResultSet rs = stm.executeQuery();
            boolean exists = rs.next();
            logger.info("{} Availability check for '{}': exists = {}",
                    LOG_PREFIX, userNameOrEmail, exists);
            return exists;
        } catch (SQLException e) {
            logger.error("{} Error in checkAvailableUserNameOrEmail for: {}",
                    LOG_PREFIX, userNameOrEmail, e);
        }
        return false;
    }

    public int countTotalUser() {
        logger.info("{} Executing countTotalUser", LOG_PREFIX);
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'user'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            logger.info("{} Total user count: {}", LOG_PREFIX, count);
            return count;
        } catch (SQLException e) {
            logger.error("{} Error in countTotalUser", LOG_PREFIX, e);
        }
        return 0;
    }


    public boolean addUser(User user) {
        logger.info("{} Executing addUser for userName: {}", LOG_PREFIX, user.getUsername());
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) "
                + "VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, BCrypt.withDefaults()
                    .hashToString(10, user.getPassword().toCharArray()));
            stm.setString(5, user.getRole());
            boolean result = stm.executeUpdate() > 0;
            logger.info("{} Add user status for '{}': {}", LOG_PREFIX, user.getUsername(), result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in addUser for: {}", LOG_PREFIX, user.getUsername(), e);
        }
        return false;
    }

    public boolean updateUser(User user) {
        logger.info("{} Executing updateUser for id: {}", LOG_PREFIX, user.getId());
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=?, role=? WHERE id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, user.getRole());
            stm.setInt(5, user.getId());
            boolean result = stm.executeUpdate() > 0;
            logger.info("{} Update user status for id {}: {}", LOG_PREFIX, user.getId(), result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updateUser for id: {}", LOG_PREFIX, user.getId(), e);
        }
        return false;
    }

    public boolean updateUserInfo(User user) {
        logger.info("{} Executing updateUserInfo for id: {}", LOG_PREFIX, user.getId());
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNum());
            ps.setInt(4, user.getId());
            boolean result = ps.executeUpdate() > 0;
            logger.info("{} Update user info status for id {}: {}",
                    LOG_PREFIX, user.getId(), result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in updateUserInfo for id: {}", LOG_PREFIX, user.getId(), e);
        }
        return false;
    }

    public boolean deleteUser(int id) {
        logger.info("{} Executing deleteUser for id: {}", LOG_PREFIX, id);
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            boolean result = stm.executeUpdate() > 0;
            logger.info("{} Delete user status for id {}: {}", LOG_PREFIX, id, result);
            return result;
        } catch (SQLException e) {
            logger.error("{} Error in deleteUser for id: {}", LOG_PREFIX, id, e);
        }
        return false;
    }

    public boolean verifyPassword(int userId, String oldPassword) {
        logger.info("{} Executing verifyPassword for userId: {}", LOG_PREFIX, userId);
        String sql = "SELECT password FROM users WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                if (hashed == null) return false; // OAuth user không có password
                BCrypt.Result result = BCrypt.verifyer()
                        .verify(oldPassword.toCharArray(), hashed);
                logger.info("{} Password verification for userId {}: {}",
                        LOG_PREFIX, userId, result.verified);
                return result.verified;
            }
        } catch (SQLException e) {
            logger.error("{} Error in verifyPassword for userId: {}", LOG_PREFIX, userId, e);
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        logger.info("{} Executing updatePassword for userId: {}", LOG_PREFIX, userId);
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String hashed = BCrypt.withDefaults()
                    .hashToString(10, newPassword.toCharArray());
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
}