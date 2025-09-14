package builder.portfolio.controller;

import builder.portfolio.model.AuditTrail;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.service.implementations.AuthService;
import builder.portfolio.util.FileWriterUtil;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;
import builder.portfolio.util.ValidatorUtil;

public class AuthController {

    private final AuthService authService = new AuthService();
    AuditTrail auditTrail=new AuditTrail();

    public User handleLogin() {
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        User loggedUser = authService.login(email, password);

        if (loggedUser == null) {
            System.out.println("Login failed. Invalid credentials.");
            return null;
        }

        SessionManager.setCurrentUser(loggedUser);
        auditTrail=new AuditTrail("User Logged In",SessionManager.getCurrentUser());
        FileWriterUtil.writeAuditTrail(auditTrail);

        System.out.println("Login successful! Welcome, " + loggedUser.getUserName() + ".");
        DashboardController.showDashboard(loggedUser);
        return loggedUser;
    }

    public User handleRegister() {
        String name = InputUtil.readString("Enter Name: ");
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        String roleInput="";
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

            auditTrail=new AuditTrail("User Registered",SessionManager.getCurrentUser());
            FileWriterUtil.writeAuditTrail(auditTrail);

            System.out.println("==== User Registered Successfully ====");
            System.out.println("Assigned User ID: " + registeredUser.getUserId());
            DashboardController.showDashboard(registeredUser);
        } else {
            System.out.println("Registration failed. Please try again.");
        }

        return registeredUser;
    }
}