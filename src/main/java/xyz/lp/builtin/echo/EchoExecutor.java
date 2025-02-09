package xyz.lp.builtin.echo;

import xyz.lp.Command;
import xyz.lp.ExecuteResult;
import xyz.lp.Executor;

public class EchoExecutor implements Executor {

    @Override
    public ExecuteResult execute(Command command) {
        System.out.println(command.getArg());
        return ExecuteResult.success();
    }

    public static String getName() {
        return "echo";
    }
    
}
