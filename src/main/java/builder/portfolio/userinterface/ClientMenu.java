package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.ClientController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

public class ClientMenu {
    public static void show(){
        ClientController clientController=new ClientController();
        System.out.println("\n--- CLIENT MENU ---");
        System.out.println("1. View Owned Projects");
        System.out.println("2. View Budget");
        System.out.println("3. View Documents");
        System.out.println("4. View Budget");
        System.out.println("0. LogOut");
        String choice = InputUtil.readString("Enter choice: ");
        while(true){
            switch (choice) {
                case "1":
                    clientController.viewOwnedProjects();
                case "2":
                    clientController.trackBudget();
                case "3":
                    clientController.getUploadedDocs();
//                case "4":
//                    clientController.viewProjectTimeline();
                case "0":
                    SessionManager.setCurrentUser(null);
                    MainMenu.show();
                default:
                    System.out.println("Invalid choice");
            }

        }
    }
}

