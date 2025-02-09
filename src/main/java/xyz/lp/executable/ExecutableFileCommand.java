package xyz.lp.executable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import xyz.lp.Command;
import xyz.lp.Input;
import xyz.lp.Result;

public class ExecutableFileCommand implements Command {

    private Input input;
    private String[] args;

    private static final String IFS = "[ |\t|\n]";

    @Override
    public Result execute() {
        try {
            ProcessBuilder pb = new ProcessBuilder(input.toString().split(IFS));
            pb.inheritIO();
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // ignore
        }
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
        this.input = input;
        this.args = input.getArg().split(" ");
        return this;
    }
    
}
