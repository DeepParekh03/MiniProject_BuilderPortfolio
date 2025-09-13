package builder.portfolio.util;

import builder.portfolio.model.Project;
import builder.portfolio.model.User;
import lombok.Getter;
import lombok.Setter;

public class SessionManager {
    @Getter @Setter private static User currentUser;
    @Getter @Setter private static Project currentProject;

    public static void clearSession() {
        currentUser = null;
        currentProject=null;
    }




}