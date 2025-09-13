package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.util.InputUtil;

import java.util.Scanner;

public class BuilderMenu {

    public static void show(){
        BuilderController builderController=new BuilderController();
        System.out.println("\n--- BUILDER MENU ---");
        System.out.println("1. Add New Project");
        System.out.println("2. Update Project");
        System.out.println("3. Delete Project");
        System.out.println("4. Update Project Manager");
        System.out.println("5. Upload Project Documents");
        System.out.println("6. View Portfolio");
        System.out.println("7. View Gantt Chart");
        String choice = InputUtil.readString("Enter choice: ");
        while(true){
            switch (choice) {
                case "1":
                    builderController.createProject();
                case "2":
                    builderController.updateProject();
                case "3":
                    builderController.deleteProject();
                case "4":
                    builderController.updateProjectManager();
                case "5":
                    builderController.uploadDocuments();
                default:
                    System.out.println("Invalid choice");
            }

        }
    }
}
