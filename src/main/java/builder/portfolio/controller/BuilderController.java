package builder.portfolio.controller;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;

import java.util.List;

import static builder.portfolio.util.ValidatorUtil.isValidDocumentPath;

public class BuilderController {
   private static final BuilderService builderService = new BuilderService();

    public void createProject(){
        String projectName= InputUtil.readString("Enter Project Name: ");
        double plannedBudget= InputUtil.readDouble("Enter Estimate budget: ");
        long managerId=availableProjectManagers();
        long clientId=availableClients();
        int numberOfTasks=InputUtil.readInt("Enter total no. of phases: ");
        Project project=new Project();
        project=builderService.createProjectService(projectName,plannedBudget,0,managerId,clientId,numberOfTasks);
        if(project!=null) {
            SessionManager.setCurrentProject(project);
            System.out.println("\n===Project added successfully====");
        }
        else{
            System.out.println("Unable to add Project");
        }

    }

    public void updateProject(){
        long projectId=availableProjects();
        String projectName= InputUtil.readString("Enter Project Name: ");
        double plannedBudget= InputUtil.readDouble("Enter Estimate budget: ");

        Project project=new Project();
        project=builderService.updateProjectService(projectId,projectName,plannedBudget);
        if(project!=null){
            DashboardController.showDashboard(SessionManager.getCurrentUser());
            System.out.println("Update successfull");
        }else{
            System.out.println("Failed");
        }
    }

    public void deleteProject(){
        long projectId=availableProjects();

        boolean deletedProject=builderService.deleteProjectService(projectId);
        if(deletedProject!=false){
            DashboardController.showDashboard(SessionManager.getCurrentUser());
            System.out.println("Delete successfull");
        }else{
            System.out.println("Failed");
        }
    }

    public void updateProjectManager(){
        long projectId=availableProjects();
        long projectManagerId=availableProjectManagers();


        boolean updateStatus=builderService.updateProjectManagerService(projectId,projectManagerId);
        if(updateStatus){
            System.out.println("Manager Updated successfully");
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
        else{
            System.out.println("Error updating manager");
        }
    }

    public void uploadDocuments(){
        long projectId =availableProjects();
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

    public long availableClients(){
        System.out.println("Available Clients: ");
        List<User> clientList= CommonRepository.getAllUsers(UserRole.CLIENT);
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long clientId = ValidatorUtil.validateId(
                "Select client ID: ",
                clientList,
                User::getUserId
        );
        return clientId;
    }

    public long availableProjectManagers(){
        System.out.println("Available Managers: ");
        List<User> managerList= CommonRepository.getAllUsers(UserRole.PROJECT_MANAGER);
        managerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long managerId = ValidatorUtil.validateId(
                "Select manager ID: ",
                managerList,
                User::getUserId
        );
        return  managerId;
    }

    public long availableProjects(){
        System.out.println("Available Projects: ");
        List<Project> projectList= CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        projectList.forEach(project ->
                System.out.println("Project ID: " + project.getProjectId()
                        + ", Project Name: " + project.getProjectName()
                        + ", Current Project Manager ID: " + project.getProjectManagerId())
        );
        long projectId=InputUtil.readLong("Select Project Id: ");

        return projectId;
    }



}
