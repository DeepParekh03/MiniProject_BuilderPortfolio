package builder.portfolio.controller;

import builder.portfolio.constants.GetData;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.util.InputUtil;
import builder.portfolio.util.ValidatorUtil;

import java.util.List;

public class BuilderController {
   private static final BuilderService builderService = new BuilderService();

    public static void createProject(){
        String projectName= InputUtil.readString("Enter Project Name: ");
        double plannedBudget= InputUtil.readDouble("Enter Estimate budget: ");
        long builderId=InputUtil.readLong("Enter builder ID");
        System.out.println("Available Managers: ");
        List<User> managerList= GetData.getAllData(String.valueOf(UserRole.PROJECT_MANAGER));
        managerList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long managerId = ValidatorUtil.validateId(
                "Select manager ID: ",
                managerList,
                User::getUserId
        );
        System.out.println("Available Clients: ");
        List<User> clientList= GetData.getAllData(String.valueOf(UserRole.CLIENT));
        clientList.forEach(user ->
                System.out.println("ID: " + user.getUserId() + ", Name: " + user.getUserName())
        );
        long clientId = ValidatorUtil.validateId(
                "Select client ID: ",
                clientList,
                User::getUserId
        );
        int numberOfTasks=InputUtil.readInt("Enter total no. of phases");

        builderService.saveProject(projectName,plannedBudget,0,builderId,managerId,clientId,numberOfTasks);

//        return
    }

}
