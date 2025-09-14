package builder.portfolio.userinterface;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.DashboardController;
import builder.portfolio.controller.ProjectManagerController;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.SessionManager;

public class ProjectManagerMenu {
    public static void show(){
        ProjectManagerController projectManagerController=new ProjectManagerController();
        System.out.println("\n--- PROJECT MANAGER MENU ---");
        System.out.println("1. View Assigned Projects");
        System.out.println("2. Update Project Status");
        System.out.println("3. Update Actual Project Spend");
        System.out.println("4. Upload Project Documents");
        System.out.println("0. LogOut");
        String choice = InputUtil.readString("Enter choice: ");
        while(true){
            switch (choice) {
                case "1":
                    projectManagerController.viewProjects();
                case "2":
                    projectManagerController.updateProjectStatus();
                case "3":
                    projectManagerController.updateProjectActualSpend();
                case "4":
                    projectManagerController.uploadDocuments();
                case "0":
                    SessionManager.setCurrentUser(null);
                    MainMenu.show();
                default:
                    System.out.println("Invalid choice");
                    DashboardController.showDashboard(SessionManager.getCurrentUser());

            }

        }
    }
}
