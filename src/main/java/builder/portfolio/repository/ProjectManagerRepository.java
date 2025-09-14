package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectManagerRepository {

    public double getCurrentActualSpend(long projectId){
        String sql="SELECT actual_spend FROM project WHERE project_id =? ";
        double actualSpend=0;
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ResultSet rs= ps.executeQuery();
            if(rs.next()){
                actualSpend=rs.getDouble("actual_spend");
            }

            return actualSpend;

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public double updateProjectActualSpend(long projectId, double actualSpend) {
        double currentActualSpend=getCurrentActualSpend(projectId);
        currentActualSpend+=actualSpend;

        String sql="UPDATE project SET actual_spend=? WHERE project_id=?";
        String sql2="SELECT builder-id FROM project WHERE project_id=?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, currentActualSpend);
            ps.setLong(2, projectId);

            ps.executeUpdate();

            return currentActualSpend;

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

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

        String countPendingSql = """
    SELECT COUNT(*) AS pending_count
    FROM task
    WHERE project_id = ? AND status = 'PENDING'
    """;

        String getProjectSql = """
    SELECT status, builder_id, client_id
    FROM project
    WHERE project_id = ?
    """;

        String updateProjectStatusSql = """
    UPDATE project
    SET status = ?
    WHERE project_id = ?
    """;

        String insertNotificationSql = """
    INSERT INTO notification (user_id, message)
    VALUES (?, ?)
    """;

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
            psGetProject.setLong(1, projectId);
            try (ResultSet rs = psGetProject.executeQuery()) {
                if (rs.next()) {
                    currentStatus = rs.getString("status");
                    builderId = rs.getLong("builder_id");
                    clientId = rs.getLong("client_id");
                }
            }

            psUpdateTasks.setLong(1, projectId);
            psUpdateTasks.setInt(2, numberOfTasks);
            int updatedTasks = psUpdateTasks.executeUpdate();

            psCountPending.setLong(1, projectId);
            ResultSet rs = psCountPending.executeQuery();
            int pendingCount = 0;
            if (rs.next()) {
                pendingCount = rs.getInt("pending_count");
            }

            String newStatus = (pendingCount > 0) ? "IN PROGRESS" : "COMPLETED";

            if (!newStatus.equals(currentStatus)) {
                psUpdateProject.setString(1, newStatus);
                psUpdateProject.setLong(2, projectId);
                psUpdateProject.executeUpdate();

                String message = (newStatus.equals("COMPLETED"))
                        ? "Project (ID: " + projectId + ") has been completed!"
                        : "Project (ID: " + projectId + ") is now in progress.";

                psInsertNotification.setLong(1, builderId);
                psInsertNotification.setString(2, message);
                psInsertNotification.executeUpdate();

                psInsertNotification.setLong(1, clientId);
                psInsertNotification.setString(2, message);
                psInsertNotification.executeUpdate();

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
            throw new RuntimeException(sqlException);
        }
    }

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
