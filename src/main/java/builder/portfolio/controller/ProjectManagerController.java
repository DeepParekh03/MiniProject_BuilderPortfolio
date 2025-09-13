package builder.portfolio.controller;

import builder.portfolio.model.Document;
import builder.portfolio.model.Task;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static builder.portfolio.util.ValidatorUtil.isValidDocumentPath;

public class ProjectManagerController {
    BuilderController builderController=new BuilderController();
    BuilderService builderService=new BuilderService();


    public  void viewProjects(){
        long projectId =builderController.availableProjects();
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




    public void uploadDocuments(){
        long projectId =builderController.availableProjects();
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
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }


}
