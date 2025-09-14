package builder.portfolio.repository;

import builder.portfolio.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        String updateProjectStatusSql = """
        UPDATE project
        SET status = ?
        WHERE project_id = ?
        """;

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement psUpdateTasks = connection.prepareStatement(updateTasksSql);
             PreparedStatement psCountPending = connection.prepareStatement(countPendingSql);
             PreparedStatement psUpdateProject = connection.prepareStatement(updateProjectStatusSql)) {

            connection.setAutoCommit(false);

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
            psUpdateProject.setString(1, newStatus);
            psUpdateProject.setLong(2, projectId);
            psUpdateProject.executeUpdate();

            connection.commit();
            return updatedTasks;

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}
