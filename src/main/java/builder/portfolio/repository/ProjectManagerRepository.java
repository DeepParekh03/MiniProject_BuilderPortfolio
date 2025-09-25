package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class handling database operations for Project Managers.
 * Provides methods to retrieve and update project actual spend,
 * update project status, and send notifications to associated users.
 */
@Slf4j
public class ProjectManagerRepository {

    /**
     * Retrieves the current actual spend for a given project.
     *
     * @param projectId the ID of the project
     * @return the current actual spend, or 0 if an error occurs
     */
    public double getCurrentActualSpend(long projectId) {
        String sql = "SELECT actual_spend FROM project WHERE project_id = ?";
        double actualSpend = 0;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                actualSpend = rs.getDouble("actual_spend");
            }
            return actualSpend;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return 0;
        }
    }

    /**
     * Updates the actual spend for a project by adding the given amount to the current spend.
     *
     * @param projectId   the ID of the project
     * @param actualSpend the amount to add to the current actual spend
     * @return the updated actual spend, or 0 if an error occurs
     */
    public double updateProjectActualSpend(long projectId, double actualSpend) {
        double currentActualSpend = getCurrentActualSpend(projectId);
        currentActualSpend += actualSpend;

        String sql = "UPDATE project SET actual_spend = ? WHERE project_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, currentActualSpend);
            ps.setLong(2, projectId);
            ps.executeUpdate();

            return currentActualSpend;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return 0;
        }
    }

    /**
     * Updates the status of a project based on the number of tasks completed.
     * Also updates the status of the tasks and sends notifications to builder and client.
     *
     * @param projectId      the ID of the project
     * @param numberOfTasks  the number of tasks to mark as completed
     * @return the number of tasks updated, or 0 if an error occurs
     */
    public int updateProjectStatus(long projectId, int numberOfTasks) {
        String updateTasksSql = """
                UPDATE task t
                SET status = 'COMPLETED',
                    updated_at = NOW()
                WHERE t.task_id IN (
                    SELECT task_id
                    FROM task
                    WHERE project_id = ? AND status = 'PENDING'
                    ORDER BY task_id
                    LIMIT ?
                )
                """;

        String countPendingSql = "SELECT COUNT(*) AS pending_count FROM task WHERE project_id = ? AND status = 'PENDING'";
        String getProjectSql = "SELECT status, builder_id, client_id FROM project WHERE project_id = ?";
        String updateProjectStatusSql = "UPDATE project SET status = ? WHERE project_id = ?";
        String insertNotificationSql = "INSERT INTO notification (user_id, message) VALUES (?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement psUpdateTasks = connection.prepareStatement(updateTasksSql);
             PreparedStatement psCountPending = connection.prepareStatement(countPendingSql);
             PreparedStatement psGetProject = connection.prepareStatement(getProjectSql);
             PreparedStatement psUpdateProject = connection.prepareStatement(updateProjectStatusSql);
             PreparedStatement psInsertNotification = connection.prepareStatement(insertNotificationSql)) {

            connection.setAutoCommit(false);

            String currentStatus = null;
            long builderId = 0;
            long clientId = 0;

            // Get current project status and user IDs
            psGetProject.setLong(1, projectId);
            try (ResultSet rs = psGetProject.executeQuery()) {
                if (rs.next()) {
                    currentStatus = rs.getString("status");
                    builderId = rs.getLong("builder_id");
                    clientId = rs.getLong("client_id");
                }
            }

            // Update task statuses
            psUpdateTasks.setLong(1, projectId);
            psUpdateTasks.setInt(2, numberOfTasks);
            int updatedTasks = psUpdateTasks.executeUpdate();

            // Count remaining pending tasks
            psCountPending.setLong(1, projectId);
            ResultSet rs = psCountPending.executeQuery();
            int pendingCount = 0;
            if (rs.next()) {
                pendingCount = rs.getInt("pending_count");
            }

            // Update project status if needed
            String newStatus = (pendingCount > 0) ? "IN_PROGRESS" : "COMPLETED";
            if (!newStatus.equals(currentStatus)) {
                psUpdateProject.setString(1, newStatus);
                psUpdateProject.setLong(2, projectId);
                psUpdateProject.executeUpdate();

                String message = (newStatus.equals("COMPLETED"))
                        ? "Project (ID: " + projectId + ") has been completed!"
                        : "Project (ID: " + projectId + ") is now in progress.";

                // Insert notifications
                psInsertNotification.setLong(1, builderId);
                psInsertNotification.setString(2, message);
                psInsertNotification.executeUpdate();

                psInsertNotification.setLong(1, clientId);
                psInsertNotification.setString(2, message);
                psInsertNotification.executeUpdate();

                // Send notifications concurrently if project completed
                if (newStatus.equals("COMPLETED")) {
                    sendNotificationsConcurrently(builderId, clientId, message);
                } else {
                    System.out.println("NOTIFICATION[BUILDER]: " + message);
                    System.out.println("NOTIFICATION[CLIENT]: " + message);
                }
            }

            connection.commit();
            return updatedTasks;

        } catch (SQLException sqlException) {
            log.error(sqlException.getMessage());
            return 0;
        }
    }

    /**
     * Sends notifications to builder and client concurrently using a thread pool.
     *
     * @param builderId the builder's user ID
     * @param clientId  the client's user ID
     * @param message   the notification message
     */
    private void sendNotificationsConcurrently(long builderId, long clientId, String message) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            System.out.println("NOTIFICATION[CLIENT]: Sending notification to client...");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.println("NOTIFICATION[CLIENT]: " + message);
        });

        executor.submit(() -> {
            System.out.println("NOTIFICATION[BUILDER]: Sending notification to builder...");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.println("NOTIFICATION[BUILDER]: " + message);
        });

        executor.shutdown();
    }
}
