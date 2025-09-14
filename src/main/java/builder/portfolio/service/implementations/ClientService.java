package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IClientService;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ClientService implements IClientService {
   CommonRepository commonRepository=new CommonRepository();
    @Override
    public List<Project> viewOwnedProjects(User user) {
        List<Project> projectList=new ArrayList<>();
        projectList= CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        return projectList;
    }

    @Override
    public String trackBudget(long projectId){
        String budgetStatus="";
        budgetStatus= commonRepository.trackBudget(projectId);
        return budgetStatus;
    }

    @Override
    public List<Document> getUploadedDocs(long projectId) {
        List<Document> documentList=new ArrayList<>();
        documentList=CommonRepository.getAllDocuments(projectId);
        return documentList;
    }
}
