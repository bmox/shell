package xyz.lp.builtin;

import java.io.File;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class CdCommand implements Command {

    Input input;
    String arg;

    @Override
    public Result execute() {
        File targetDir = new File(arg);
        if (!targetDir.exists()) {
            System.out.println("cd: no such file or directory: " + getArgs()[0]);
        } else if (!targetDir.isDirectory()) {
            System.out.println("cd: not a directory: " + getArgs()[0]);
        } else {
            Context.getInstance().setCurrentPath(targetDir.getAbsolutePath());
        }
        return Result.success();
    }

    @Override
    public String[] getArgs() {
        return new String[]{arg};
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
        arg = input.getArg();
        return this;
    }

    public static String getName() {
        return "cd";
    }
    
}
