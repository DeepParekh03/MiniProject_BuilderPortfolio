package builder.portfolio.util;

public class ValidatorUtil {
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static boolean isValidName(String name) {
        return name.matches("^[A-Z]+('?([a-zA-Z]*)?)$");
    }
}