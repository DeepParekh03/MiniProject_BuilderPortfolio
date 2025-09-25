package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Repository class for Admin-related database operations.
 * Provides methods to perform CRUD operations on the users table.
 */
@Slf4j
public class AdminRepository {

    /**
     * Deletes a user from the database based on the user ID.
     *
     * @param userId the ID of the user to delete
     * @return {@code true} if the user was successfully deleted; {@code false} otherwise
     */
    public boolean deleteUser(long userId) {
        String sql = "DELETE FROM users WHERE user_id=? ";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return false;
        }
    }
}
