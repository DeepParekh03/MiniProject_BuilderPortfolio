package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;

public interface IBuilderService {
    Project createProjectService(String projectName,
                        double plannedBudget,
                        double actualSpend,
                        long manager,
                        long client,
                        int numberOfTasks);

    Document uploadDocumentDetails(long projectId,String documentName,String documentPath);

    Project updateProjectService(long projectId,String pojectName,double plannedBudget);
    boolean deleteProjectService(long projectId);
}
