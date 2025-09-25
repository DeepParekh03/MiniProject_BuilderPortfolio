package builder.portfolio.controller;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.userinterface.AdminMenu;
import builder.portfolio.userinterface.BuilderMenu;
import builder.portfolio.userinterface.ClientMenu;
import builder.portfolio.userinterface.ProjectManagerMenu;

/**
 * Controller class responsible for displaying the appropriate dashboard
 * based on the role of the currently logged-in user.
 * Delegates control to the corresponding menu interface for each user type.
 */
public class DashboardController {

    /**
     * Displays the dashboard for the specified user.
     * Determines the user's role and invokes the corresponding menu:
     *  ADMIN -> AdminMenu
     *  BUILDER -> BuilderMenu
     *  PROJECT_MANAGER -> ProjectManagerMenu
     *  CLIENT -> ClientMenu
     * If the role is unknown, prints an error message.
     *
     * @param user the currently logged-in user
     */
    public static void showDashboard(User user) {
        UserRole role = user.getRole();

        switch (role) {
            case ADMIN -> AdminMenu.show();
            case BUILDER -> BuilderMenu.show();
            case PROJECT_MANAGER -> ProjectManagerMenu.show();
            case CLIENT -> ClientMenu.show();
            default -> System.out.println("Unknown role. Cannot load dashboard.");
        }
    }
}
