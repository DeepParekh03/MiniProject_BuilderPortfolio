package builder.portfolio.controller;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;

import java.util.List;

public class BuilderController {
   private static final BuilderService builderService = new BuilderService();

    public static void createProject(){
        String projectName= InputUtil.readString("Enter Project Name: ");
        double plannedBudget= InputUtil.readDouble("Enter Estimate budget: ");
        System.out.println("Available Managers: ");
        List<User> managerList= BuilderRepository.getAllData(String.valueOf(UserRole.PROJECT_MANAGER));
        managerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long managerId = ValidatorUtil.validateId(
                "Select manager ID: ",
                managerList,
                User::getUserId
        );
        System.out.println("Available Clients: ");
        List<User> clientList= BuilderRepository.getAllData(String.valueOf(UserRole.CLIENT));
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long clientId = ValidatorUtil.validateId(
                "Select client ID: ",
                clientList,
                User::getUserId
        );
        int numberOfTasks=InputUtil.readInt("Enter total no. of phases: ");
        Project project=new Project();
        project=builderService.saveProject(projectName,plannedBudget,0,managerId,clientId,numberOfTasks);
        if(project!=null) {
            SessionManager.setCurrentProject(project);
            System.out.println("\n===Project added successfully====");
        }
        else{
            System.out.println("Unable to add Project");
        }

    }

    public static void uploadDocuments(){
        System.out.println("Available Projects: ");
        List<Project> projectList= BuilderRepository.getAllProjectsBuilder(SessionManager.getCurrentUser());
        projectList.forEach(project ->
                System.out.println("Project ID: " + project.getProjectId()
                        + ", Project Name: " + project.getProjectName())
        );
        String documentName= InputUtil.readString("Enter Document Name: ");
        String documentPath= InputUtil.readString("Enter Document Path(.pdf,.png,.jpg): ");
        builderService.uploadDocumentDetails(documentName,documentPath);
    }

}
