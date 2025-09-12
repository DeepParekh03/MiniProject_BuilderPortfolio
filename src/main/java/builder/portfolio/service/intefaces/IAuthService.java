package builder.portfolio.service.intefaces;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;


public interface IAuthService {
    User login(String email, String password);
    User register(String name, String email, String password, UserRole role);
}
