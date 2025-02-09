package xyz.lp;

public interface Command { 
    Command init(Input input);

    String getCommandName();

    String[] getArgs();

    Result execute();
}
