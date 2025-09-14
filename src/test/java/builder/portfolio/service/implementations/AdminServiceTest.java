package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.AdminRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    private AdminService adminService;
    private AdminRepository mockAdminRepository;

    @BeforeEach
    void setUp() {
        mockAdminRepository = mock(AdminRepository.class);
        adminService = new AdminService();

        adminService.adminRepository = mockAdminRepository;
    }

    @Test
    void testViewAllProjects_ReturnsProjects() {
        User mockUser = new User();
        mockUser.setUserId(1L);

        List<Project> mockProjects = Arrays.asList(
                new Project(1L, "Project A", Status.IN_PROGRESS, 0, 0, 0, 0, 0, LocalDate.now(),null, null),
                new Project(2L, "Project B", Status.IN_PROGRESS, 0, 0, 0, 0, 0, LocalDate.now(),null, null)

                );

        try (MockedStatic<SessionManager> mockedSession = Mockito.mockStatic(SessionManager.class);
             MockedStatic<CommonRepository> mockedCommonRepo = Mockito.mockStatic(CommonRepository.class)) {

            mockedSession.when(SessionManager::getCurrentUser).thenReturn(mockUser);
            mockedCommonRepo.when(() -> CommonRepository.getAllProjects(mockUser)).thenReturn(mockProjects);

            List<Project> result = adminService.viewAllProjects(mockUser);

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Project A", result.get(0).getProjectName());
            mockedCommonRepo.verify(() -> CommonRepository.getAllProjects(mockUser), times(1));
        }
    }

    @Test
    void testDeleteProjectManager_Success() {
        when(mockAdminRepository.deleteProjectManager(anyLong())).thenReturn(true);

        boolean result = adminService.deleteProjectManager(1L);

        assertTrue(result);
        verify(mockAdminRepository, times(1)).deleteProjectManager(1L);
    }

    @Test
    void testDeleteClient_Success() {
        when(mockAdminRepository.deleteProjectManager(anyLong())).thenReturn(true);

        boolean result = adminService.deleteClient(2L);

        assertTrue(result);
        verify(mockAdminRepository, times(1)).deleteProjectManager(2L);
    }

    @Test
    void testDeleteBuilder_Success() {
        when(mockAdminRepository.deleteProjectManager(anyLong())).thenReturn(true);

        boolean result = adminService.deleteBuilder(3L);

        assertTrue(result);
        verify(mockAdminRepository, times(1)).deleteProjectManager(3L);
    }

    @Test
    void testDeleteMethods_Failure() {
        when(mockAdminRepository.deleteProjectManager(anyLong())).thenReturn(false);

        assertFalse(adminService.deleteProjectManager(10L));
        assertFalse(adminService.deleteClient(11L));
        assertFalse(adminService.deleteBuilder(12L));

        verify(mockAdminRepository, times(3)).deleteProjectManager(anyLong());
    }
}
