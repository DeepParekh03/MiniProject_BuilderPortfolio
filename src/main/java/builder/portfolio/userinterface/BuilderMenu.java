package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

/**
 * Provides the user interface menu for Builder users in the Builder Portfolio Management System.
 *
 * This class displays the available builder operations such as creating, updating, and deleting projects,
 * assigning project managers, uploading documents, and viewing project timelines. User input is read via {@link InputUtil}.
 *
 * Note: The {@code switch} statements in the {@link #show()} method do not include {@code break} statements
 * because the control flow for each choice is managed within the respective controller methods.
 */
public class BuilderMenu {

    /**
     * Displays the builder menu and handles user input to perform builder-specific operations.
     *
     * Options include: Add new project, Update project, Delete project, Update project manager,
     * Upload project documents, View portfolio, View Gantt chart / project timeline, Log out.
     *
     * The menu will loop indefinitely until the user logs out, at which point the session is cleared
     * and the {@link MainMenu} is shown.
     */
    public static void show() {
        BuilderController builderController = new BuilderController();
        System.out.println("\n--- BUILDER MENU ---");
        System.out.println("1. Add New Project");
        System.out.println("2. Update Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Update Project Manager");
        System.out.println("5. Upload Project Documents");
        System.out.println("6. View Portfolio");
        System.out.println("7. View Gantt Chart");
        System.out.println("0. LogOut");

        String choice = InputUtil.readString("Enter choice: ");

        while (true) {
            switch (choice) {
                case "1":
                    builderController.createProject();
                case "2":
                    builderController.updateProject();
                case "3":
                    builderController.deleteProject();
                case "4":
                    builderController.updateProjectManager();
                case "5":
                    builderController.uploadDocuments();
                case "6":
                    builderController.viewPortfolio();
                case "7":
                    builderController.viewTimeLine();
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
