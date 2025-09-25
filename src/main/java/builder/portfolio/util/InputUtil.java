package builder.portfolio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.time.format.ResolverStyle;

/**
 * Utility class for handling console input in a standardized way.
 *
 * This class provides static methods to read various data types from the console,
 * including {@link String}, {@link int}, {@link long}, {@link double}, and {@link LocalDate}.
 * All input is trimmed automatically to remove leading and trailing whitespace.
 *
 * The {@link #readDate(String)} method expects the date in the format {@code dd-MM-yyyy}.
 * If the input does not match the expected format, the user is prompted to re-enter the date.
 *
 * Example usage:
 * String name = InputUtil.readString("Enter your name: ");
 * int age = InputUtil.readInt("Enter your age: ");
 * LocalDate dob = InputUtil.readDate("Enter your date of birth (dd-MM-yyyy): ");
 */
public class InputUtil {

    /** Scanner instance used for reading console input. */
    private static final Scanner input = new Scanner(System.in);

    /**
     * Reads a string from the console after displaying a prompt.
     *
     * @param prompt the message displayed to the user
     * @return the entered string, trimmed of leading and trailing whitespace
     */
    public static String readString(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    /**
     * Reads an integer from the console after displaying a prompt.
     *
     * @param prompt the message displayed to the user
     * @return the integer value entered by the user
     * @throws NumberFormatException if the input cannot be parsed as an integer
     */
    public static int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(input.nextLine().trim());
    }

    /**
     * Reads a long integer from the console after displaying a prompt.
     *
     * @param prompt the message displayed to the user
     * @return the long value entered by the user
     * @throws NumberFormatException if the input cannot be parsed as a long
     */
    public static long readLong(String prompt){
        System.out.println(prompt);
        return Long.parseLong(input.nextLine().trim());
    }

    /** Formatter used for parsing dates in strict "dd-MM-yyyy" format. */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Reads a {@link LocalDate} from the console in the format {@code dd-MM-yyyy}.
     * If the input is invalid, the user is repeatedly prompted until a valid date is entered.
     *
     * @param prompt the message displayed to the user
     * @return the {@link LocalDate} entered by the user
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String date = input.nextLine().trim();

            try {
                LocalDate finalDate = LocalDate.parse(date, FORMATTER);
                return finalDate;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd-MM-yyyy (e.g. 15-09-2025).");
            }
        }
    }

    /**
     * Reads a double from the console after displaying a prompt.
     *
     * @param prompt the message displayed to the user
     * @return the double value entered by the user
     * @throws NumberFormatException if the input cannot be parsed as a double
     */
    public static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(input.nextLine().trim());
    }
}
