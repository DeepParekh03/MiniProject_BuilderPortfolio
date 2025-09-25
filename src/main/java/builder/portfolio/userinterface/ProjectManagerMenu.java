package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.controller.ProjectManagerController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

/**
 * Provides the menu interface for users with the Project Manager role.
 *
 * This menu allows Project Managers to perform actions related to projects assigned to them:
 * view assigned projects, update project status, update actual project spend,
 * upload project documents, or logout.
 *
 * User input is captured using {@link InputUtil}, and the respective controller
 * methods from {@link ProjectManagerController} are invoked to handle the actions.
 *
 * Note: The {@code switch} statements in the {@link #show()} method do not include
 * {@code break} statements because the flow of control is handled within the controller methods.
 */
public class ProjectManagerMenu {

    /**
     * Displays the Project Manager menu and handles user input.
     *
     * This method loops indefinitely until the user chooses to log out.
     * Based on the user's choice, it calls {@link ProjectManagerController#viewProjects()},
     * {@link ProjectManagerController#updateProjectStatus()},
     * {@link ProjectManagerController#updateProjectActualSpend()},
     * {@link ProjectManagerController#uploadDocuments()}, or {@link MainMenu#show()} to logout.
     * If an invalid option is selected, a message is displayed and the
     * {@link DashboardController} may be shown depending on the session state.
     */
    public static void show() {
        ProjectManagerController projectManagerController = new ProjectManagerController();
        System.out.println("\n--- PROJECT MANAGER MENU ---");
        System.out.println("1. View Assigned Projects");
        System.out.println("2. Update Project Status");
        System.out.println("3. Update Actual Project Spend");
        System.out.println("4. Upload Project Documents");
        System.out.println("0. LogOut");

        String choice = InputUtil.readString("Enter choice: ");

        while (true) {
            switch (choice) {
                case "1":
                    projectManagerController.viewProjects();
                case "2":
                    projectManagerController.updateProjectStatus();
                case "3":
                    projectManagerController.updateProjectActualSpend();
                case "4":
                    projectManagerController.uploadDocuments();
                case "0":
                    SessionManager.setCurrentUser(null);
                    MainMenu.show();
                default:
                    System.out.println("Invalid choice");
                    DashboardController.showDashboard(SessionManager.getCurrentUser());
            }
        }
    }
}
