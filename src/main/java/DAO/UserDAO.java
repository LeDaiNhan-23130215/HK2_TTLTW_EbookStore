package DAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import models.User;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User login(String usernameOrEmail, String password) {
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
                    return new User(
                            rs.getInt("id"),
                            rs.getString("userName"),
                            rs.getString("email"),
                            rs.getString("phoneNum"),
                            hashed,
                            rs.getString("role")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean signUp(String userName, String email, String phoneNum, String password) {
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) VALUES(?, ?, ?, ?, 'user')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            String hash = BCrypt.withDefaults().hashToString(10, password.toCharArray());

            stm.setString(1, userName);
            stm.setString(2, email);
            stm.setString(3, phoneNum);
            stm.setString(4, hash);

            return stm.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean checkAvailableUserNameOrEmail(String userNameOrEmail) {
        String sql = "select userName, email from users where userName = ? or email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, userNameOrEmail);
            stm.setString(2, userNameOrEmail);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByID(int id) {
        String sql = "select * from users where id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("phoneNum"),
                        rs.getString("password"),
                        rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users(userName, email, phoneNum, password, role) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPhoneNum());
            stm.setString(4, BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray()));
            stm.setString(5, user.getRole());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }


    public boolean deleteUser(int id) {
        String sql = "DELETE from users where id=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, id);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkAdminLogin(String usernameOrEmail, String password) {
        String sql = "select password from users where (userName=? OR email=?) and role = 'admin'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, usernameOrEmail);
            stm.setString(2, usernameOrEmail);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
                return result.verified;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUserNameByEmail(String userAndEmail) {
        String query = "SELECT userName FROM users WHERE (email = ? or userName = ?)";
        String username = "";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)){
            stm.setString(1, userAndEmail);
            stm.setString(2, userAndEmail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                username = rs.getString(1);
            } else {username = "Error";}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    public int countTotalUser() {
        String sql = "SELECT COUNT(*) FROM `users` WHERE role = 'user'";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public boolean verifyPassword(int userId, String oldPassword) {
        String sql = "SELECT password FROM users WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                BCrypt.Result result = BCrypt.verifyer()
                        .verify(oldPassword.toCharArray(), hashedPassword);

                return result.verified;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String hashed = BCrypt.withDefaults()
                    .hashToString(10, newPassword.toCharArray());

            ps.setString(1, hashed);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findUserByEmail(String email) {
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
                return user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
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
            e.printStackTrace();
        }
        return false;
    }

}
