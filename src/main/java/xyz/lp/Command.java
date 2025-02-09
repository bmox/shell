package xyz.lp;

import java.util.Objects;

public class Command {

    private String command;

    private String arg;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public ExecuteResult execute() {
        if (command == null || command.isEmpty()) {
            return ExecuteResult.success();
        }
        Executor executor = ExecutorFactory.getExecutor(command);
        if (Objects.isNull(executor)) {
            return new ExecuteResult(ExecuteResultEnum.NOT_FOUND);
        }
        return executor.execute(this);
    }

    @Override
    public String toString() {
        return "Command [command=" + command + ", arg=" + arg + "]";
    }

}
