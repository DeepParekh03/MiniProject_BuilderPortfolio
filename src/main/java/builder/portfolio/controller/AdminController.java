package builder.portfolio.controller;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.AdminService;
import builder.portfolio.util.FileReaderUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AdminController {
    AdminService adminService=new AdminService();
    public void viewAllProjects(){
        List<Project> projectList=new ArrayList<>();
        projectList= adminService.viewAllProjects(SessionManager.getCurrentUser());
        if(projectList.isEmpty()){
            log.info("No current projects in the system");
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
        DashboardController.showDashboard(SessionManager.getCurrentUser());


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
            log.info("Manager Deleted Successfully");
        }
        else{
            log.info("Error Deleting Manager");
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
            log.info("Client Deleted Successfully");
        }
        else{
            log.info("Error Deleting Client");
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
            log.info("Builder Deleted Successfully");
        }
        else{
            log.info("Error Deleting Builder");
        }
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }
}
