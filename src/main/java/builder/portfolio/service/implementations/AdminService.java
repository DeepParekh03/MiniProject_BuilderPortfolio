package builder.portfolio.service.implementations;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.repository.AdminRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IAdminService;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link IAdminService} for administrative operations.
 * Handles viewing all projects and deleting users such as project managers, clients, and builders.
 */
public class AdminService implements IAdminService {

    /** Repository for admin-specific database operations */
    AdminRepository adminRepository = new AdminRepository();

    /**
     * Retrieves all projects visible to the given user.
     * Currently uses the user from {@link SessionManager}.
     *
     * @param user the user requesting the project list
     * @return a list of all projects
     */
    @Override
    public List<Project> viewAllProjects(User user) {
        List<Project> projectList = new ArrayList<>();
        projectList = CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        return projectList;
    }

    /**
     * Deletes a project manager user by their user ID.
     *
     * @param userId the ID of the project manager to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteProjectManager(long userId) {
        boolean deleteStatus = false;
        deleteStatus = adminRepository.deleteUser(userId);
        return deleteStatus;
    }

    /**
     * Deletes a client user by their user ID.
     *
     * @param userId the ID of the client to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteClient(long userId) {
        boolean deleteStatus = false;
        deleteStatus = adminRepository.deleteUser(userId);
        return deleteStatus;
    }

    /**
     * Deletes a builder user by their user ID.
     *
     * @param userId the ID of the builder to delete
     * @return true if deletion was successful, false otherwise
     */
    @Override
    public boolean deleteBuilder(long userId) {
        boolean deleteStatus = false;
        deleteStatus = adminRepository.deleteUser(userId);
        return deleteStatus;
    }

}
