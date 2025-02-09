package xyz.lp.builtin.echo;

import xyz.lp.Command;
import xyz.lp.ExecuteResult;
import xyz.lp.ExecuteResultEnum;
import xyz.lp.Executor;

public class ExitExecutor implements Executor {

    @Override
    public ExecuteResult execute(Command command) {
        return new ExecuteResult(ExecuteResultEnum.EXIT);
    }

    public static String getName() {
        return "exit";
    }
    
}
