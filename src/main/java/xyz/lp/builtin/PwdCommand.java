package xyz.lp.builtin;

import java.nio.file.Path;

import xyz.lp.Command;
import xyz.lp.Input;
import xyz.lp.Result;

public class PwdCommand implements Command {

    private Input input;
    private String[] args;

    @Override
    public Result execute() {
        System.out.println(Path.of("").toAbsolutePath());
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
