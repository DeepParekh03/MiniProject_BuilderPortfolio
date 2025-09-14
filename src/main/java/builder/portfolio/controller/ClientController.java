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

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    ClientService clientService=new ClientService();
    BuilderService builderService = new BuilderService();
    CommonRepository commonRepository=new CommonRepository();
    AuditTrail auditTrail=new AuditTrail();

    public void viewOwnedProjects(){
        List<Project> projectList=new ArrayList<>();
        projectList=clientService.viewOwnedProjects(SessionManager.getCurrentUser());
        if(projectList.isEmpty()){
            System.out.println("Unable to Load Projects");
        }else{
            auditTrail=new AuditTrail("View Projects",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            projectList.forEach(project ->
                    System.out.println("Project ID: " + project.getProjectId()
                            + ", Project Name: " + project.getProjectName()
                            + ", Project Status: " + project.getStatus())
            );

            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }


    public void trackBudget(){
        String budgetStatus="";
        long projectId= commonRepository.availableProjects();
        budgetStatus= clientService.trackBudget(projectId);
        if(budgetStatus!=null){
            auditTrail=new AuditTrail("Budget Tracked",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            System.out.println("Budget Status of Project: "+budgetStatus);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    public void getUploadedDocs(){
        long projectId=commonRepository.availableProjects();
        List<Document> documentList=new ArrayList<>();
        documentList=clientService.getUploadedDocs(projectId);
        if(documentList==null){
            System.out.println("No docs to be shown");

        }else{
            auditTrail=new AuditTrail("Viewed Saved Documents",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            documentList.forEach(document -> {
                System.out.println("Document Name: "+document.getDocumentName()
                +"Document Type: "+document.getType()
                +"Document URL: "+document.getFilePath()
                +"Uploaded By: "+document.getUploadedBy());
            });
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    public void viewTimeLine() {
        long projectId=commonRepository.availableProjects();
        ProjectTimeline timeline = builderService.getProjectTimeline(projectId);
        if (timeline == null) {
            System.out.println("Could not fetch project timeline.");
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
