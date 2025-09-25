package builder.portfolio.controller;

import builder.portfolio.model.*;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import static builder.portfolio.util.ValidatorUtil.isValidDocumentPath;

/**
 * Controller class for Builder operations.
 * Handles creating, updating, and deleting projects, uploading documents,
 * managing project managers, and viewing project portfolio and timelines.
 */
@Slf4j
public class BuilderController {

    private static final BuilderService builderService = new BuilderService();
    private static final CommonRepository commonRepository = new CommonRepository();
    private AuditTrail auditTrail = new AuditTrail();

    /**
     * Creates a new project.
     * Prompts the builder for project details including name, budget, manager, client,
     * estimated completion date, and number of phases. Validates the date and uses
     * BuilderService to create the project. Records an audit trail for the creation.
     */
    public void createProject() {
        String projectName = InputUtil.readString("Enter Project Name: ");
        double plannedBudget = InputUtil.readDouble("Enter Estimate budget: ");
        long managerId = commonRepository.availableProjectManagers();
        long clientId = commonRepository.availableClients();
        LocalDate endDate;

        while (true) {
            endDate = InputUtil.readDate("Enter the estimated Completion Date (dd-MM-yyyy): ");
            if (ValidatorUtil.isValidDate(endDate)) {
                break;
            } else {
                System.out.println("Date cannot be in the past. Try again.");
            }
        }

        int numberOfTasks = InputUtil.readInt("Enter total no. of phases: ");
        Project project = builderService.createProjectService(projectName, plannedBudget, 0, managerId, clientId, endDate, numberOfTasks);

        if (project != null) {
            SessionManager.setCurrentProject(project);
            auditTrail = new AuditTrail("Create Project", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            log.info("\n===Project added successfully====");
        } else {
            log.info("Unable to add Project");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Updates an existing project.
     * Prompts the builder for the project ID, new name, and updated budget.
     * Calls BuilderService to update the project and logs the operation in the audit trail.
     */
    public void updateProject() {
        long projectId = commonRepository.availableProjects();
        String projectName = InputUtil.readString("Enter Project Name: ");
        double plannedBudget = InputUtil.readDouble("Enter Estimate budget: ");

        Project project = builderService.updateProjectService(projectId, projectName, plannedBudget);

        if (project != null) {
            auditTrail = new AuditTrail("Update Project", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            log.info("Update successful");
        } else {
            log.info("Failed");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Deletes a project.
     * Prompts the builder for the project ID and deletes it using BuilderService.
     * Records an audit trail for the deletion.
     */
    public void deleteProject() {
        long projectId = commonRepository.availableProjects();
        boolean deletedProject = builderService.deleteProjectService(projectId);

        if (deletedProject) {
            auditTrail = new AuditTrail("Delete Project", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            log.info("Delete successful");
        } else {
            log.info("Failed");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Updates the project manager of a project.
     * Prompts for project ID and new project manager ID and updates the assignment
     * via BuilderService. Records an audit trail if successful.
     */
    public void updateProjectManager() {
        long projectId = commonRepository.availableProjects();
        long projectManagerId = commonRepository.availableProjectManagers();
        boolean updateStatus = builderService.updateProjectManagerService(projectId, projectManagerId);

        if (updateStatus) {
            System.out.println("Manager Updated successfully");
            auditTrail = new AuditTrail("Updated Project Manager", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
        } else {
            log.info("Error updating manager");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Uploads documents for a project.
     * Prompts the builder for project ID, document name, and document path.
     * Validates the document path and stores the document via BuilderService.
     * Records an audit trail for successful uploads.
     */
    public void uploadDocuments() {
        long projectId = commonRepository.availableProjects();
        String documentName = InputUtil.readString("Enter Document Name: ");
        String documentPath;

        while (true) {
            documentPath = InputUtil.readString("Enter Document Path (.pdf, .png, .jpg): ");
            if (isValidDocumentPath(documentPath)) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a file ending with .pdf, .png, or .jpg");
            }
        }

        Document savedDoc = builderService.uploadDocumentDetails(projectId, documentName, documentPath);

        if (savedDoc != null) {
            log.info("Document saved successfully");
            auditTrail = new AuditTrail("Documents Saved Successfully", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    /**
     * Views the project portfolio.
     * Provides options for budget tracking, project phase status, and viewing uploaded documents.
     * Calls corresponding BuilderService methods based on user input.
     */
    public void viewPortfolio() {
        long projectId = commonRepository.availableProjects();

        System.out.println("1. Budget tracking");
        System.out.println("2. Project Phase Status");
        System.out.println("3. View Documents");
        System.out.println("4. Exit");

        String choice = InputUtil.readString("Enter the operation to perform on project: ");

        while (true) {
            switch (choice) {
                case "1" -> builderService.budgetTrack(projectId);
                case "2" -> builderService.projectStatus(projectId);
                case "3" -> builderService.getUploadADocs(projectId);
                case "4" -> DashboardController.showDashboard(SessionManager.getCurrentUser());
                default -> System.out.println("Invalid choice! Try Again");
            }
        }
    }

    /**
     * Views the timeline of a project.
     * Fetches the project timeline using BuilderService and displays completed tasks,
     * remaining tasks, days remaining, and progress. Logs a message if timeline cannot be fetched.
     */
    public void viewTimeLine() {
        long projectId = commonRepository.availableProjects();
        ProjectTimeline timeline = builderService.getProjectTimeline(projectId);

        if (timeline == null) {
            log.info("Could not fetch project timeline.");
            return;
        }

        log.info("Timeline:");
        System.out.println("Project: " + timeline.getProjectName());
        System.out.println("Completed Tasks: " + timeline.getCompletedTasks());
        System.out.println("Remaining Tasks: " + timeline.getRemainingTasks());
        System.out.println("Days Remaining: " + timeline.getDaysRemaining());
        System.out.println("Progress: " + timeline.getGanttChart());

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }
}
