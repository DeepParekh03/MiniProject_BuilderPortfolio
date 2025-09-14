package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.Task;
import builder.portfolio.model.User;
import builder.portfolio.model.enums.Status;
import builder.portfolio.repository.BuilderRepository;
import builder.portfolio.service.intefaces.IBuilderService;
import builder.portfolio.util.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BuilderService implements IBuilderService {

    private final BuilderRepository repository = new BuilderRepository();

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
        document.setUploadedBy(SessionManager.getCurrentUser());
        document.setFilePath(documentPath);
        document.setType(documentPath.substring(documentPath.length()-3));

        Document savedDocument=repository.uploadDocumentDB(document);
        return savedDocument;
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

    // Other methods like getAvailableManagers(), getAvailableClients() can stay here
}

