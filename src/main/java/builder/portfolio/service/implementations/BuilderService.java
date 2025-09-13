package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.service.intefaces.IBuilderService;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BuilderService implements IBuilderService {

    private final BuilderRepository repository = new BuilderRepository();

    @Override
    public Project saveProject(String projectName,
                               double plannedBudget,
                               double actualSpend,
                               long manager,
                               long client,
                               int numberOfTasks) {

        Project project = new Project();
        project.setProjectName(projectName);
        project.setPlannedBudget(plannedBudget);
        project.setActualSpend(0);
        project.setBuilderId(SessionManager.getCurrentUser().getUserId());
        project.setProjectManagerId(manager);
        project.setClientId(client);
        project.setStatus(Status.UPCOMING);

        Project savedProject = repository.saveProject(project);

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
    public Document uploadDocumentDetails(String documentName, String documentPath) {
        Document document=new Document();
        document.setProjectId(SessionManager.getCurrentProject().getProjectId());
        document.setDocumentName(documentName);
        document.setUploadedBy(SessionManager.getCurrentUser());
        document.setFilePath(documentPath);
        document.setType(documentPath.substring(documentPath.length()-2));

        Document savedDocument=repository.uploadDocumentDB(document);
        return document;
    }

    // Other methods like getAvailableManagers(), getAvailableClients() can stay here
}

