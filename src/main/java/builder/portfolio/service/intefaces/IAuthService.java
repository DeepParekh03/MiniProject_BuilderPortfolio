package builder.portfolio.service.intefaces;

import builder.portfolio.model.User;
import builder.portfolio.model.enums.UserRole;

/**
 * Interface defining authentication operations for the Builder Portfolio Management System.
 * Provides methods for user login and registration.
 */
public interface IAuthService {

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user
     * @return the {@link User} object if authentication is successful, otherwise null
     */
    User login(String email, String password);

    /**
     * Registers a new user in the system.
     *
     * @param name     the name of the new user
     * @param email    the email of the new user
     * @param password the password for the new user
     * @param role     the role of the new user (e.g., ADMIN, BUILDER, CLIENT)
     * @return the newly created {@link User} object if registration is successful, otherwise null
     */
    User register(String name, String email, String password, UserRole role);
}
