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
        String sql = """
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

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, projectId);
            ps.setInt(2, numberOfTasks);

            return ps.executeUpdate();

        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
