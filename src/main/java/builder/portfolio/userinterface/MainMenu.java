package builder.portfolio.userinterface;

import builder.portfolio.controller.AuthController;
import builder.portfolio.util.DBUtil;
import builder.portfolio.util.InputUtil;

import java.sql.Connection;
import java.util.Scanner;

public class MainMenu {
    private static final AuthController authController = new AuthController();

    public static void show() {

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");

            String choice = InputUtil.readString("Enter choice: ");

            switch (choice) {
                case "1" : authController.handleLogin();
                case "2" : authController.handleRegister();
                case "0" : {
                    System.out.println("Exiting... Goodbye!");
                    System.exit(0);
                }
                default : System.out.println("Invalid choice, try again.");
            }
        }

    }
}

