package builder.portfolio.service.implementations;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.AuthRepository;
import builder.portfolio.service.intefaces.IAuthService;
import builder.portfolio.util.ValidatorUtil;

public class AuthService implements IAuthService {

    private final AuthRepository authRepository = new AuthRepository();

    @Override
    public User login(String email, String password) {
        if (!ValidatorUtil.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return null;
        }
        if (!ValidatorUtil.isValidPassword(password)) {
            System.out.println("Invalid password format.");
            return null;
        }
        return authRepository.login(email, password);
    }

    @Override
    public User register(String name, String email, String password, UserRole role) {
        boolean valid = true;

        if (!ValidatorUtil.isValidName(name)) {
            System.out.println("Invalid name. Name should only contain alphabets and be at least 3 characters long.");
            valid = false;
        }

        if (!ValidatorUtil.isValidEmail(email)) {
            System.out.println("Invalid email format. Example: user@example.com");
            valid = false;
        }

        if (!ValidatorUtil.isValidPassword(password)) {
            System.out.println("Invalid password. It must have at least 6 characters.");
            valid = false;
        }

        if (!valid) {
            return null;
        }

        User user = new User();
        user.setUserName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        return authRepository.register(user);
    }
}