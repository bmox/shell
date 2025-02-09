package xyz.lp.builtin;

import xyz.lp.Input;
import xyz.lp.Result;
import xyz.lp.ExecuteResultEnum;
import xyz.lp.Command;

public class ExitCommand implements Command {

    private Input input;

    private String[] args;

    @Override
    public String[] getArgs() {
        return args;
    }

    @Override
    public String getCommandName() {
        return input.getCommandName();
    }

    @Override
    public Command init(Input input) {
        if (!getName().equals(input.getCommandName())) {
            throw new IllegalArgumentException();
        }
        this.input = input;
        args = new String[]{input.getArg()};
        return this;
    }

    @Override
    public Result execute() {
        return new Result(ExecuteResultEnum.EXIT);
    }

    public static String getName() {
        return "exit";
    }
    
}
