package builder.portfolio.util;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for validating various input fields across the application.
 *
 * Provides static methods for validating emails, passwords, names, document paths,
 * user roles, dates, and for validating IDs from a list of objects.
 *
 * Example usage:
 * boolean validEmail = ValidatorUtil.isValidEmail("user@example.com");
 * boolean validPassword = ValidatorUtil.isValidPassword("password123");
 * long selectedId = ValidatorUtil.validateId("Select Project ID: ", projects, Project::getProjectId);
 */
public class ValidatorUtil {

    /**
     * Validates an email string.
     *
     * @param email the email to validate
     * @return {@code true} if the email is non-null and matches a standard email pattern; {@code false} otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    /**
     * Validates a password string.
     *
     * @param password the password to validate
     * @return {@code true} if the password is non-null and at least 6 characters long; {@code false} otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validates a name string.
     *
     * @param name the name to validate
     * @return {@code true} if the name is non-null and contains only alphabetic characters and spaces; {@code false} otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]+$");
    }

    /**
     * Validates a document file path.
     *
     * @param path the file path to validate
     * @return {@code true} if the path is non-null, non-empty, and ends with .pdf, .png, or .jpg; {@code false} otherwise
     */
    public static boolean isValidDocumentPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }
        path = path.toLowerCase();
        return path.endsWith(".pdf") || path.endsWith(".png") || path.endsWith(".jpg");
    }

    /**
     * Validates a user role string.
     *
     * @param role the role to validate
     * @return {@code true} if the role matches one of the predefined roles (ADMIN, CLIENT, BUILDER, PROJECT_MANAGER); {@code false} otherwise
     */
    public static boolean isValidRole(String role){
        return role.equalsIgnoreCase("PROJECT_MANAGER") || role.equalsIgnoreCase("BUILDER")
                || role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("CLIENT");
    }

    /**
     * Validates a date.
     *
     * @param date the date to validate
     * @return {@code true} if the date is non-null and not before the current date; {@code false} otherwise
     */
    public static boolean isValidDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(LocalDate.now());
    }

    /**
     * Prompts the user to select a valid ID from a list of objects.
     *
     * @param <T> the type of objects in the list
     * @param prompt the message to display when asking for input
     * @param items the list of objects containing valid IDs
     * @param idMapper a function to extract the ID from an object
     * @return the ID selected by the user that exists in the list
     */
    public static <T> long validateId(String prompt, List<T> items, Function<T, Long> idMapper) {

        List<Long> validIds = items.stream()
                .map(idMapper)
                .toList();

        long selectedId;
        while (true) {
            selectedId = InputUtil.readLong(prompt);

            if (validIds.contains(selectedId)) {
                break;
            } else {
                System.out.println("Error: No such available ID. Please try again.");
            }
        }
        return selectedId;
    }
}
