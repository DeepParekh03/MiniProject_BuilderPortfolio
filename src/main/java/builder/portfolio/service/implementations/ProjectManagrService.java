package builder.portfolio.service.implementations;

import builder.portfolio.repository.ProjectManagerRepository;
import builder.portfolio.service.intefaces.IProjectManagerService;

public class ProjectManagrService implements IProjectManagerService {
    ProjectManagerRepository projectManagerRepository=new ProjectManagerRepository();
    @Override
    public int updateProjectStatus(long projectId,int numberOfTasks) {
        int totalTaskUpdate=projectManagerRepository.updateProjectStatus(projectId,numberOfTasks);
        return totalTaskUpdate;
    }

    @Override
    public double updateActualSpend(long projectId, double actualSpend) {
        double moneySpend=projectManagerRepository.updateProjectActualSpend(projectId,actualSpend);
        return moneySpend;
    }
}
