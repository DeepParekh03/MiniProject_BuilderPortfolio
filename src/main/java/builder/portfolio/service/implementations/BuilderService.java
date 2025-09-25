package builder.portfolio.service.implementations;

import builder.portfolio.controller.DashboardController;
import builder.portfolio.model.*;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IBuilderService;
import builder.portfolio.util.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class for Builder operations in the Builder Portfolio Management System.
 * This class provides methods for project creation, updating project manager,
 * uploading documents, tracking budget, viewing project status, and handling project timelines.
 */
public class BuilderService implements IBuilderService {

    private final BuilderRepository repository = new BuilderRepository();
    private final CommonRepository commonRepository = new CommonRepository();

    /**
     * Creates a new project with the given details and initializes tasks for the project.
     *
     * @param projectName   the name of the project
     * @param plannedBudget the planned budget for the project
     * @param actualSpend   the initial actual spend (usually 0)
     * @param manager       the ID of the assigned project manager
     * @param client        the ID of the client
     * @param endDate       the planned end date of the project
     * @param numberOfTasks the number of tasks to initialize for the project
     * @return the saved Project object with generated project ID
     */
    @Override
    public Project createProjectService(String projectName,
                                        double plannedBudget,
                                        double actualSpend,
                                        long manager,
                                        long client,
                                        LocalDate endDate,
                                        int numberOfTasks) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setPlannedBudget(plannedBudget);
        project.setActualSpend(0);
        project.setBuilderId(SessionManager.getCurrentUser().getUserId());
        project.setProjectManagerId(manager);
        project.setClientId(client);
        project.setEndDate(endDate);
        project.setStatus(Status.UPCOMING);

        Project savedProject = repository.createProjectRepository(project);

        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= numberOfTasks; i++) {
            Task task = new Task();
            task.setProjectId(savedProject.getProjectId());
            task.setTaskName("Phase " + i);
            task.setStatus("PENDING");
            tasks.add(task);
        }

        for (Task task : tasks) {
            repository.saveTask(task);
        }

        return savedProject;
    }

    /**
     * Updates the project manager for a given project.
     *
     * @param projectId       the ID of the project
     * @param projectManagerId the ID of the new project manager
     * @return true if the project manager was successfully updated, false otherwise
     */
    @Override
    public boolean updateProjectManagerService(long projectId, long projectManagerId) {
        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectManagerId(projectManagerId);
        return repository.updateProjectManagerRepository(project);
    }

    /**
     * Uploads a document associated with a project.
     *
     * @param projectId    the ID of the project
     * @param documentName the name of the document
     * @param documentPath the file path of the document
     * @return the saved Document object with generated document ID
     */
    @Override
    public Document uploadDocumentDetails(long projectId, String documentName, String documentPath) {
        Document document = new Document();
        document.setProjectId(projectId);
        document.setDocumentName(documentName);
        document.setUploadedBy(SessionManager.getCurrentUser().getUserName());
        document.setFilePath(documentPath);
        document.setType(documentPath.substring(documentPath.length() - 3));
        return repository.uploadDocumentDB(document);
    }

    /**
     * Tracks and prints the budget status of a project.
     *
     * @param projectId the ID of the project
     */
    @Override
    public void budgetTrack(long projectId) {
        String budgetStatus = commonRepository.trackBudget(projectId);
        if (budgetStatus != null) {
            System.out.println("Budget Status of Project: " + budgetStatus);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    /**
     * Displays the status of all tasks within a project.
     *
     * @param projectId the ID of the project
     */
    @Override
    public void projectStatus(long projectId) {
        List<Task> taskList = CommonRepository.getAllTasks(projectId);
        taskList.forEach(task -> {
            String updatedInfo = !Objects.equals(task.getStatus(), "COMPLETED")
                    ? "Not worked on"
                    : "Last Updated At: " + task.getUpdatedAt();
            System.out.println(task.getTaskName() + " | " + task.getStatus() + " | "
                    + "Created At: " + task.getCreatedAt() + " | " + updatedInfo);
        });
        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Prints all uploaded documents for a project.
     *
     * @param projectId the ID of the project
     */
    @Override
    public void getUploadADocs(long projectId) {
        List<Document> documentList = CommonRepository.getAllDocuments(projectId);
        if (documentList.isEmpty()) {
            System.out.println("No docs to be shown");
        } else {
            documentList.forEach(document -> {
                System.out.println("Document Name: " + document.getDocumentName()
                        + ", Document Type: " + document.getType()
                        + ", Document URL: " + document.getFilePath()
                        + ", Uploaded By: " + document.getUploadedBy());
            });
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Updates project details such as name and planned budget.
     *
     * @param projectId     the ID of the project
     * @param projectName   the updated project name
     * @param plannedBudget the updated planned budget
     * @return the updated Project object
     */
    @Override
    public Project updateProjectService(long projectId, String projectName, double plannedBudget) {
        Project project = new Project();
        project.setProjectName(projectName);
        project.setPlannedBudget(plannedBudget);
        project.setProjectId(projectId);
        return repository.updateProjectRepository(project);
    }

    /**
     * Deletes a project by its ID.
     *
     * @param projectId the ID of the project
     * @return true if the project was successfully deleted, false otherwise
     */
    @Override
    public boolean deleteProjectService(long projectId) {
        Project project = new Project();
        project.setProjectId(projectId);
        return repository.deleteProjectRepository(project);
    }

    /**
     * Retrieves the project timeline for a project.
     *
     * @param projectId the ID of the project
     * @return a ProjectTimeline object containing project timeline details
     */
    @Override
    public ProjectTimeline getProjectTimeline(long projectId) {
        return repository.getProjectTimeline(projectId);
    }
}
