package xyz.lp;

import java.util.Map;

import xyz.lp.builtin.echo.EchoExecutor;

public class ExecutorFactory {
    private static Map<String, Executor> executors = Map.of(
        EchoExecutor.getName(), new EchoExecutor() 
    );

    public static Executor getExecutor(String command) {
        return executors.get(command);
    }

}
