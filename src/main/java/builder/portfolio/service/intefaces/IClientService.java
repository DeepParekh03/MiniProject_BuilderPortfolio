package builder.portfolio.service.intefaces;

import builder.portfolio.model.Document;
import builder.portfolio.model.Project;
import builder.portfolio.model.User;

import java.util.List;

public interface IClientService {
   List<Project> viewOwnedProjects(User user);

   String trackBudget(long projectId);
   List<Document> getUploadedDocs(long projectId);
}
