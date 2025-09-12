package builder.portfolio.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    public static Connection getConnection() {
        Connection connection=null;
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

