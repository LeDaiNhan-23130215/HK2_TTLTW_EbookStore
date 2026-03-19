package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() {
        try{
            Connection connection = ConnectionPool.getInstance().getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Cannot get database connection from pool", e);
        }
    }
}
