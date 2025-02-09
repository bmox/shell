package xyz.lp.builtin.echo;

import xyz.lp.Input;
import xyz.lp.Result;
import xyz.lp.Command;

public class EchoCommand implements Command {

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
        this.args = new String[]{input.getArg()};
        return this;
    }

    @Override
    public Result execute() {
        if (getArgs().length != 0) {
            System.out.println(getArgs()[0]);
        }
        return Result.success();
    }

    public static String getName() {
        return "echo";
    }
    
}
