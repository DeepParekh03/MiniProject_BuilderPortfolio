package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.service.implementations.AuthService;
import builder.portfolio.userinterface.MainMenu;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller class responsible for handling user authentication operations,
 * including login and registration processes. Also handles creating audit
 * trail entries for these actions.
 */
@Slf4j
public class AuthController {

    private final AuthService authService = new AuthService();
    private AuditTrail auditTrail = new AuditTrail();

    /**
     * Handles user login.
     * Prompts the user for email and password, validates credentials using AuthService,
     * and sets the current user in SessionManager upon successful login.
     * Also creates an audit trail entry for successful login.
     * If login fails, logs a message and redirects to the Main Menu.
     *
     * @return the logged-in User object if successful, otherwise null
     */
    public User handleLogin() {
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        User loggedUser = authService.login(email, password);

        if (loggedUser == null) {
            log.info("Login failed. Invalid credentials.");
            MainMenu.show();
            return null;
        } else {
            SessionManager.setCurrentUser(loggedUser);

            auditTrail = new AuditTrail("User Logged In", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            log.info("Login successful! Welcome, " + loggedUser.getUserName() + ".");
            DashboardController.showDashboard(loggedUser);
            return loggedUser;
        }
    }

    /**
     * Handles user registration.
     * Prompts the user for name, email, password, and role. Validates the role input.
     * Registers the user using AuthService and sets the current user in SessionManager
     * upon successful registration. Also creates an audit trail entry for registration.
     * If registration fails, logs a message and redirects to the Main Menu.
     *
     * @return the registered User object if successful, otherwise null
     */
    public User handleRegister() {
        String name = InputUtil.readString("Enter Name: ");
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        String roleInput = "";

        do {
            roleInput = InputUtil.readString("Enter Role (ADMIN/BUILDER/PROJECT_MANAGER/CLIENT): ");
            if (!ValidatorUtil.isValidRole(roleInput)) {
                System.out.println("Invalid role. Please enter one of: ADMIN, BUILDER, PROJECT_MANAGER, CLIENT.");
            }
        } while (!ValidatorUtil.isValidRole(roleInput));

        UserRole role = UserRole.valueOf(roleInput.toUpperCase());
        User registeredUser = authService.register(name, email, password, role);

        if (registeredUser != null) {
            SessionManager.setCurrentUser(registeredUser);

            auditTrail = new AuditTrail("User Registered", SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            System.out.println("==== User Registered Successfully ====");
            log.info("Assigned User ID: " + registeredUser.getUserId());
            DashboardController.showDashboard(registeredUser);
        } else {
            log.info("Registration failed. Please try again.");
            MainMenu.show();
        }

        return registeredUser;
    }
}
