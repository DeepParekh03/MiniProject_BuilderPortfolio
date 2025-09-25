package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.ClientController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

/**
 * Provides the user interface menu for Client users in the Builder Portfolio Management System.
 *
 * This class displays the available client operations such as viewing owned projects,
 * checking budget status, viewing uploaded documents, and viewing project timelines.
 * User input is read using {@link InputUtil}.
 *
 * Note: The {@code switch} statements in the {@link #show()} method do not include {@code break} statements
 * because the control flow for each choice is managed within the respective controller methods.
 */
public class ClientMenu {

    /**
     * Displays the client menu and handles user input to perform client-specific operations.
     *
     * Options include: View owned projects, View budget status, View uploaded documents,
     * View project timeline, Log out.
     *
     * The menu will loop indefinitely until the user logs out, at which point the session is cleared
     * and the {@link MainMenu} is shown.
     */
    public static void show() {
        ClientController clientController = new ClientController();
        System.out.println("\n--- CLIENT MENU ---");
        System.out.println("1. View Owned Projects");
        System.out.println("2. View Budget Status");
        System.out.println("3. View Documents");
        System.out.println("4. View Timeline");
        System.out.println("0. LogOut");

        String choice = InputUtil.readString("Enter choice: ");

        while (true) {
            switch (choice) {
                case "1":
                    clientController.viewOwnedProjects();
                case "2":
                    clientController.trackBudget();
                case "3":
                    clientController.getUploadedDocs();
                case "4":
                    clientController.viewTimeLine();
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
