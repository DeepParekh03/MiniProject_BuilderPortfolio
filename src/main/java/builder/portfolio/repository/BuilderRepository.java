package builder.portfolio.repository;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.ProjectTimeline;
import builder.portfolio.model.Task;
import builder.portfolio.model.enums.Status;
import builder.portfolio.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;

/**
 * Repository class for Builder-related database operations.
 * Handles CRUD operations for projects, tasks, documents, and notifications.
 */
@Slf4j
public class BuilderRepository {

    /**
     * Creates a new project in the database and sends notifications
     * to the assigned client and project manager.
     *
     * @param project the {@link Project} object containing project details
     * @return the created {@link Project} with assigned project ID, or {@code null} if creation fails
     */
    public Project createProjectRepository(Project project) {
        String sql = """
            INSERT INTO project (project_name, status, planned_budget, actual_spend, builder_id, manager_id, client_id, end_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING project_id
            """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setString(2, String.valueOf(Status.UPCOMING));
            ps.setDouble(3, project.getPlannedBudget());
            ps.setDouble(4, project.getActualSpend());
            ps.setLong(5, project.getBuilderId());
            ps.setLong(6, project.getProjectManagerId());
            ps.setLong(7, project.getClientId());
            ps.setDate(8, Date.valueOf(project.getEndDate()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                project.setProjectId(rs.getLong("project_id"));
            }

            String message = "A new project '" + project.getProjectName() + "' has been created and you have been added.";
            sendNotification(connection, project.getClientId(), message, "CLIENT");
            sendNotification(connection, project.getProjectManagerId(), message, "PROJECT_MANAGER");

            System.out.println("Notifications sent.");
            return project;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return null;
        }
    }

    /**
     * Updates an existing project's name and budget, and sends notifications
     * to the assigned client and project manager.
     *
     * @param project the {@link Project} object containing updated details
     * @return the updated {@link Project}, or {@code null} if update fails
     */
    public Project updateProjectRepository(Project project) {
        String sql = """
            UPDATE project SET project_name = ?, planned_budget = ?
            WHERE project_id = ? RETURNING project_id
            """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setDouble(2, project.getPlannedBudget());
            ps.setLong(3, project.getProjectId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                project.setProjectId(rs.getLong("project_id"));
            }

            String message = "Project '" + project.getProjectName() + "' has been updated.";
            sendNotification(connection, project.getClientId(), message, "CLIENT");
            sendNotification(connection, project.getProjectManagerId(), message, "PROJECT_MANAGER");

            System.out.println("Notifications sent.");
            return project;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return null;
        }
    }

    /**
     * Deletes a project along with its associated documents and tasks.
     * Sends notifications to the assigned client and project manager.
     *
     * @param project the {@link Project} to delete
     * @return {@code true} if the project was successfully deleted; {@code false} otherwise
     */
    public boolean deleteProjectRepository(Project project) {
        try (Connection connection = DBUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM document WHERE project_id = ?");
                 PreparedStatement ps2 = connection.prepareStatement("DELETE FROM task WHERE project_id = ?");
                 PreparedStatement ps3 = connection.prepareStatement("DELETE FROM project WHERE project_id = ?")) {

                ps1.setLong(1, project.getProjectId());
                ps1.executeUpdate();

                ps2.setLong(1, project.getProjectId());
                ps2.executeUpdate();

                ps3.setLong(1, project.getProjectId());
                int rows = ps3.executeUpdate();

                connection.commit();

                if (rows > 0) {
                    String message = "🗑️ Project '" + project.getProjectName() + "' has been deleted.";
                    sendNotification(connection, project.getClientId(), message, "CLIENT");
                    sendNotification(connection, project.getProjectManagerId(), message, "PROJECT_MANAGER");

                    System.out.println("Notifications sent.");
                    return true;
                }

                return false;
            } catch (SQLException sqlException) {
                connection.rollback();
                throw sqlException;
            }
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }
        return false;
    }

    /**
     * Updates the project manager for a project and notifies them.
     *
     * @param project the {@link Project} object containing the updated manager ID
     * @return {@code true} if the update succeeded; {@code false} otherwise
     */
    public boolean updateProjectManagerRepository(Project project) {
        String sql = "UPDATE project SET manager_id = ? WHERE project_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, project.getProjectManagerId());
            ps.setLong(2, project.getProjectId());

            int updatedRows = ps.executeUpdate();

            if (updatedRows > 0) {
                String message = "Project manager has been assigned/updated for project ID " + project.getProjectId();
                sendNotification(connection, project.getProjectManagerId(), message, "PROJECT_MANAGER");

                System.out.println("Notifications sent.");
                return true;
            }

            return false;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return false;
        }
    }

    /**
     * Uploads a document associated with a project to the database.
     *
     * @param document the {@link Document} to upload
     * @return the saved {@link Document} with assigned document ID, or {@code null} if upload fails
     */
    public Document uploadDocumentDB(Document document) {
        String sql = "INSERT INTO DOCUMENT (project_id,document_name, document_url, created_at, document_type, uploaded_by) VALUES (?, ?, ?, ?, ?, ?) RETURNING document_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, document.getProjectId());
            ps.setString(2, document.getDocumentName());
            ps.setString(3, document.getFilePath());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setString(5, document.getType());
            ps.setString(6, document.getUploadedBy());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                document.setDocumentId(rs.getLong("document_id"));
            }
            return document;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return null;
        }
    }

    /**
     * Saves a task associated with a project to the database.
     *
     * @param task the {@link Task} to save
     * @return the saved {@link Task} with assigned task ID, or {@code null} if save fails
     */
    public Task saveTask(Task task) {
        String sql = "INSERT INTO TASK (project_id,task_name,status,created_at,updated_at) VALUES (?, ?, ?, ?, ?) RETURNING task_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, task.getProjectId());
            ps.setString(2, task.getTaskName());
            ps.setString(3, task.getStatus());
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setDate(5, Date.valueOf(LocalDate.now()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                task.setTaskId(rs.getLong("task_id"));
            }
            return task;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return null;
        }
    }

    /**
     * Sends a notification message to a user.
     *
     * @param connection the database connection
     * @param userId     the recipient user's ID
     * @param message    the notification message
     * @param roleLabel  a label for logging purposes (e.g., CLIENT, PROJECT_MANAGER)
     */
    private void sendNotification(Connection connection, long userId, String message, String roleLabel) {
        if (userId <= 0) return;

        String insertNotificationSql = "INSERT INTO notification (user_id, message) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertNotificationSql)) {
            ps.setLong(1, userId);
            ps.setString(2, message);
            ps.executeUpdate();
        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
        }

        System.out.println("NOTIFICATION[" + roleLabel + "]: Sending...");
        try {
            Thread.sleep(400);
        } catch (InterruptedException ignored) {}
        System.out.println("NOTIFICATION[" + roleLabel + "]: " + message);
    }

    /**
     * Retrieves the timeline of a project, including completed tasks, total tasks, and end date.
     *
     * @param projectId the ID of the project
     * @return a {@link ProjectTimeline} containing project progress, or {@code null} if retrieval fails
     */
    public ProjectTimeline getProjectTimeline(long projectId) {
        String taskCountSql = """
            SELECT 
                COUNT(*) FILTER (WHERE status = 'COMPLETED') AS completed_tasks,
                COUNT(*) AS total_tasks
            FROM task
            WHERE project_id = ?
        """;

        String projectDateSql = "SELECT project_name, end_date FROM project WHERE project_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement psTask = connection.prepareStatement(taskCountSql);
             PreparedStatement psProject = connection.prepareStatement(projectDateSql)) {

            psTask.setLong(1, projectId);
            ResultSet rsTask = psTask.executeQuery();

            int completed = 0;
            int total = 0;
            if (rsTask.next()) {
                completed = rsTask.getInt("completed_tasks");
                total = rsTask.getInt("total_tasks");
            }

            psProject.setLong(1, projectId);
            ResultSet rsProj = psProject.executeQuery();

            String projectName = "";
            LocalDate endDate = null;
            if (rsProj.next()) {
                projectName = rsProj.getString("project_name");
                endDate = rsProj.getDate("end_date").toLocalDate();
            }

            return new ProjectTimeline(projectId, projectName, completed, total, endDate);

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return null;
        }
    }
}
