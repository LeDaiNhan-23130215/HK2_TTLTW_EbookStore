package utils;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static final String LOG_PREFIX = "[CONNECTION_POOL]";

    private static ConnectionPool instance;

    private final String URL = DBConfig.getUrl();
    private final String USER = DBConfig.getUsername();
    private final String PASS = DBConfig.getPassword();
    private final String DRIVER = DBConfig.getDriver();

    private HikariDataSource ds;

    private ConnectionPool() {
        logger.info("{} Initializing HikariCP Database Connection Pool subsystem...", LOG_PREFIX);
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USER);
            config.setPassword(PASS);
            config.setDriverClassName(DRIVER);

            config.setMaximumPoolSize(5);
            config.setMinimumIdle(2);

            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(3000);

            ds = new HikariDataSource(config);
            logger.info("{} HikariCP Datasource instantiated successfully. [Max Size: 5, Min Idle: 2].", LOG_PREFIX);
        } catch (Exception e) {
            logger.error("{} CRITICAL BOOT ERROR: Failed to configure or instantiate HikariDataSource context provider: ", LOG_PREFIX, e);
            throw e;
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            logger.debug("{} Singleton instance is null. Building initial ConnectionPool structure.", LOG_PREFIX);
            instance = new ConnectionPool();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("{} Handing off active SQL Connection thread from the managed Hikari Pool registry.", LOG_PREFIX);
        }
        return ds.getConnection();
    }

    public void shutdown() throws SQLException {
        logger.warn("{} Triggering active lifecycle system teardown sequence: Disposing HikariCP data pool allocations.", LOG_PREFIX);
        if (ds != null && !ds.isClosed()) {
            ds.close();
            logger.info("{} Database Connection Pool safely flushed, closed, and unregistered.", LOG_PREFIX);
        }
    }
}