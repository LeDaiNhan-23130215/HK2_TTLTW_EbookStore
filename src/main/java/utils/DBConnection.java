package utils;

import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnection {
    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);
    private static final String LOG_PREFIX = "[DB_CONNECTION]";

    public static Connection getConnection() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("{} Attempting to borrow a database connection from the ConnectionPool instance.", LOG_PREFIX);
            }

            Connection connection = ConnectionPool.getInstance().getConnection();

            if (logger.isDebugEnabled()) {
                logger.debug("{} Successfully borrowed connection instance: {} from pool.", LOG_PREFIX, connection.hashCode());
            }
            
            return connection;
        } catch (SQLException e) {
            logger.error("{} CRITICAL SYSTEM ERROR: Unable to acquire active database connection from allocation pool. Exception trace: ", LOG_PREFIX, e);
            throw new RuntimeException("Cannot get database connection from pool", e);
        }
    }
}