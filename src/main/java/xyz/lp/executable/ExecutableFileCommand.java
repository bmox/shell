package xyz.lp.executable;

import java.io.File;
import java.util.List;

import xyz.lp.Command;
import xyz.lp.Context;
import xyz.lp.Input;
import xyz.lp.Result;

public class ExecutableFileCommand implements Command {

    private Input input;

    private static final String IFS = "[ |\t|\n]";

    @Override
    public Result execute() {
        try {
            ProcessBuilder pb = new ProcessBuilder(input.getTokens());
            pb.inheritIO();
            if (Context.getInstance().getRedirectStdoutFile() != null) {
                pb.redirectOutput(Context.getInstance().getRedirectStdoutFile());
            }
            if (Context.getInstance().getRedirectStderrFile() != null) {
                pb.redirectError(Context.getInstance().getRedirectStderrFile());
            }
            pb.directory(new File(Context.getInstance().getCurrentPath()));
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            // ignore
        }
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
        this.input = input;
        return this;
    }
    
}
