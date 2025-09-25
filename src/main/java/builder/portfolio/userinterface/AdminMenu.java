package builder.portfolio.userinterface;

import builder.portfolio.controller.AdminController;
import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

/**
 * Provides the user interface menu for Admin users in the Builder Portfolio Management System.
 * This class displays the available admin operations such as viewing projects, viewing audit trails,
 * and deleting users (Project Managers, Clients, Builders). User input is read via {@link InputUtil}.
 * Note: The {@code switch} statements in the {@link #show()} method do not include {@code break} statements
 * because the control flow for each choice is managed within the respective controller methods.
 */
public class AdminMenu {

    /**
     * Displays the admin menu and handles user input to perform administrative operations.
     * Options include:
     * View all projects
     *  View audit trail
     *  Delete Project Manager
     *  Delete Client
     *  Delete Builder
     *  Log out
     * The menu will loop indefinitely until the user logs out, at which point the session is cleared
     * and the {@link MainMenu} is shown.
     */
    public static void show() {
        AdminController adminController = new AdminController();
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. View All Projects");
        System.out.println("2. View Audit Trail");
        System.out.println("3. Delete Project Manager");
        System.out.println("4. Delete Client");
        System.out.println("5. Delete Builder");
        System.out.println("0. LogOut");

        String choice = InputUtil.readString("Enter choice: ");

        while (true) {
            switch (choice) {
                case "1":
                    adminController.viewAllProjects();
                case "2":
                    adminController.viewAuditTrail();
                case "3":
                    adminController.deleteProjectManager();
                case "4":
                    adminController.deleteClient();
                case "5":
                    adminController.deleteBuilder();
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
