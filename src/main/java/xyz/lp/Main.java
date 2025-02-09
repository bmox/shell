package xyz.lp;

import java.util.Scanner;

public class Main {
    static Parser parser = new BaseParser();

    public static void main(String[] args) throws Exception {
        while (true) {
            // Uncomment this block to pass the first stage
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (Constants.EXIT_COMMAND.equals(input)) {
                break;
            }
            
            Command command = parser.parse(input);
            ExecuteResult result = command.execute();

            if (ExecuteResultEnum.NOT_FOUND.equals(result.getExecuteResultEnum())) {
                System.out.println(command.getCommand() + ": " + ExecuteResultEnum.NOT_FOUND.getMsg());
            }
        }
    }
}
