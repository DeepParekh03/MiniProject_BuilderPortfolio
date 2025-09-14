package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.Document;
import builder.portfolio.model.Task;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.service.implementations.ProjectManagrService;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static builder.portfolio.util.ValidatorUtil.isValidDocumentPath;

public class ProjectManagerController {
    CommonRepository commonRepository=new CommonRepository();
    BuilderService builderService=new BuilderService();
    ProjectManagrService projectManagrService=new ProjectManagrService();
    AuditTrail auditTrail=new AuditTrail();

    public  void viewProjects(){
        long projectId =commonRepository.availableProjects();
        System.out.println("All Available tasks for selected project ID are: ");
        List<Task> taskList=new ArrayList<>();
        taskList= CommonRepository.getAllTasks(projectId);
        taskList.stream().forEach(task -> {
            System.out.println("Task Name: " + task.getTaskName()
                    + " Task Status: " + task.getStatus()
                    + " Created At: " + task.getCreatedAt()
                    + " Last Updated At: " + task.getUpdatedAt());
        });
        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    public void updateProjectStatus(){
        long projectId=commonRepository.availableProjects();
        int numberOfTasksCompleted=InputUtil.readInt("Enter number of tasks completed: ");
        int totalTaskUpdate= projectManagrService.updateProjectStatus(projectId,numberOfTasksCompleted);
        if (totalTaskUpdate == 0) {
            System.out.println("No pending tasks available for this project.");
        } else {
            auditTrail=new AuditTrail("Updated Project Status",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            System.out.println(totalTaskUpdate + " tasks marked as COMPLETED.");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    public  void updateProjectActualSpend(){
        long projectId=commonRepository.availableProjects();
        double actualSpend=InputUtil.readDouble("Enter amount spend: ");
        double moneySpend=projectManagrService.updateActualSpend(projectId,actualSpend);
        if(moneySpend!=0){
            auditTrail=new AuditTrail("Updated Project Spend",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);
            System.out.println("Updated Project Actual Spend: "+moneySpend);
        }else{
            System.out.println("Error updating");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }




    public void uploadDocuments(){
        long projectId =commonRepository.availableProjects();
        String documentName= InputUtil.readString("Enter Document Name: ");
        String documentPath;
        while (true) {
            documentPath = InputUtil.readString("Enter Document Path (.pdf, .png, .jpg): ");
            if (isValidDocumentPath(documentPath)) {
                break;
            } else {
                System.out.println("Invalid choice. Please enter a file ending with .pdf, .png, or .jpg");
            }
        }
        Document savedDoc= builderService.uploadDocumentDetails(projectId,documentName,documentPath);
        if(savedDoc!=null){
            System.out.println("Document saved successfully");
            auditTrail=new AuditTrail("Document Saved",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }


}
