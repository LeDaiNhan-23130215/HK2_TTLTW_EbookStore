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

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public User login(String usernameOrEmail, String password) {
        logger.info("Executing login for usernameOrEmail: {}", usernameOrEmail);
        String sql = "SELECT * FROM users WHERE (userName = ? OR email = ?) LIMIT 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");

                BCrypt.Result result = BCrypt.verifyer()
                        .verify(password.toCharArray(), hashed);

                if (result.verified) {
                    logger.info("Login successful for usernameOrEmail: {}", usernameOrEmail);
                    return new User(
                            rs.getInt("id"),
                            rs.getString("userName"),
                            rs.getString("email"),
                            rs.getString("phoneNum"),
                            hashed,
                            rs.getString("role")
                    );
                }
                logger.warn("Login failed: password verification failed for usernameOrEmail: {}", usernameOrEmail);
            } else {
                logger.warn("Login failed: user not found for usernameOrEmail: {}", usernameOrEmail);
            }
        } catch (Exception e) {
            logger.error("Error in login for usernameOrEmail: {}", usernameOrEmail, e);
            e.printStackTrace();
        }
        return null;
    }

    public boolean signUp(String userName, String email, String phoneNum, String password) {
        logger.info("Executing signUp for userName: {}, email: {}", userName, email);
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) VALUES(?, ?, ?, ?, 'user')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            String hash = BCrypt.withDefaults().hashToString(10, password.toCharArray());

            stm.setString(1, userName);
            stm.setString(2, email);
            stm.setString(3, phoneNum);
            stm.setString(4, hash);

            boolean result = stm.executeUpdate() > 0;
            logger.info("SignUp status for userName '{}': {}", userName, result);
            return result;

        } catch (SQLException e) {
            logger.error("Error in signUp for userName: {}", userName, e);
            e.printStackTrace();
        }
        return false;
    }


    public boolean checkAvailableUserNameOrEmail(String userNameOrEmail) {
        logger.info("Executing checkAvailableUserNameOrEmail for: {}", userNameOrEmail);
        String sql = "select userName, email from users where userName = ? or email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, userNameOrEmail);
            stm.setString(2, userNameOrEmail);
            ResultSet rs = stm.executeQuery();
            boolean exists = rs.next();
            logger.info("Availability check for '{}': exists = {}", userNameOrEmail, exists);
            return exists;
        } catch (SQLException e) {
            logger.error("Error in checkAvailableUserNameOrEmail for: {}", userNameOrEmail, e);
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByID(int id) {
        logger.info("Executing getUserByID for id: {}", id);
        String sql = "select * from users where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                logger.info("Successfully fetched user for id: {}", id);
                return new User(rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("phoneNum"),
                        rs.getString("password"),
                        rs.getString("role"));
            }
            logger.info("No user found for id: {}", id);
        } catch (SQLException e) {
            logger.error("Error in getUserByID for id: {}", id, e);
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        logger.info("Executing getAllUsers");
        String sql = "SELECT * FROM users";
        List<User> list = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("phoneNum"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
            logger.info("Successfully fetched {} users", list.size());
        } catch (SQLException e) {
            logger.error("Error in getAllUsers", e);
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUser(User user) {
        logger.info("Executing addUser for userName: {}", user.getUsername());
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray()));
            stm.setString(5, user.getRole());

            boolean result = stm.executeUpdate() > 0;
            logger.info("Add user status for userName '{}': {}", user.getUsername(), result);
            return result;
        } catch (SQLException e) {
            logger.error("Error in addUser for userName: {}", user.getUsername(), e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        logger.info("Executing updateUser for id: {}", user.getId());
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=?, role=? WHERE id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, user.getRole());
            stm.setInt(5, user.getId());

            boolean result = stm.executeUpdate() > 0;
            logger.info("Update user status for id {}: {}", user.getId(), result);
            return result;

        } catch (SQLException e) {
            logger.error("Error in updateUser for id: {}", user.getId(), e);
            e.printStackTrace();
        }
        return false;
    }


    public boolean deleteUser(int id) {
        logger.info("Executing deleteUser for id: {}", id);
        String sql = "DELETE from users where id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            boolean result = stm.executeUpdate() > 0;
            logger.info("Delete user status for id {}: {}", id, result);
            return result;
        } catch (SQLException e) {
            logger.error("Error in deleteUser for id: {}", id, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAdminLogin(String usernameOrEmail, String password) {
        logger.info("Executing checkAdminLogin for usernameOrEmail: {}", usernameOrEmail);
        String sql = "select password from users where (userName=? OR email=?) and role = 'admin'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, usernameOrEmail);
            stm.setString(2, usernameOrEmail);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                logger.info("Admin verification status for '{}': {}", usernameOrEmail, result.verified);
                return result.verified;
            }
            logger.warn("Admin verification failed: user not found or not an admin for '{}'", usernameOrEmail);
        } catch (SQLException e) {
            logger.error("Error in checkAdminLogin for usernameOrEmail: {}", usernameOrEmail, e);
            e.printStackTrace();
        }
        return false;
    }

    public String getUserNameByEmail(String userAndEmail) {
        logger.info("Executing getUserNameByEmail for: {}", userAndEmail);
        String query = "SELECT userName FROM users WHERE (email = ? or userName = ?)";
        String username = "";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)){
            stm.setString(1, userAndEmail);
            stm.setString(2, userAndEmail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                username = rs.getString(1);
                logger.info("Successfully found username for input '{}'", userAndEmail);
            } else {
                username = "Error";
                logger.info("No user found for input '{}'", userAndEmail);
            }
        } catch (SQLException e) {
            logger.error("Error in getUserNameByEmail for: {}", userAndEmail, e);
            e.printStackTrace();
        }
        return username;
    }

    public int countTotalUser() {
        logger.info("Executing countTotalUser");
        String sql = "SELECT COUNT(*) FROM `users` WHERE role = 'user'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            int count = rs.next() ? rs.getInt(1) : 0;
            logger.info("Total user count: {}", count);
            return count;
        } catch (SQLException e) {
            logger.error("Error in countTotalUser", e);
            e.printStackTrace();
        }
        return 0;
    }

    public boolean verifyPassword(int userId, String oldPassword) {
        logger.info("Executing verifyPassword for userId: {}", userId);
        String sql = "SELECT password FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                BCrypt.Result result = BCrypt.verifyer()
                        .verify(oldPassword.toCharArray(), hashedPassword);

                logger.info("Password verification status for userId {}: {}", userId, result.verified);
                return result.verified;
            }
            logger.info("No password record found for verification of userId: {}", userId);

        } catch (SQLException e) {
            logger.error("Error in verifyPassword for userId: {}", userId, e);
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        logger.info("Executing updatePassword for userId: {}", userId);
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hashed = BCrypt.withDefaults()
                    .hashToString(10, newPassword.toCharArray());

            ps.setString(1, hashed);
            ps.setInt(2, userId);

            boolean result = ps.executeUpdate() > 0;
            logger.info("Update password status for userId {}: {}", userId, result);
            return result;

        } catch (SQLException e) {
            logger.error("Error in updatePassword for userId: {}", userId, e);
            e.printStackTrace();
        }
        return false;
    }

    public User findUserByEmail(String email) {
        logger.info("Executing findUserByEmail for email: {}", email);
        String sql = "SELECT * FROM users WHERE email = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                User user = new User(id);
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNum(rs.getString("phoneNum"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                logger.info("Successfully found user for email: {}", email);
                return user;
            }
            logger.info("No user found for email: {}", email);
        } catch (SQLException e){
            logger.error("Error in findUserByEmail for email: {}", email, e);
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserInfo(User user) {
        logger.info("Executing updateUserInfo for id: {}", user.getId());
        String sql = "UPDATE users SET userName=?, email=?, phoneNum=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNum());
            ps.setInt(4, user.getId());
            boolean result = ps.executeUpdate() > 0;
            logger.info("Update user info status for id {}: {}", user.getId(), result);
            return result;
        } catch (SQLException e) {
            logger.error("Error in updateUserInfo for id: {}", user.getId(), e);
            e.printStackTrace();
        }
        return false;
    }

}