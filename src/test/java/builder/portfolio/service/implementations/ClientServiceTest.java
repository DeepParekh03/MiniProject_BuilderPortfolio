package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.util.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private ClientService clientService;
    private CommonRepository commonRepository;

    @BeforeEach
    void setUp() {
        commonRepository = mock(CommonRepository.class);
        clientService = new ClientService();
        clientService.commonRepository = commonRepository; // inject mock
    }

    @Test
    void testViewOwnedProjects() {
        User mockUser = new User();
        mockUser.setUserId(101L);

        List<Project> mockProjects = Arrays.asList(
                new Project(1L, "Project A", Status.IN_PROGRESS, 0, 0, 0, 0, 0, LocalDate.now(),null, null),
                new Project(2L, "Project B", Status.IN_PROGRESS, 0, 0, 0, 0, 0, LocalDate.now(),null, null)

        );

        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class);
             MockedStatic<CommonRepository> mockedCommonRepo = Mockito.mockStatic(CommonRepository.class)) {

            mockedSession.when(SessionManager::getCurrentUser).thenReturn(mockUser);
            mockedCommonRepo.when(() -> CommonRepository.getAllProjects(mockUser)).thenReturn(mockProjects);

            List<Project> result = clientService.viewOwnedProjects(mockUser);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Project A", result.get(0).getProjectName());
        }
    }

    @Test
    void testTrackBudget() {
        long projectId = 123L;
        when(commonRepository.trackBudget(projectId)).thenReturn("Budget is within limits");

        String result = clientService.trackBudget(projectId);

        assertNotNull(result);
        assertEquals("Budget is within limits", result);
    }

    @Test
    void testGetUploadedDocs() {
        long projectId = 456L;
        List<Document> mockDocs = Arrays.asList(
                new Document(1L, projectId, "Contract.pdf", ".pdf","http://dummy","deep"),

                new Document(2L, projectId, "Contract.pdf", ".pdf","http://dummy","deep")
        );

        try (MockedStatic<CommonRepository> mockedCommonRepo = Mockito.mockStatic(CommonRepository.class)) {
            mockedCommonRepo.when(() -> CommonRepository.getAllDocuments(projectId)).thenReturn(mockDocs);

            List<Document> result = clientService.getUploadedDocs(projectId);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Contract.pdf", result.get(0).getDocumentName());
        }
    }
}
