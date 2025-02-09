package xyz.lp;

import java.util.Objects;

public class Input {

    private String commandName;

    private String arg;

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String command) {
        this.commandName = command;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    @Override
    public String toString() {
        return "Input [commandName=" + commandName + ", arg=" + arg + "]";
    }

}
