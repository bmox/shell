package xyz.lp;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Parser parser = new BaseParser();

    public static void main(String[] args) throws Exception {
        while (true) {
            // Uncomment this block to pass the first stage
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String inputStr = scanner.nextLine();
            
            Input input = parser.parse(inputStr);
            if (input.getCommandName() == null || input.getCommandName().isEmpty()) {
                continue;
            }
            Command command = CommandManager.getCommand(input);
            if (Objects.isNull(command)) {
                System.out.println(input.getCommandName() + ": " + Constants.NOT_FOUND);
                continue;
            }
            Result res = command.execute();

            if (ExecuteResultEnum.EXIT.equals(res.getExecuteResultEnum())) {
                break;
            }
        }
    }
}
