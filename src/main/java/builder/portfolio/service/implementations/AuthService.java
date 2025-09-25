package builder.portfolio.service.implementations;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;
import builder.portfolio.repository.AuthRepository;
import builder.portfolio.service.intefaces.IAuthService;
import builder.portfolio.util.ValidatorUtil;

/**
 * Service class implementing {@link IAuthService} to handle
 * user authentication and registration operations.
 * Validates user input before interacting with the {@link AuthRepository}.
 */
public class AuthService implements IAuthService {

    /** Repository responsible for authentication-related database operations */
    private final AuthRepository authRepository = new AuthRepository();

    /**
     * Authenticates a user based on email and password.
     * Validates email and password formats before querying the repository.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user
     * @return the authenticated {@link User} if credentials are valid; {@code null} otherwise
     */
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

    /**
     * Registers a new user with the provided details.
     * Validates name, email, and password formats before registration.
     *
     * @param name     the name of the user
     * @param email    the email address of the user
     * @param password the password for the user account
     * @param role     the role assigned to the user ({@link UserRole})
     * @return the registered {@link User} if successful; {@code null} if validation fails or registration fails
     */
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
