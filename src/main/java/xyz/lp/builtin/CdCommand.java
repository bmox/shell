package xyz.lp.builtin;

import java.io.File;
import java.util.regex.Pattern;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class CdCommand implements Command {

    Input input;
    String arg;

    @Override
    public Result execute() {
        File targetDir = new File("/");
        File inputDir = new File(arg);
        String[] splittedPath;
        if (inputDir.isAbsolute()) {
            splittedPath = inputDir.getAbsolutePath().split(Pattern.quote(File.separator));
        } else {
            splittedPath = new File(Context.getInstance().getCurrentPath() + File.separator + arg).getAbsolutePath().split(Pattern.quote(File.separator));
        }
        for (int i = 1; i < splittedPath.length; i++) {
            String path = splittedPath[i];
            if (".".equals(path)) {
                continue;
            } else if ("..".equals(path)) {
                targetDir = targetDir.getParentFile() == null ? targetDir : targetDir.getParentFile();
            } else {
                targetDir = new File(targetDir.getAbsoluteFile() + File.separator + path);
            }
            if (!targetDir.exists()) {
                System.out.println("cd: no such file or directory: " + getArgs()[0]);
                return Result.success();
            } else if (!targetDir.isDirectory()) {
                System.out.println("cd: not a directory: " + getArgs()[0]);
                return Result.success();
            }
        }
        Context.getInstance().setCurrentPath(targetDir.getAbsolutePath());
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
