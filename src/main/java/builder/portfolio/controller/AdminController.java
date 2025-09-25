package builder.portfolio.controller;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.AdminService;
import builder.portfolio.util.FileReaderUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for administrative actions in the system.
 * Handles operations such as viewing all projects, viewing audit trails,
 * and deleting users including Project Managers, Clients, and Builders.
 */
@Slf4j
public class AdminController {

    private final AdminService adminService = new AdminService();

    /**
     * Displays all projects in the system.
     * Retrieves the project list from AdminService and prints details to console.
     * If no projects are found, logs a message indicating the system has no projects.
     * After displaying the projects, shows the admin dashboard.
     */
    public void viewAllProjects() {
        List<Project> projectList = new ArrayList<>();
        projectList = adminService.viewAllProjects(SessionManager.getCurrentUser());

        if (projectList.isEmpty()) {
            log.info("No current projects in the system");
        } else {
            projectList.forEach(project ->
                    System.out.println("Project ID: " + project.getProjectId()
                            + ", Project Name: " + project.getProjectName()
                            + ", Project Status: " + project.getStatus())
            );
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Displays the audit trail of system actions.
     * Reads audit trail data using FileReaderUtil and prints each action
     * along with the performing user's name and role.
     * After displaying the audit trail, shows the admin dashboard.
     */
    public void viewAuditTrail() {
        FileReaderUtil.readAuditTrails().forEach(audit -> {
            System.out.println("Action: " + audit.getAction() +
                    ", Performed By: " + audit.getPerformedBy().getUserName() +
                    ", User Type: " + audit.getPerformedBy().getRole());
        });

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Deletes a Project Manager from the system.
     * Retrieves all Project Managers, displays their details, and asks for a valid ID.
     * Uses AdminService to perform the deletion and logs the result.
     * Finally, shows the admin dashboard.
     */
    public void deleteProjectManager() {
        List<User> projectManagerList = CommonRepository.getAllUsers(UserRole.PROJECT_MANAGER);

        projectManagerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );

        long projectManagerId = ValidatorUtil.validateId(
                "Select Project Manager ID to remove: ",
                projectManagerList,
                User::getUserId
        );

        boolean deleteManager = adminService.deleteProjectManager(projectManagerId);

        if (deleteManager) {
            log.info("Manager Deleted Successfully");
        } else {
            log.info("Error Deleting Manager");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Deletes a Client from the system.
     * Retrieves all Clients, displays their details, and asks for a valid ID.
     * Uses AdminService to perform the deletion and logs the result.
     * Finally, shows the admin dashboard.
     */
    public void deleteClient() {
        List<User> clientList = CommonRepository.getAllUsers(UserRole.CLIENT);

        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );

        long clientId = ValidatorUtil.validateId(
                "Select Client ID to remove: ",
                clientList,
                User::getUserId
        );

        boolean deleteClient = adminService.deleteClient(clientId);

        if (deleteClient) {
            log.info("Client Deleted Successfully");
        } else {
            log.info("Error Deleting Client");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }

    /**
     * Deletes a Builder from the system.
     * Retrieves all Builders, displays their details, and asks for a valid ID.
     * Uses AdminService to perform the deletion and logs the result.
     * Finally, shows the admin dashboard.
     */
    public void deleteBuilder() {
        List<User> builderList = CommonRepository.getAllUsers(UserRole.BUILDER);

        builderList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );

        long builderId = ValidatorUtil.validateId(
                "Select Builder ID to remove: ",
                builderList,
                User::getUserId
        );

        boolean deleteBuilderDetails = adminService.deleteBuilder(builderId);

        if (deleteBuilderDetails) {
            log.info("Builder Deleted Successfully");
        } else {
            log.info("Error Deleting Builder");
        }

        DashboardController.showDashboard(SessionManager.getCurrentUser());
    }
}
