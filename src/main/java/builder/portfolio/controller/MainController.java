package builder.portfolio.controller;

import builder.portfolio.userinterface.MainMenu;

/**
 * Controller class responsible for starting the application.
 * Displays the main welcome message and delegates control to the MainMenu.
 */
public class MainController {

    /**
     * Starts the Builder Portfolio Management System.
     * Prints the welcome banner and invokes the {@link MainMenu} to handle
     * user interactions from the main menu.
     */
    public void start() {
        System.out.println("=== Builder Portfolio Management System ===");
        MainMenu.show();
    }
}
