package xyz.lp.builtin;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class PwdCommand implements Command {

    private Input input;
    private String[] args;

    @Override
    public Result execute() {
        System.out.println(Context.getInstance().getCurrentPath());
        return Result.success();
    }

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
        this.args = null;
        return this;
    }

    public static String getName() {
        return "pwd";
    }
    
    
}
