package builder.portfolio.repository;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BuilderRepository {


    public Project saveProject(Project project) {
        String sql = "INSERT INTO project (project_name, status, planned_budget, actual_spend, builder_id, manager_id, client_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING project_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, String.valueOf(Status.UPCOMING));
            ps.setDouble(3, project.getPlannedBudget());
            ps.setDouble(4, project.getActualSpend());
            ps.setLong(5, project.getBuilderId());
            ps.setLong(6, project.getProjectManagerId());
            ps.setLong(7, project.getClientId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                project.setProjectId(rs.getLong("project_id"));
            }
            return project;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public Document uploadDocumentDB(Document document){
        String sql="INSERT INTO DOCUMENT (project_id,document_name, document_url, created_at, document_type, uploaded_by) VALUES (?, ?, ?, ?, ?, ?) RETURNING document_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, document.getProjectId());
            ps.setString(2, document.getDocumentName());
            ps.setString(3, document.getFilePath());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setString(5, document.getType());
            ps.setString(6,document.getUploadedBy().getUserName());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                document.setDocumentId(rs.getLong("document_id"));
            }
            return document;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }

    public Task saveTask(Task task){

        String sql="INSERT INTO TASK (project_id,task_name,status,created_at,updated_at) VALUES (?, ?, ?, ?, ?) RETURNING task_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, task.getProjectId());
            ps.setString(2, task.getTaskName());
            ps.setString(3,task.getStatus());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setDate(5, Date.valueOf(LocalDate.now()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                task.setTaskId(rs.getLong("task_id"));
            }
            return task;

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return null;
        }
    }


    public static List<Project> getAllProjectsBuilder(User user){
        List<Project> projectList=new ArrayList<>();

        String sql="SELECT * FROM project WHERE builder_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, user.getUserId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
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

                projectList.add(project);

            }
            return projectList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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
