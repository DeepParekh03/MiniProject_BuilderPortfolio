package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;

import java.time.LocalDate;
import java.util.List;

public interface IBuilderService {
    Project createProjectService(String projectName,
                        double plannedBudget,
                        double actualSpend,
                        long manager,
                        long client, LocalDate endDate,
                        int numberOfTasks);

    Document uploadDocumentDetails(long projectId,String documentName,String documentPath);
    void budgetTrack(long projectId);
    void projectStatus(long projectId);
    void getUploadADocs(long projectId);

    Project updateProjectService(long projectId,String pojectName,double plannedBudget);
    boolean updateProjectManagerService(long projectId,long projectManagerId);
    boolean deleteProjectService(long projectId);
}
