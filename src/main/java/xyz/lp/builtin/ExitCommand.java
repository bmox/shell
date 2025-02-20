package xyz.lp.builtin;

import java.util.List;

import xyz.lp.Command;
import xyz.lp.ExecuteResultEnum;
import xyz.lp.Input;
import xyz.lp.Result;

public class ExitCommand implements Command {

    private Input input;

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

    @Override
    public Result execute() {
        return new Result(ExecuteResultEnum.EXIT);
    }

    public static String getName() {
        return "exit";
    }
    
}
