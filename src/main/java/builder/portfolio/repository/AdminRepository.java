package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminRepository {
    public boolean deleteProjectManager(long userId){
        String sql="DELETE FROM users WHERE user_id=?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);
            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
