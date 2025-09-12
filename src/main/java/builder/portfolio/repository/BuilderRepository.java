package builder.portfolio.repository;

import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.enums.Status;
import builder.portfolio.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

        } catch (SQLException e) {
            e.printStackTrace();
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

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
