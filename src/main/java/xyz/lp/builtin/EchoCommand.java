package xyz.lp.builtin;

import java.util.List;

import xyz.lp.Command;
import xyz.lp.Input;
import xyz.lp.Result;
import xyz.lp.common.CollUtil;

public class EchoCommand implements Command {

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
        if (!CollUtil.isEmpty(getArgs())) {
            System.out.println(String.join(" ", getArgs()));
        }
        return Result.success();
    }

    public static String getName() {
        return "echo";
    }
    
}
