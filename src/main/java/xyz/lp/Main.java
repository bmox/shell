package xyz.lp;

import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.Objects;

public class Main {
    static Parser parser = new BaseParser();

    public static void main(String[] args) throws Exception {

        Terminal terminal = TerminalBuilder.builder().build();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(new StringsCompleter(CommandManager.getBuiltins()))
                .build();

        while (true) {
            // Uncomment this block to pass the first stage
            // System.out.print("$ ");

            // Scanner scanner = new Scanner(System.in);
            // String inputStr = scanner.nextLine();

            String inputStr = reader.readLine("$ ");
            
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

            Context.getInstance().clear();
        }
    }

    private static void autoComplete(StringBuilder inputStringBuilder) {
        String prefix = inputStringBuilder.toString();
        for (String command : CommandManager.getBuiltins()) {
            if (command.startsWith(prefix)) {
                // 通过回车符 + 退格符清空当前行
                System.out.print("\r$ " + command + " ");

                inputStringBuilder.setLength(0);
                inputStringBuilder.append(command);
                return ;
            }
        }
    }
}
