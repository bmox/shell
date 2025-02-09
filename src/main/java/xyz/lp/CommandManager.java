package xyz.lp;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import xyz.lp.builtin.echo.EchoCommand;
import xyz.lp.builtin.echo.ExitCommand;
import xyz.lp.builtin.echo.TypeCommand;

public class CommandManager {
    private static Map<String, Function<Input, Command>> builtins = Map.of(
        EchoCommand.getName(), (input) -> new EchoCommand().init(input),
        TypeCommand.getName(), (input) -> new TypeCommand().init(input),
        ExitCommand.getName(), (input) -> new ExitCommand().init(input)
    );

    private static Map<String, Path> executableFiles = new HashMap<>();
    static {
        String path = System.getenv("PATH");
        if (path != null) {
            String[] dirs = path.split(":");
            for (String dir : dirs) {
                try (DirectoryStream<Path> ds = Files.newDirectoryStream(Path.of(dir))) {
                    ds.forEach((p) -> {
                        File f = p.toFile();
                        if (!f.canExecute()) {
                            return;
                        }
                        Path fileName = p.getFileName();
                        if (Objects.isNull(fileName)) {
                            return;
                        }
                        executableFiles.put(fileName.toString(), p);
                    });
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static Command getCommand(Input input) {
        Function<Input, Command> f = builtins.get(input.getCommandName());
        if (Objects.isNull(f)) {
            return null;
        }
        return f.apply(input);
    }

    public static boolean isBuiltin(String commandName) {
        return builtins.containsKey(commandName);
    }

    public static boolean isExecutableFile(String cmdName) {
        return executableFiles.containsKey(cmdName);
    }

    public static Path getExecutableFile(String cmdName) {
        return executableFiles.get(cmdName);
    }
}
