package builder.portfolio.util;

import java.util.Scanner;

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

    public static float readFloat(String prompt){
        System.out.println(prompt);
        return Float.parseFloat(input.next().trim());
    }

    public static double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(input.nextLine().trim());
    }
}