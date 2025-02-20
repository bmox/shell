package xyz.lp.builtin;

import java.util.List;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class PwdCommand implements Command {

    private Input input;

    @Override
    public Result execute() {
        System.out.println(Context.getInstance().getCurrentPath());
        return Result.success();
    }

    @Override
    public List<String> getArgs() {
        return input.getArgs();
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
        return this;
    }

    public static String getName() {
        return "pwd";
    }
    
    
}
