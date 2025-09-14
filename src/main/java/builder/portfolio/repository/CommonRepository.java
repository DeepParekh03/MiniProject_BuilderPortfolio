package builder.portfolio.repository;

import builder.portfolio.controller.DashboardController;
import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.util.DBUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;

import javax.print.Doc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonRepository {

    public static List<Project> getAllProjects(User user) {
        List<Project> projects = new ArrayList<>();
        String sql;

        switch (user.getRole()) {
            case ADMIN:
                sql = "SELECT * FROM project";
                break;
            case BUILDER:
                sql = "SELECT * FROM project WHERE builder_id = ?";
                break;
            case PROJECT_MANAGER:
                sql = "SELECT * FROM project WHERE manager_id = ?";
                break;
            case CLIENT:
                sql = "SELECT * FROM project WHERE client_id = ?";
                break;
            default:
                throw new IllegalArgumentException("Unsupported role: " + user.getRole());
        }

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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

    public static List<Task> getAllTasks(long projectId){
        List<Task> taskList=new ArrayList<>();
        String sql="SELECT * FROM task where project_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                taskList.add(mapTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public static List<Document> getAllDocuments(long projectId){
        List<Document> documentList=new ArrayList<>();
        String sql="SELECT * FROM document where project_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                documentList.add(mapDocumentFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentList;
    }

    public static List<User> getAllUsers(UserRole role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

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

    public String trackBudget(long projectId){
        String budgetStatus="";
        String sql="SELECT actual_spend, planned_budget FROM project WHERE project_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            double actual_spend=0;
            double planned_budget=0;
            ps.setLong(1, projectId);


            ResultSet rs= ps.executeQuery();
            if(rs.next()){
                actual_spend=rs.getDouble("actual_spend");
                planned_budget=rs.getDouble("planned_subject");
            }
            if(actual_spend<planned_budget){
                budgetStatus= "IN BUDGET";
            }
            else{
                System.out.println("Out");
                budgetStatus= "OUT OF BUDGET";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return budgetStatus;
    }


    public long availableClients(){
        System.out.println("Available Clients: ");
        List<User> clientList= CommonRepository.getAllUsers(UserRole.CLIENT);
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long clientId = ValidatorUtil.validateId(
                "Select client ID: ",
                clientList,
                User::getUserId
        );
        return clientId;
    }

    public long availableProjectManagers(){
        System.out.println("Available Managers: ");
        List<User> managerList= CommonRepository.getAllUsers(UserRole.PROJECT_MANAGER);
        managerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long managerId = ValidatorUtil.validateId(
                "Select manager ID: ",
                managerList,
                User::getUserId
        );
        return  managerId;
    }

    public long availableProjects(){
        System.out.println("Here");
        System.out.println("Available Projects: ");
        List<Project> projectList= CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        projectList.forEach(project ->
                System.out.println("Project ID: " + project.getProjectId()
                        + ", Project Name: " + project.getProjectName()
                        + ", Current Project Manager ID: " + project.getProjectManagerId()
                        +", Project Actual Spend: "+project.getActualSpend()
                        +", Project Planned Budget: "+project.getPlannedBudget())
        );
        long projectId=0;
        if(projectList.isEmpty()){
            System.out.println("No projects to display for this user");
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
        else {
            projectId=ValidatorUtil.validateId(
                    "Select Project ID: ",
                    projectList,
                    Project::getProjectId
            );
        }
        return projectId;
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

    private static Document mapDocumentFromResultSet(ResultSet rs) throws SQLException{
        Document document=new Document();
        document.setDocumentId(rs.getLong("document_id"));
        document.setType(rs.getString("document_type"));
        document.setDocumentName(rs.getString("document_name"));
        User uploader = new User();
        uploader.setUserId(rs.getLong("uploaded_by"));
        document.setUploadedBy(uploader);
        return document;
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

    private static Task mapTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getLong("task_id"));
        task.setTaskName(rs.getString("task_name"));
        task.setStatus(rs.getString("status"));
        task.setCreatedAt(rs.getDate("created_at"));
        task.setUpdatedAt(rs.getDate("updated_at"));
        return task;
    }
}
