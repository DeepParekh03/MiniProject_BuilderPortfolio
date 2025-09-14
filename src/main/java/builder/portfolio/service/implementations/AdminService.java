package builder.portfolio.service.implementations;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import builder.portfolio.repository.AdminRepository;
import builder.portfolio.repository.CommonRepository;
import builder.portfolio.service.intefaces.IAdminService;
import builder.portfolio.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AdminService implements IAdminService {
    AdminRepository adminRepository=new AdminRepository();
    @Override
    public List<Project> viewAllProjects(User user) {
        List<Project> projectList=new ArrayList<>();
        projectList= CommonRepository.getAllProjects(SessionManager.getCurrentUser());
        return projectList;
    }

    @Override
    public boolean deleteProjectManager(long userId) {
        boolean deleteStatus=false;
        deleteStatus=adminRepository.deleteUser(userId);
        return deleteStatus;
    }

    @Override
    public boolean deleteClient(long userId) {
        boolean deleteStatus=false;
        deleteStatus=adminRepository.deleteUser(userId);
        return deleteStatus;
    }

    @Override
    public boolean deleteBuilder(long userId) {
        boolean deleteStatus=false;
        deleteStatus=adminRepository.deleteUser(userId);
        return deleteStatus;
    }

}


