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
        String query = "INSERT INTO users (userName, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setUserId(rs.getLong(1)); // capture generated id
                }
                return user;
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
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
