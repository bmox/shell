package xyz.lp.builtin.echo;

import java.util.Objects;

import xyz.lp.Command;
import xyz.lp.ExecuteResult;
import xyz.lp.Executor;
import xyz.lp.ExecutorManager;

public class TypeExecutor implements Executor {

    @Override
    public ExecuteResult execute(Command command) {
        Executor executor = ExecutorManager.getExecutor(command.getArg());
        if (Objects.isNull(executor)) {
            System.out.println(command.getArg() + ": not found");
            return ExecuteResult.success();
        }
        System.out.println(command.getArg() + " is a shell builtin");
        return ExecuteResult.success();
    }

    public static String getName() {
        return "type";
    }

}
