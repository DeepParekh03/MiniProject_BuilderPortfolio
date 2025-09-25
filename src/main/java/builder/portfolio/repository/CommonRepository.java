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
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class containing common database operations used across different roles.
 * Provides methods for fetching projects, tasks, documents, users, budget tracking,
 * and for selecting available clients, managers, and projects.
 */
@Slf4j
public class CommonRepository {

    /**
     * Retrieves all projects for a given user based on their role.
     *
     * @param user the {@link User} requesting the projects
     * @return a {@link List} of {@link Project} associated with the user
     */
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

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }

        return projects;
    }

    /**
     * Retrieves all tasks associated with a specific project.
     *
     * @param projectId the ID of the project
     * @return a {@link List} of {@link Task} objects
     */
    public static List<Task> getAllTasks(long projectId) {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE project_id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                taskList.add(mapTaskFromResultSet(rs));
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return taskList;
    }

    /**
     * Retrieves all documents associated with a specific project.
     *
     * @param projectId the ID of the project
     * @return a {@link List} of {@link Document} objects
     */
    public static List<Document> getAllDocuments(long projectId) {
        List<Document> documentList = new ArrayList<>();
        String sql = "SELECT * FROM document WHERE project_id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                documentList.add(mapDocumentFromResultSet(rs));
            }

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return documentList;
    }

    /**
     * Retrieves all users with a specific role.
     *
     * @param role the {@link UserRole} to filter users
     * @return a {@link List} of {@link User} objects
     */
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

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return users;
    }

    /**
     * Tracks the budget status of a project.
     *
     * @param projectId the ID of the project
     * @return {@code "IN BUDGET"} if actual spend is below planned budget,
     *         {@code "OUT OF BUDGET"} otherwise
     */
    public String trackBudget(long projectId) {
        String budgetStatus = "";
        String sql = "SELECT actual_spend, planned_budget FROM project WHERE project_id=?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            double actualSpend = 0;
            double plannedBudget = 0;
            ps.setLong(1, projectId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                actualSpend = rs.getDouble("actual_spend");
                plannedBudget = rs.getDouble("planned_budget");
            }

            budgetStatus = (actualSpend < plannedBudget) ? "IN BUDGET" : "OUT OF BUDGET";

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return budgetStatus;
    }

    /**
     * Displays available clients and prompts the user to select one.
     *
     * @return the selected client's ID
     */
    public long availableClients() {
        System.out.println("Available Clients: ");
        List<User> clientList = CommonRepository.getAllUsers(UserRole.CLIENT);
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );

        return ValidatorUtil.validateId("Select client ID: ", clientList, User::getUserId);
    }

    /**
     * Displays available project managers and prompts the user to select one.
     *
     * @return the selected project manager's ID
     */
    public long availableProjectManagers() {
        System.out.println("Available Managers: ");
        List<User> managerList = CommonRepository.getAllUsers(UserRole.PROJECT_MANAGER);
        managerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );

        return ValidatorUtil.validateId("Select manager ID: ", managerList, User::getUserId);
    }

    /**
     * Displays available projects for the current user and prompts the user to select one.
     *
     * @return the selected project's ID, or 0 if no projects are available
     */
    public long availableProjects() {
        System.out.println("Available Projects: ");
        List<Project> projectList = CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        projectList.forEach(project ->
                System.out.println("Project ID: " + project.getProjectId()
                        + ", Project Name: " + project.getProjectName()
                        + ", Current Project Manager ID: " + project.getProjectManagerId()
                        + ", Project Actual Spend: " + project.getActualSpend()
                        + ", Project Planned Budget: " + project.getPlannedBudget())
        );

        if (projectList.isEmpty()) {
            System.out.println("No projects to display for this user");
            DashboardController.showDashboard(SessionManager.getCurrentUser());
            return 0;
        }

        return ValidatorUtil.validateId("Select Project ID: ", projectList, Project::getProjectId);
    }

    // ------------------- Private Mapper Methods -------------------

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

    private static Document mapDocumentFromResultSet(ResultSet rs) throws SQLException {
        Document document = new Document();
        document.setDocumentId(rs.getLong("document_id"));
        document.setType(rs.getString("document_type"));
        document.setDocumentName(rs.getString("document_name"));
        document.setUploadedBy(rs.getString("uploaded_by"));
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
