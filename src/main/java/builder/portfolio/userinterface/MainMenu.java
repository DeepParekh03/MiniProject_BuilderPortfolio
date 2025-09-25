package builder.portfolio.userinterface;

import builder.portfolio.controller.AuthController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

/**
 * Provides the main entry-point menu for the Builder Portfolio Management System.
 *
 * This menu is displayed when the application starts and allows users to:
 * login to their account, register a new account, or exit the application.
 *
 * User input is handled using {@link InputUtil}, and authentication actions are delegated
 * to {@link AuthController}.
 *
 * Note: The {@code switch} statements in the {@link #show()} method do not include {@code break} statements
 * because the flow is controlled within the respective controller methods.
 */
public class MainMenu {

    private static final AuthController authController = new AuthController();

    /**
     * Displays the main menu and handles user input.
     *
     * The menu will loop indefinitely until the user chooses to exit the application.
     * Based on the choice, it will call {@link AuthController#handleLogin()} for login,
     * {@link AuthController#handleRegister()} for registration, or {@link System#exit(int)} for exiting.
     * If an invalid choice is entered, the user is prompted again and the {@link DashboardController}
     * may be displayed depending on session state.
     */
    public static void show() {

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            String choice = InputUtil.readString("Enter choice: ");

            switch (choice) {
                case "1": authController.handleLogin();
                case "2": authController.handleRegister();
                case "0": {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }
                default:
                    System.out.println("Invalid choice, try again.");
                    DashboardController.showDashboard(SessionManager.getCurrentUser());
            }
        }

    }
}
