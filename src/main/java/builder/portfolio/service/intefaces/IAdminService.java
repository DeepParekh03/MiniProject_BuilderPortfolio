package builder.portfolio.service.intefaces;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import java.util.List;

/**
 * Interface defining administrative operations in the Builder Portfolio Management System.
 * Provides methods for viewing projects and managing users such as deleting Project Managers, Clients, and Builders.
 */
public interface IAdminService {

   /**
    * Retrieves all projects visible to the given user.
    *
    * @param user the user requesting to view projects
    * @return a {@link List} of {@link Project} objects
    */
   List<Project> viewAllProjects(User user);

   /**
    * Deletes a Project Manager from the system based on their user ID.
    *
    * @param userId the ID of the Project Manager to delete
    * @return {@code true} if the deletion was successful, {@code false} otherwise
    */
   boolean deleteProjectManager(long userId);

   /**
    * Deletes a Client from the system based on their user ID.
    *
    * @param userId the ID of the Client to delete
    * @return {@code true} if the deletion was successful, {@code false} otherwise
    */
   boolean deleteClient(long userId);

   /**
    * Deletes a Builder from the system based on their user ID.
    *
    * @param userId the ID of the Builder to delete
    * @return {@code true} if the deletion was successful, {@code false} otherwise
    */
   boolean deleteBuilder(long userId);
}
