package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.util.InputUtil;

import java.util.Scanner;

public class BuilderMenu {

    public static void show(){
        System.out.println("\n--- BUILDER MENU ---");
        System.out.println("1. Add New Project");
        System.out.println("2. Update Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Assign Project Manager");
        System.out.println("5. View Portfolio");
        System.out.println("6. View Gantt Chart");
        String choice = InputUtil.readString("Enter choice: ");
        while(true){
            switch (choice) {
                case "1":
                    BuilderController.createProject();
                    break;
                default:
                    System.out.println("Invalid choice");
            }

        }
    }
}
