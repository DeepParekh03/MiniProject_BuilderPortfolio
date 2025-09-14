package builder.portfolio.service.intefaces;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;

import java.util.List;

public interface IAdminService {

   List<Project> viewAllProjects(User user);
   boolean deleteProjectManager(long userId);
   boolean deleteClient(long userId);
   boolean deleteBuilder(long userId);
}
