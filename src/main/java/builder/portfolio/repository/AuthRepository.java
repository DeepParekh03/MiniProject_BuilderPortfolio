package builder.portfolio.repository;

import builder.portfolio.exceptions.InvalidUserFormatException;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * Repository class for authentication-related database operations.
 * Handles user login and registration by interacting with the 'users' table in the database.
 */
@Slf4j
public class AuthRepository {

    /**
     * Attempts to log in a user with the given email and password.
     * Queries the database for a user matching the provided credentials.
     * Returns a {@link User} object if successful, otherwise returns {@code null}.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the authenticated {@link User} or {@code null} if login fails
     */
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapUserFromResultSet(rs);
            }
        } catch (SQLException | InvalidUserFormatException exception) {
            log.error(exception.getMessage());
        }

        return null;
    }

    /**
     * Registers a new user in the database.
     * First checks if the email is already registered. If not, inserts the user
     * into the 'users' table and sets the generated user ID.
     *
     * @param user the {@link User} object containing registration details
     * @return the registered {@link User} with assigned ID, or {@code null} if registration fails
     */
    public User register(User user) {
        String checkQuery = "SELECT user_id FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (userName, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection()) {

            // Check if email already exists
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, user.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    log.info("Email already registered.");
                    return null;
                }
            }

            // Insert new user
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, user.getUserName());
                insertStmt.setString(2, user.getEmail());
                insertStmt.setString(3, user.getPassword());
                insertStmt.setString(4, user.getRole().name());

                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getLong(1));
                    }
                    return user;
                }
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * Maps a {@link ResultSet} row to a {@link User} object.
     * Extracts user ID, username, email, password, and role from the result set.
     * Throws {@link InvalidUserFormatException} if any required field is null.
     *
     * @param rs the {@link ResultSet} containing user data
     * @return a {@link User} object populated with data from the result set
     * @throws InvalidUserFormatException if required user fields are missing or invalid
     * @throws SQLException               if a database access error occurs
     */
    private User mapUserFromResultSet(ResultSet rs) throws InvalidUserFormatException, SQLException {
        long userId = rs.getLong("user_id");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String roleStr = rs.getString("role");

        if (username == null || email == null || password == null || roleStr == null) {
            throw new InvalidUserFormatException("Invalid User Format from Postgres");
        }

        User user = new User();
        user.setUserId(userId);
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(UserRole.valueOf(roleStr));

        return user;
    }
}
