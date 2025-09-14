package builder.portfolio.controller;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.implementations.BuilderService;
import builder.portfolio.service.implementations.ClientService;
import builder.portfolio.util.SessionManager;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    ClientService clientService=new ClientService();
    CommonRepository commonRepository=new CommonRepository();
    public void viewOwnedProjects(){
        List<Project> projectList=new ArrayList<>();
        projectList=clientService.viewOwnedProjects(SessionManager.getCurrentUser());
        if(projectList.isEmpty()){
            System.out.println("Unable to Load Projects");
        }else{

            projectList.forEach(project ->
                    System.out.println("Project ID: " + project.getProjectId()
                            + ", Project Name: " + project.getProjectName()
                            + ", Project Status: " + project.getStatus())
            );

            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

//    public void viewUploadedDocs(){
//        List<Document> documentList;
//        documentList
//    }

    public void trackBudget(){
        String budgetStatus="";
        long projectId= commonRepository.availableProjects();
        budgetStatus= clientService.trackBudget(projectId);
        if(budgetStatus!=null){
            System.out.println("Budget Status of Project: "+budgetStatus);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    public void getUploadedDocs(){
        long projectId=commonRepository.availableProjects();
        List<Document> documentList=new ArrayList<>();
        documentList=clientService.getUploadedDocs(projectId);
        if(documentList==null){
            System.out.println("No docs to be shown");

        }else{
            documentList.forEach(document -> {
                System.out.println("Document Name: "+document.getDocumentName()
                +"Document Type: "+document.getType()
                +"Document URL: "+document.getFilePath()
                +"Uploaded By: "+document.getUploadedBy());
            });
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }
}
