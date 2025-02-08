import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        while (true) {
            // Uncomment this block to pass the first stage
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (Constants.EXIT_COMMAND.equals(input)) {
                break;
            }

            System.out.println(input + ": " + Constants.NOT_FOUND);
        }
    }
}
