package builder.portfolio.service.intefaces;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;

public interface IBuilderService {
    Project saveProject(String projectName,
                        double plannedBudget,
                        double actualSpend,
                        long builder,
                        long manager,
                        long client,
                        int numberOfTasks);
}
