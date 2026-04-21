import java.util.Scanner;

public class SafeInput {

    public static int getRangedInt(Scanner console, String prompt, int low, int high) {
        int value = 0;
        boolean valid;

        do {
            System.out.print(prompt + " [" + low + "-" + high + "]: ");
            valid = console.hasNextInt();

            if (valid) {
                value = console.nextInt();
                console.nextLine();

                if (value < low || value > high) {
                    System.out.println("Error: number not in range.");
                    valid = false;
                }
            } else {
                System.out.println("Error: invalid integer.");
                console.nextLine();
            }

        } while (!valid);

        return value;
    }

    public static boolean getYNConfirm(Scanner console, String prompt) {
        String response;

        do {
            System.out.print(prompt + " [Y/N]: ");
            response = console.nextLine().toUpperCase();

            if (!response.equals("Y") && !response.equals("N")) {
                System.out.println("Error: please enter Y or N.");
            }

        } while (!response.equals("Y") && !response.equals("N"));

        return response.equals("Y");
    }

    public static String getRegExString(Scanner console, String prompt, String regEx) {
        String response;

        do {
            System.out.print(prompt + ": ");
            response = console.nextLine();

            if (!response.matches(regEx)) {
                System.out.println("Error: input does not match required pattern.");
            }

        } while (!response.matches(regEx));

        return response;
    }
}