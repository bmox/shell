package xyz.lp.builtin;

import java.util.List;

import xyz.lp.Command;
import xyz.lp.CommandManager;
import xyz.lp.Input;
import xyz.lp.Result;
import xyz.lp.common.CollUtil;

public class TypeCommand implements Command {

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
        if (CollUtil.isEmpty(getArgs())) {
            return Result.success();
        }
        String toTypedCmdName = getArgs().get(0);
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
