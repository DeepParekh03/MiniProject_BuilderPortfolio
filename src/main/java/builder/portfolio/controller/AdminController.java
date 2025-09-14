package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.AdminService;
import builder.portfolio.util.FileReaderUtil;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminController {
    AdminService adminService=new AdminService();
    public void viewAllProjects(){
        List<Project> projectList=new ArrayList<>();
        projectList= adminService.viewAllProjects(SessionManager.getCurrentUser());
        if(projectList.isEmpty()){
            System.out.println("No current projects in the system");
        }
        else{
            projectList.forEach(project ->
                    System.out.println("Project ID: " + project.getProjectId()
                            + ", Project Name: " + project.getProjectName()
                            + ", Project Status: " + project.getStatus())
            );
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    public void viewAuditTrail(){
        FileReaderUtil.readAuditTrails().forEach(audit -> {
            System.out.println("Action: " + audit.getAction()+
                    ", Performed By: " + audit.getPerformedBy().getUserName()+
                    ", User Type: "+audit.getPerformedBy().getRole());
        });


    }
    public void deleteProjectManager(){
        boolean deleteManager=false;
        List<User> projectManagerList= CommonRepository.getAllUsers(UserRole.PROJECT_MANAGER);
        projectManagerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long projectMangerId = ValidatorUtil.validateId(
                "Select Project Manager ID to remove: ",
                projectManagerList,
                User::getUserId
        );
        deleteManager=adminService.deleteProjectManager(projectMangerId);
        if(deleteManager){
            System.out.println("Manager Deleted Successfully");
        }
        else{
            System.out.println("Error Deleting Manager");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }
    public void deleteClient() {
        boolean deleteClient=false;
        List<User> clientList= CommonRepository.getAllUsers(UserRole.CLIENT);
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long clientId = ValidatorUtil.validateId(
                "Select Client ID to remove: ",
                clientList,
                User::getUserId
        );
        deleteClient=adminService.deleteClient(clientId);
        if(deleteClient){
            System.out.println("Client Deleted Successfully");
        }
        else{
            System.out.println("Error Deleting Client");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }
    public void deleteBuilder(){
        boolean deleteBuilderDetails=false;
        List<User> builderList= CommonRepository.getAllUsers(UserRole.BUILDER);
        builderList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long builderId = ValidatorUtil.validateId(
                "Select Builder ID to remove: ",
                builderList,
                User::getUserId
        );
        deleteBuilderDetails= adminService.deleteBuilder(builderId);
        if(deleteBuilderDetails){
            System.out.println("Builder Deleted Successfully");
        }
        else{
            System.out.println("Error Deleting Builder");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }
}
