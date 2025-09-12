package builder.portfolio.controller;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.userinterface.BuilderMenu;

public class DashboardController {

    public static void showDashboard(User user) {
        UserRole role = user.getRole();

        switch (role) {
           // case ADMIN -> new AdminDashboard(user).show();
            case BUILDER -> BuilderMenu.show();
           // case PROJECT_MANAGER -> new ProjectManagerDashboard(user).show();
           // case CLIENT -> new ClientDashboard(user).show();
            default -> System.out.println("Unknown role. Cannot load dashboard.");
        }
    }
}

