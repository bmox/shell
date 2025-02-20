package xyz.lp;

import java.util.List;

public class Input {

    private String rawInput;

    private List<String> tokens;

    public String getCommandName() {
        return (tokens == null || tokens.isEmpty()) ? "" : tokens.get(0);
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    public String getRawInput() {
        return rawInput;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getTokens() {
        return this.tokens;
    }

    public List<String> getArgs() {
        return (tokens == null || tokens.size() < 2) ? List.of() : tokens.subList(1, tokens.size());
    }

    @Override
    public String toString() {
        return String.join(" ", tokens);
    }

}
