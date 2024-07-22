import java.sql.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private String databaseName;
    private volatile JdbcConnectionSource connection;

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    public JdbcConnectionSource getConnection() throws SQLException {
        if (databaseName == null) {
            throw new SQLException("Database name is null");
        }
        if (connection == null) {
            synchronized (this) {
                if (connection == null) {
                    try {
                        connection = new JdbcConnectionSource("jdbc:sqlite:" + databaseName);
                        logger.info("Opened database successfully");
                    } catch (Exception e) {
                        logger.error("Error opening database", e);
                        throw new SQLException("Error opening database", e);
                    }
                }
            }
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            synchronized (this) {
                if (connection != null) {
                    try {
                        connection.close();
                        connection = null;
                        logger.info("Closed database connection successfully");
                    } catch (Exception e) {
                        logger.error("Error closing database connection", e);
                    }
                }
            }
        }
    }
}
