package builder.portfolio.service.implementations;

import builder.portfolio.repository.ProjectManagerRepository;
import builder.portfolio.service.intefaces.IProjectManagerService;

/**
 * Service class for Project Manager operations in the Builder Portfolio Management System.
 * Provides methods to update project status and track actual project spend.
 */
public class ProjectManagerService implements IProjectManagerService {

    private final ProjectManagerRepository projectManagerRepository = new ProjectManagerRepository();

    /**
     * Updates the status of a project based on the number of tasks completed.
     * Delegates the operation to the ProjectManagerRepository.
     *
     * @param projectId      the ID of the project to update
     * @param numberOfTasks  the number of tasks completed in the project
     * @return the total number of tasks that were updated
     */
    @Override
    public int updateProjectStatus(long projectId, int numberOfTasks) {
        return projectManagerRepository.updateProjectStatus(projectId, numberOfTasks);
    }

    /**
     * Updates the actual spend of a project by adding the provided amount.
     * Delegates the operation to the ProjectManagerRepository.
     *
     * @param projectId    the ID of the project to update
     * @param actualSpend  the amount to add to the current actual spend
     * @return the updated actual spend for the project
     */
    @Override
    public double updateActualSpend(long projectId, double actualSpend) {
        return projectManagerRepository.updateProjectActualSpend(projectId, actualSpend);
    }
}
