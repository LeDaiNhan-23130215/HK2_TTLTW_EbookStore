package services;

import DAO.UserDAO;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    public boolean checkPassword(int userId, String oldPassword) {
        return userDAO.verifyPassword(userId, oldPassword);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userDAO.updatePassword(userId, newPassword);
    }
}
