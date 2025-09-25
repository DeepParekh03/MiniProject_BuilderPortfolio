package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.ProjectTimeline;

import java.time.LocalDate;

/**
 * Interface defining operations available to a Builder in the Builder Portfolio Management System.
 * A Builder can create projects, update them, track budgets and project statuses,
 * upload documents, and retrieve project timelines.
 */
public interface IBuilderService {

    /**
     * Creates a new project with the specified details and initial tasks.
     *
     * @param projectName   the name of the project
     * @param plannedBudget the planned budget for the project
     * @param actualSpend   the initial actual spend (usually 0)
     * @param manager       the ID of the project manager
     * @param client        the ID of the client
     * @param endDate       the end date of the project
     * @param numberOfTasks the number of initial tasks for the project
     * @return the created {@link Project} object
     */
    Project createProjectService(String projectName,
                                 double plannedBudget,
                                 double actualSpend,
                                 long manager,
                                 long client,
                                 LocalDate endDate,
                                 int numberOfTasks);

    /**
     * Uploads a document to a specific project.
     *
     * @param projectId    the ID of the project
     * @param documentName the name of the document
     * @param documentPath the file path or URL of the document
     * @return the saved {@link Document} object
     */
    Document uploadDocumentDetails(long projectId, String documentName, String documentPath);

    /**
     * Tracks and displays the budget status of a project.
     *
     * @param projectId the ID of the project
     */
    void budgetTrack(long projectId);

    /**
     * Displays the current status of all tasks within a project.
     *
     * @param projectId the ID of the project
     */
    void projectStatus(long projectId);

    /**
     * Displays all documents uploaded for a specific project.
     *
     * @param projectId the ID of the project
     */
    void getUploadADocs(long projectId);

    /**
     * Retrieves the timeline details of a project.
     *
     * @param projectId the ID of the project
     * @return a {@link ProjectTimeline} object containing project progress details
     */
    ProjectTimeline getProjectTimeline(long projectId);

    /**
     * Updates the details of a project, such as its name and planned budget.
     *
     * @param projectId     the ID of the project to update
     * @param pojectName    the new project name
     * @param plannedBudget the new planned budget
     * @return the updated {@link Project} object
     */
    Project updateProjectService(long projectId, String pojectName, double plannedBudget);

    /**
     * Updates the project manager assigned to a specific project.
     *
     * @param projectId        the ID of the project
     * @param projectManagerId the ID of the new project manager
     * @return true if the update was successful, false otherwise
     */
    boolean updateProjectManagerService(long projectId, long projectManagerId);

    /**
     * Deletes a project from the system.
     *
     * @param projectId the ID of the project to delete
     * @return true if the project was successfully deleted, false otherwise
     */
    boolean deleteProjectService(long projectId);
}
