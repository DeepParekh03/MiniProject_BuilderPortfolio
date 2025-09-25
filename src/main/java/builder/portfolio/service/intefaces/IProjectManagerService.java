package builder.portfolio.service.intefaces;

/**
 * Interface defining operations for a Project Manager in the Builder Portfolio Management System.
 * Provides methods for updating project status and tracking project actual spend.
 */
public interface IProjectManagerService {

    /**
     * Updates the status of a project based on the number of tasks completed.
     *
     * @param projectId      the ID of the project to update
     * @param numberOfTasks  the number of tasks completed in the project
     * @return the total number of tasks that were updated
     */
    int updateProjectStatus(long projectId, int numberOfTasks);

    /**
     * Updates the actual spend of a project by adding the provided amount.
     *
     * @param projectId    the ID of the project to update
     * @param actualSpend  the amount to add to the current actual spend
     * @return the updated actual spend for the project
     */
    double updateActualSpend(long projectId, double actualSpend);
}
