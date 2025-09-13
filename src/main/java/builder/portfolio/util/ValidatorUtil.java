package builder.portfolio.util;

import java.util.List;
import java.util.function.Function;

public class ValidatorUtil {
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return name != null && name.matches("^[A-Za-z ]+$");

    }

    public static boolean isValidDocumentPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }
        path = path.toLowerCase();
        return path.endsWith(".pdf") || path.endsWith(".png") || path.endsWith(".jpg");
    }


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