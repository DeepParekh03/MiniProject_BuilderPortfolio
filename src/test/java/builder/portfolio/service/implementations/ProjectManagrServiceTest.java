package builder.portfolio.service.implementations;

import builder.portfolio.repository.ProjectManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProjectManagrServiceTest {

    private ProjectManagerRepository projectManagerRepository;
    private ProjectManagrService projectManagrService;

    @BeforeEach
    void setUp() {
        projectManagerRepository = mock(ProjectManagerRepository.class);
        projectManagrService = new ProjectManagrService();

         try {
            var field = ProjectManagrService.class.getDeclaredField("projectManagerRepository");
            field.setAccessible(true);
            field.set(projectManagrService, projectManagerRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUpdateProjectStatus() {
        long projectId = 1L;
        int numberOfTasks = 5;
        int expectedTotalTaskUpdate = 5;

        when(projectManagerRepository.updateProjectStatus(projectId, numberOfTasks))
                .thenReturn(expectedTotalTaskUpdate);

        int result = projectManagrService.updateProjectStatus(projectId, numberOfTasks);

        assertEquals(expectedTotalTaskUpdate, result);
        verify(projectManagerRepository, times(1))
                .updateProjectStatus(projectId, numberOfTasks);
    }

    @Test
    void testUpdateActualSpend() {
        long projectId = 1L;
        double actualSpend = 1000.50;
        double expectedMoneySpent = 1000.50;

        when(projectManagerRepository.updateProjectActualSpend(projectId, actualSpend))
                .thenReturn(expectedMoneySpent);

        double result = projectManagrService.updateActualSpend(projectId, actualSpend);

        assertEquals(expectedMoneySpent, result);
        verify(projectManagerRepository, times(1))
                .updateProjectActualSpend(projectId, actualSpend);
    }
}
