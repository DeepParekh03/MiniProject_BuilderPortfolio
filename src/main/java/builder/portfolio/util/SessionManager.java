package builder.portfolio.util;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Manages the current session state for the application.
 *
 * This class holds the currently logged-in {@link User} and the {@link Project}
 * that the user is currently working on. It provides static methods to access
 * and modify the session state, as well as clear it when the user logs out.
 *
 * Example usage:
 * User user = SessionManager.getCurrentUser();
 * SessionManager.setCurrentProject(project);
 * SessionManager.clearSession(); // logs out the user and clears current project
 */
public class SessionManager {

    /** The currently logged-in user. */
    @Getter @Setter
    private static User currentUser;

    /** The project currently being accessed or edited by the user. */
    @Getter @Setter
    private static Project currentProject;

    /**
     * Clears the current session by setting both the logged-in user and
     * the current project to {@code null}.
     *
     * This method should be called when the user logs out to reset the session state.
     */
    public static void clearSession() {
        currentUser = null;
        currentProject = null;
    }
}
