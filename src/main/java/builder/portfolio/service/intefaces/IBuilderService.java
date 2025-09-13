package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;

public interface IBuilderService {
    Project saveProject(String projectName,
                        double plannedBudget,
                        double actualSpend,
                        long manager,
                        long client,
                        int numberOfTasks);

    Document uploadDocumentDetails(String documentName,String documentPath);
}
