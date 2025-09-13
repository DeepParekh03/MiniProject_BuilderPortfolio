package builder.portfolio.repository;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonRepository {

    public static List<Project> getAllProjects(User user) {
        List<Project> projects = new ArrayList<>();
        String query;

        switch (user.getRole()) {
            case ADMIN:
                query = "SELECT * FROM project";
                break;
            case BUILDER:
                query = "SELECT * FROM project WHERE builder_id = ?";
                break;
            case PROJECT_MANAGER:
                query = "SELECT * FROM project WHERE manager_id = ?";
                break;
            case CLIENT:
                query = "SELECT * FROM project WHERE client_id = ?";
                break;
            default:
                throw new IllegalArgumentException("Unsupported role: " + user.getRole());
        }

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            if (!user.getRole().equals(UserRole.ADMIN)) {
                ps.setLong(1, user.getUserId());
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                projects.add(mapProjectFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }


    public static List<User> getAllUsers(UserRole role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, role.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(mapUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private static Project mapProjectFromResultSet(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getLong("project_id"));
        project.setProjectName(rs.getString("project_name"));
        project.setStatus(Status.valueOf(rs.getString("status")));
        project.setPlannedBudget(rs.getDouble("planned_budget"));
        project.setActualSpend(rs.getDouble("actual_spend"));
        project.setBuilderId(rs.getLong("builder_id"));
        project.setProjectManagerId(rs.getLong("manager_id"));
        project.setClientId(rs.getLong("client_id"));
        project.setDocument(null);
        project.setTimeline(null);
        return project;
    }

    private static User mapUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUserName(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        return user;
    }
}
