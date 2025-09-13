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


    public Project createProjectRepository(Project project) {
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

    public Project updateProjectRepository(Project project){
        String sql = "UPDATE project SET project_name = ?, planned_budget = ? WHERE project_id = ? RETURNING project_id";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, project.getProjectName());
            ps.setDouble(2, project.getPlannedBudget());
            ps.setLong(3, project.getProjectId());

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

    public boolean deleteProjectRepository(Project project){
        try (Connection connection = DBUtil.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM document WHERE project_id = ?");
                 PreparedStatement ps2 = connection.prepareStatement("DELETE FROM task WHERE project_id = ?");
                 PreparedStatement ps3 = connection.prepareStatement("DELETE FROM project WHERE project_id = ?");
                 ) {

                ps1.setLong(1, project.getProjectId());
                ps1.executeUpdate();

                ps2.setLong(1, project.getProjectId());
                ps2.executeUpdate();

                ps3.setLong(1, project.getProjectId());
                ps3.executeUpdate();

                int rows = ps3.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException sqlException) {
                connection.rollback();
                throw sqlException;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return false;
    }

    public boolean updateProjectManagerRepository(Project project) {
        String sql = "UPDATE project SET manager_id = ? WHERE project_id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, project.getProjectManagerId());
            ps.setLong(2, project.getProjectId());

            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

}
