package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;

import java.util.List;

/**
 * Interface defining operations available to a Client in the Builder Portfolio Management System.
 * A Client can view their own projects, track project budgets, and access uploaded documents.
 */
public interface IClientService {

   /**
    * Retrieves all projects owned by the specified user (client).
    *
    * @param user the client whose projects are to be retrieved
    * @return a list of {@link Project} objects associated with the client
    */
   List<Project> viewOwnedProjects(User user);

   /**
    * Tracks the budget status of a specific project.
    *
    * @param projectId the ID of the project to check
    * @return a {@link String} indicating whether the project is "IN BUDGET" or "OUT OF BUDGET"
    */
   String trackBudget(long projectId);

   /**
    * Retrieves all documents uploaded for a specific project.
    *
    * @param projectId the ID of the project for which documents are retrieved
    * @return a list of {@link Document} objects associated with the project
    */
   List<Document> getUploadedDocs(long projectId);
}
