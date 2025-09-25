package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.Document;
import builder.portfolio.model.Task;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.service.implementations.ProjectManagerService;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static builder.portfolio.util.ValidatorUtil.isValidDocumentPath;

/**
 * Controller class for Project Manager operations.
 * Handles actions such as viewing projects and tasks, updating project status,
 * updating actual spend, and uploading project-related documents.
 * Each action records an audit trail for traceability.
 */
@Slf4j
public class ProjectManagerController {

    private final CommonRepository commonRepository = new CommonRepository();
    private final BuilderService builderService = new BuilderService();
    private final ProjectManagerService projectManagerService = new ProjectManagerService();
    private AuditTrail auditTrail = new AuditTrail();

    /**
     * Displays all tasks for a selected project.
     * Prompts the project manager to select a project and fetches all tasks.
     * Prints task details including name, status, creation date, and last update date.
     */
    public void viewProjects() {
        long projectId = commonRepository.availableProjects();
        log.info("All Available tasks for selected project ID are: ");

        List<Task> taskList = CommonRepository.getAllTasks(projectId);
        taskList.forEach(task -> {
            System.out.println("Task Name: " + task.getTaskName()
                    + " Task Status: " + task.getStatus()
                    + " Created At: " + task.getCreatedAt()
                    + " Last Updated At: " + task.getUpdatedAt());
        });

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Updates the status of tasks in a selected project.
     * Prompts the project manager for the number of tasks completed and updates
     * the project status using ProjectManagerService. Records an audit trail entry.
     */
    public void updateProjectStatus() {
        long projectId = commonRepository.availableProjects();
        int numberOfTasksCompleted = InputUtil.readInt("Enter number of tasks completed: ");
        int totalTaskUpdate = projectManagerService.updateProjectStatus(projectId, numberOfTasksCompleted);

        if (totalTaskUpdate == 0) {
            System.out.println("No pending tasks available for this project.");
        } else {
            auditTrail = new AuditTrail("Updated Project Status", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            log.info("{} tasks marked as COMPLETED.", totalTaskUpdate);
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Updates the actual amount spent on a selected project.
     * Prompts the project manager to enter the actual spend and updates
     * it using ProjectManagerService. Records an audit trail entry if successful.
     */
    public void updateProjectActualSpend() {
        long projectId = commonRepository.availableProjects();
        double actualSpend = InputUtil.readDouble("Enter amount spent: ");
        double moneySpent = projectManagerService.updateActualSpend(projectId, actualSpend);

        if (moneySpent != 0) {
            auditTrail = new AuditTrail("Updated Project Spend", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            log.info("Updated Project Actual Spend: {}", moneySpent);
        } else {
            System.out.println("Error updating");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Uploads a document for a selected project.
     * Prompts the project manager for the document name and path, validates the path,
     * and saves the document using BuilderService. Records an audit trail entry for the upload.
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
            auditTrail = new AuditTrail("Document Saved", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }
}
