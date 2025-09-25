package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.ProjectTimeline;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.service.implementations.ClientService;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.SessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for Client operations.
 * Handles actions related to viewing owned projects, tracking budgets,
 * accessing uploaded documents, and viewing project timelines.
 * Records audit trails for client activities.
 */
@Slf4j
public class ClientController {

    private final ClientService clientService = new ClientService();
    private final BuilderService builderService = new BuilderService();
    private final CommonRepository commonRepository = new CommonRepository();
    private AuditTrail auditTrail = new AuditTrail();

    /**
     * Displays all projects owned by the currently logged-in client.
     * If no projects are found, logs an appropriate message.
     * Records an audit trail for viewing projects.
     */
    public void viewOwnedProjects() {
        List<Project> projectList = clientService.viewOwnedProjects(SessionManager.getCurrentUser());

        if (projectList.isEmpty()) {
            log.info("Unable to Load Projects");
        } else {
            auditTrail = new AuditTrail("View Projects", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            projectList.forEach(project ->
                    System.out.println("Project ID: " + project.getProjectId()
                            + ", Project Name: " + project.getProjectName()
                            + ", Project Status: " + project.getStatus())
            );

            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    /**
     * Tracks the budget of a selected project.
     * Prompts the client to select a project and retrieves the budget status
     * from ClientService. Logs the status and creates an audit trail entry.
     */
    public void trackBudget() {
        long projectId = commonRepository.availableProjects();
        String budgetStatus = clientService.trackBudget(projectId);

        if (budgetStatus != null) {
            auditTrail = new AuditTrail("Budget Tracked", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            log.info("Budget Status of Project: {}", budgetStatus);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    /**
     * Retrieves documents uploaded for a specific project.
     * Prompts the client to select a project and retrieves the list of documents.
     * Displays document details including name, type, URL, and uploader.
     * Records an audit trail for viewing documents.
     */
    public void getUploadedDocs() {
        long projectId = commonRepository.availableProjects();
        List<Document> documentList = clientService.getUploadedDocs(projectId);

        if (documentList == null || documentList.isEmpty()) {
            log.info("No documents to be shown");
        } else {
            auditTrail = new AuditTrail("Viewed Saved Documents", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            documentList.forEach(document -> {
                System.out.println("Document Name: " + document.getDocumentName()
                        + ", Document Type: " + document.getType()
                        + ", Document URL: " + document.getFilePath()
                        + ", Uploaded By: " + document.getUploadedBy());
            });

            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    /**
     * Views the timeline of a selected project.
     * Prompts the client to select a project and fetches its timeline from BuilderService.
     * Displays completed tasks, remaining tasks, days remaining, and project progress.
     * Logs a message if the timeline cannot be fetched.
     */
    public void viewTimeLine() {
        long projectId = commonRepository.availableProjects();
        ProjectTimeline timeline = builderService.getProjectTimeline(projectId);

        if (timeline == null) {
            log.info("Could not fetch project timeline.");
            return;
        }

        System.out.println("Project: " + timeline.getProjectName());
        System.out.println("Completed Tasks: " + timeline.getCompletedTasks());
        System.out.println("Remaining Tasks: " + timeline.getRemainingTasks());
        System.out.println("Days Remaining: " + timeline.getDaysRemaining());
        System.out.println("Progress: " + timeline.getGanttChart());

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }
}
