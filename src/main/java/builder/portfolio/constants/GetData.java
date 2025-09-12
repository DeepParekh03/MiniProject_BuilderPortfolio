package builder.portfolio.constants;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetData {
    public static List<User> getAllData(String role) {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role=?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) { // use while to fetch all rows
                User user = new User();
                user.setUserId(rs.getLong("user_id"));        // match your column name
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUserName(rs.getString("username"));
                user.setRole(UserRole.valueOf(rs.getString("role"))); // convert string to enum
                userList.add(user);
            }
            return userList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
