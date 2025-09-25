package builder.portfolio.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 *
 * This class provides a single static method {@link #getConnection()} to establish
 * a connection to the database using properties defined in the
 * {@code src/main/resources/application.properties} file.
 *
 * The following properties are expected in the properties file:
 * db_class_name - Fully qualified name of the JDBC driver class
 * db_database_url - URL of the database server (without database name)
 * db_username - Database username
 * db_password - Database password
 * db_database_name - Name of the database
 *
 * Example usage:
 * <pre>
 * {@code
 * Connection connection = DBUtil.getConnection();
 * if (connection != null) {
 *     // use the connection
 * }
 * }
 * </pre>
 *
 * Note: The method prints any exceptions to standard error and returns {@code null}
 * if a connection cannot be established.
 */
public class DBUtil {

    /**
     * Establishes and returns a {@link Connection} to the database.
     *
     * This method reads the database connection details from the
     * {@code application.properties} file, loads the JDBC driver, and
     * creates a connection to the specified database.
     *
     * @return a {@link Connection} object if successful; {@code null} otherwise
     */
    public static Connection getConnection() {
        Connection connection = null;
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(inputStream);
        } catch (IOException ioException) {
            System.err.println("Exception: " + ioException.getMessage());
            return null;
        }

        String dbClassName = properties.getProperty("db_class_name");
        String dbDatabaseUrl = properties.getProperty("db_database_url");
        String dbUsername = properties.getProperty("db_username");
        String dbPassword = properties.getProperty("db_password");
        String dbDatabaseName = properties.getProperty("db_database_name");

        try {
            Class.forName(dbClassName);
            connection = DriverManager.getConnection(
                    dbDatabaseUrl + "/" + dbDatabaseName,
                    dbUsername, dbPassword);
        } catch (SQLException | ClassNotFoundException exception) {
            System.err.println(exception.getMessage());
        } finally {
            return connection;
        }
    }
}
