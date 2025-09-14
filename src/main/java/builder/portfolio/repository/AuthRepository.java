package builder.portfolio.repository;

import builder.portfolio.exceptions.InvalidUserFormatException;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
@Slf4j
public class AuthRepository {

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

    public User register(User user) {
        String checkQuery = "SELECT user_id FROM users WHERE email = ?";
        String insertQuery = "INSERT INTO users (userName, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setString(1, user.getEmail());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    log.info("Email already registered.");
                    return null;
                }
            }

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
