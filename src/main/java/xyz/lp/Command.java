package xyz.lp;

import java.util.List;

public interface Command { 
    Command init(Input input);

    String getCommandName();

    List<String> getArgs();

    Result execute();
}
