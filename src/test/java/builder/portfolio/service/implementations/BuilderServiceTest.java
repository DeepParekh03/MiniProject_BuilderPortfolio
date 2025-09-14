package builder.portfolio.service.implementations;

import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.Document;
import builder.portfolio.model.ProjectTimeline;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.util.DBUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuilderServiceTest {

    private BuilderRepository builderRepository;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        builderRepository = new BuilderRepository();

        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    void testCreateProjectRepository_Success() throws Exception {
        Project project = new Project();
        project.setProjectName("Test Project");
        project.setPlannedBudget(1000);
        project.setActualSpend(0);
        project.setBuilderId(1);
        project.setProjectManagerId(2);
        project.setClientId(3);
        project.setEndDate(LocalDate.now());

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("project_id")).thenReturn(100L);

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            Project createdProject = builderRepository.createProjectRepository(project);

            assertNotNull(createdProject);
            assertEquals(100L, createdProject.getProjectId());
            verify(preparedStatement, times(1)).setString(eq(1), eq("Test Project"));
            verify(preparedStatement, times(1)).executeQuery();
        }
    }

    @Test
    void testUpdateProjectRepository_Success() throws Exception {
        Project project = new Project();
        project.setProjectId(100);
        project.setProjectName("Updated Project");
        project.setPlannedBudget(2000);
        project.setClientId(3);
        project.setProjectManagerId(2);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("project_id")).thenReturn(100L);

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            Project updatedProject = builderRepository.updateProjectRepository(project);

            assertNotNull(updatedProject);
            assertEquals(100L, updatedProject.getProjectId());
            verify(preparedStatement, times(1)).setString(eq(1), eq("Updated Project"));
        }
    }

    @Test
    void testDeleteProjectRepository_Success() throws Exception {
        Project project = new Project();
        project.setProjectId(99);
        project.setProjectName("Delete Me");
        project.setClientId(3);
        project.setProjectManagerId(2);

        PreparedStatement ps1 = mock(PreparedStatement.class);
        PreparedStatement ps2 = mock(PreparedStatement.class);
        PreparedStatement ps3 = mock(PreparedStatement.class);

        when(connection.prepareStatement("DELETE FROM document WHERE project_id = ?")).thenReturn(ps1);
        when(connection.prepareStatement("DELETE FROM task WHERE project_id = ?")).thenReturn(ps2);
        when(connection.prepareStatement("DELETE FROM project WHERE project_id = ?")).thenReturn(ps3);

        when(ps3.executeUpdate()).thenReturn(1); // simulate project deletion

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            boolean result = builderRepository.deleteProjectRepository(project);

            assertTrue(result);
            verify(ps3, times(1)).executeUpdate();
            verify(connection, times(1)).commit();
        }
    }

    @Test
    void testUploadDocumentDB_Success() throws Exception {
        Document document = new Document();
        document.setProjectId(1);
        document.setDocumentName("Spec.pdf");
        document.setFilePath("/tmp/spec.pdf");
        document.setType("PDF");
        document.setUploadedBy("Builder");

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("document_id")).thenReturn(123L);

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            Document savedDoc = builderRepository.uploadDocumentDB(document);

            assertNotNull(savedDoc);
            assertEquals(123L, savedDoc.getDocumentId());
        }
    }

    @Test
    void testSaveTask_Success() throws Exception {
        Task task = new Task();
        task.setProjectId(1);
        task.setTaskName("Phase 1");
        task.setStatus(Status.UPCOMING.name());

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("task_id")).thenReturn(10L);

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            Task savedTask = builderRepository.saveTask(task);

            assertNotNull(savedTask);
            assertEquals(10L, savedTask.getTaskId());
        }
    }

    @Test
    void testGetProjectTimeline_Success() throws Exception {
        when(resultSet.next()).thenReturn(true).thenReturn(true); // task count + project query
        when(resultSet.getInt("completed_tasks")).thenReturn(3);
        when(resultSet.getInt("total_tasks")).thenReturn(5);
        when(resultSet.getString("project_name")).thenReturn("Timeline Test");
        when(resultSet.getDate("end_date")).thenReturn(Date.valueOf(LocalDate.now().plusDays(10)));

        try (MockedStatic<DBUtil> dbUtilMock = Mockito.mockStatic(DBUtil.class)) {
            dbUtilMock.when(DBUtil::getConnection).thenReturn(connection);

            ProjectTimeline timeline = builderRepository.getProjectTimeline(1L);

            assertNotNull(timeline);
            assertEquals("Timeline Test", timeline.getProjectName());
            assertEquals(3, timeline.getCompletedTasks());
            assertEquals(5, timeline.getTotalTasks());
        }
    }
}

