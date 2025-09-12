package builder.portfolio.controller;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.service.implementations.AuthService;
import builder.portfolio.util.InputUtil;

public class AuthController {

    private final AuthService authService = new AuthService();

    public User handleLogin() {
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        User user = authService.login(email, password);

        if (user == null) {
            System.out.println("Login failed. Invalid credentials.");
            return null;
        }

        System.out.println("Login successful! Welcome, " + user.getUserName() + ".");
        return user;
    }

    public User handleRegister() {
        String name = InputUtil.readString("Enter Name: ");
        String email = InputUtil.readString("Enter Email: ");
        String password = InputUtil.readString("Enter Password: ");
        String roleInput = InputUtil.readString("Enter Role (ADMIN/BUILDER/PROJECT_MANAGER/CLIENT): ");
        UserRole role = UserRole.valueOf(roleInput.toUpperCase());

        User registeredUser = authService.register(name, email, password, role);

        if (registeredUser != null) {
            System.out.println("==== User Registered Successfully ====");
            System.out.println("Assigned User ID: " + registeredUser.getUserId());
            DashboardController.showDashboard(registeredUser);
        } else {
            System.out.println("Registration failed. Please try again.");
        }

        return registeredUser;
    }
}