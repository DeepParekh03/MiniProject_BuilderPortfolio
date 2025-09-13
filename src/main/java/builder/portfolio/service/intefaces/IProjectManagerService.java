package builder.portfolio.service.intefaces;

public interface IProjectManagerService {
    int updateProjectStatus(long projectId,int numberOfTasks);
    double updateActualSpend(long projectId,double actualSpend);
}
