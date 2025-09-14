package builder.portfolio.service.implementations;

import builder.portfolio.controller.DashboardController;
import builder.portfolio.model.*;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IBuilderService;
import builder.portfolio.util.SessionManager;

import javax.print.Doc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuilderService implements IBuilderService {

    private final BuilderRepository repository = new BuilderRepository();
    private final CommonRepository commonRepository = new CommonRepository();

    @Override
    public Project createProjectService(String projectName,
                               double plannedBudget,
                               double actualSpend,
                               long manager,
                               long client,
                               LocalDate endDate,
                               int numberOfTasks) {

        Project project = new Project();
        project.setProjectName(projectName);
        project.setPlannedBudget(plannedBudget);
        project.setActualSpend(0);
        project.setBuilderId(SessionManager.getCurrentUser().getUserId());
        project.setProjectManagerId(manager);
        project.setClientId(client);
        project.setEndDate(endDate);
        project.setStatus(Status.UPCOMING);

        Project savedProject = repository.createProjectRepository(project);

        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i <= numberOfTasks; i++) {
            Task task = new Task();
            task.setProjectId(savedProject.getProjectId());
            task.setTaskName("Phase " + i);
            task.setStatus("PENDING"); // Default status
            tasks.add(task);
        }


        for (Task task : tasks) {
            repository.saveTask(task);
        }

        return savedProject;
    }

    @Override
    public boolean updateProjectManagerService(long projectId, long projectManagerId) {
        Project project=new Project();
        project.setProjectId(projectId);
        project.setProjectManagerId(projectManagerId);

        boolean updatedProjectManger=repository.updateProjectManagerRepository(project);
        return updatedProjectManger;
    }

    @Override
    public Document uploadDocumentDetails(long projectId, String documentName, String documentPath) {
        Document document=new Document();
        document.setProjectId(projectId);
        document.setDocumentName(documentName);
        document.setUploadedBy(SessionManager.getCurrentUser().getUserName());
        document.setFilePath(documentPath);
        document.setType(documentPath.substring(documentPath.length()-3));

        Document savedDocument=repository.uploadDocumentDB(document);
        return savedDocument;
    }

    @Override
    public void budgetTrack(long projectId) {
        String budgetStatus="";
        budgetStatus= commonRepository.trackBudget(projectId);
        if(budgetStatus!=null){
            System.out.println("Budget Status of Project: "+budgetStatus);
            DashboardController.showDashboard(SessionManager.getCurrentUser());
        }
    }

    @Override
    public void projectStatus(long projectId) {
        List<Task> taskList=CommonRepository.getAllTasks(projectId);
        System.out.println(taskList);
        taskList.forEach(task -> {
            String updatedInfo;
            if (!Objects.equals(task.getStatus(), "COMPLETED")) {
                updatedInfo = "Not worked on";
            } else {
                updatedInfo = "Last Updated At: " + task.getUpdatedAt();
            }
            System.out.println(task.getTaskName() + " | " + task.getStatus() + " | " + "Created At: " + task.getCreatedAt() + " | " + updatedInfo);
            });
        DashboardController.showDashboard(SessionManager.getCurrentUser());

    }

    @Override
    public void getUploadADocs(long projectId) {
        List<Document> documentList=new ArrayList<>();
        documentList=CommonRepository.getAllDocuments(projectId);
        if(documentList.isEmpty()){
            System.out.println("No docs to be shown");
        }else{
            documentList.forEach(document -> {
                System.out.println("Document Name: "+document.getDocumentName()
                        +", Document Type: "+document.getType()
                        +", Document URL: "+document.getFilePath()
                        +", Uploaded By: "+document.getUploadedBy());
            });
            }
        DashboardController.showDashboard(SessionManager.getCurrentUser());


    }


    @Override
    public Project updateProjectService(long projectId, String projectName, double plannedBudget) {

        Project project = new Project();
        project.setProjectName(projectName);
        project.setPlannedBudget(plannedBudget);
        project.setProjectId(projectId);

        Project updateProject = repository.updateProjectRepository(project);
        return updateProject;
    }

    @Override
    public boolean deleteProjectService(long projectId) {
        Project project = new Project();
        project.setProjectId(projectId);

        boolean deleteProject = repository.deleteProjectRepository(project);
        return deleteProject;
    }

    @Override
    public ProjectTimeline getProjectTimeline(long projectId) {
        return repository.getProjectTimeline(projectId);
    }

}

