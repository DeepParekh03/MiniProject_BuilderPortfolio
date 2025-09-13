package builder.portfolio;

import builder.portfolio.controller.BuilderController;
import builder.portfolio.controller.MainController;

public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController();
       // BuilderController.createProject();
        controller.start();
    }
}