package xyz.lp;

import java.util.Map;

import xyz.lp.builtin.echo.EchoExecutor;
import xyz.lp.builtin.echo.ExitExecutor;
import xyz.lp.builtin.echo.TypeExecutor;

public class ExecutorManager {
    private static Map<String, Executor> executors = Map.of(
        EchoExecutor.getName(), new EchoExecutor(),
        TypeExecutor.getName(), new TypeExecutor(),
        ExitExecutor.getName(), new ExitExecutor()
    );

    public static Executor getExecutor(String command) {
        return executors.get(command);
    }
}
