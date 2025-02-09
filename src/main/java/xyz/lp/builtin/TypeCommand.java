package xyz.lp.builtin;

import java.util.Objects;

import xyz.lp.Input;
import xyz.lp.Result;
import xyz.lp.Command;
import xyz.lp.CommandManager;

public class TypeCommand implements Command {

    private Input input;

    @Override
    public String[] getArgs() {
        return new String[]{input.getArg()};
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
        if (getArgs() == null || getArgs().length == 0) {
            return Result.success();
        }
        String toTypedCmdName = getArgs()[0];
        boolean isBuiltin = CommandManager.isBuiltin(toTypedCmdName);
        if (isBuiltin) {
            System.out.println(toTypedCmdName + " is a shell builtin");
        } else if (CommandManager.isExecutableFile(toTypedCmdName)) {
            System.out.println(toTypedCmdName + " is " + CommandManager.getExecutableFile(toTypedCmdName));
        } else {
            System.out.println(toTypedCmdName + ": not found");
        }
        return Result.success();
    }

    public static String getName() {
        return "type";
    }

}
