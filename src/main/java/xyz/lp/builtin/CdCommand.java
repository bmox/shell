package xyz.lp.builtin;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class CdCommand implements Command {

    Input input;

    @Override
    public Result execute() {
        String arg = input.getArgs().get(0);
        if ("~".equals(arg)) {
            Context.getInstance().setCurrentPath(System.getenv("HOME"));
            return Result.success();
        }
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
                System.out.println("cd: no such file or directory: " + arg);
                return Result.success();
            } else if (!targetDir.isDirectory()) {
                System.out.println("cd: not a directory: " + arg);
                return Result.success();
            }
        }
        Context.getInstance().setCurrentPath(targetDir.getAbsolutePath());
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
        return "cd";
    }
    
}
