package builder.portfolio.service.implementations;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IClientService;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for Client operations in the Builder Portfolio Management System.
 * Provides methods for viewing owned projects, tracking budget, and retrieving uploaded documents.
 */
public class ClientService implements IClientService {

    public CommonRepository commonRepository = new CommonRepository();

    /**
     * Retrieves a list of projects owned by the currently logged-in client.
     *
     * @param user the User object representing the client
     * @return a list of Project objects owned by the client
     */
    @Override
    public List<Project> viewOwnedProjects(User user) {
        List<Project> projectList = CommonRepository.getAllProjects(SessionManager.getCurrentUser());

        return projectList;
    }

    /**
     * Tracks the budget status of a specific project.
     *
     * @param projectId the ID of the project
     * @return a String indicating whether the project is "IN BUDGET" or "OUT OF BUDGET"
     */
    @Override
    public String trackBudget(long projectId) {
        return commonRepository.trackBudget(projectId);
    }

    /**
     * Retrieves all documents uploaded for a specific project.
     *
     * @param projectId the ID of the project
     * @return a list of Document objects associated with the project
     */
    @Override
    public List<Document> getUploadedDocs(long projectId) {
        List<Document> documentList = new ArrayList<>();
        documentList = CommonRepository.getAllDocuments(projectId);
        return documentList;
    }
}
