package builder.portfolio.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.time.format.ResolverStyle;

public class InputUtil {
    private static final Scanner input = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(input.nextLine().trim());
    }

    public static long readLong(String prompt){
        System.out.println(prompt);
        return Long.parseLong(input.nextLine().trim());
    }


    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String date = input.nextLine().trim();

            try {
                LocalDate finalDate = LocalDate.parse(date, FORMATTER);
                return finalDate;
            } catch (DateTimeParseException e) {
                System.out.println(" Invalid date format. Please use dd-MM-yyyy (e.g. 15-09-2025).");
            }
        }
    }


    public static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(input.nextLine().trim());
    }
}