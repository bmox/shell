package xyz.lp;

import java.util.Map;

public class BaseParser implements Parser {

    private enum StateEnum {
        START,
        COMMAND,
        SEPARATOR,
        ARG;
    }
    private enum CharTypeEnum {
        WHITESPACE,
        WORD;
    }
    /**
     * |           | whitespace | word      |
     * | --------- | ---------- | --------- |
     * | START     | START      | COMMAND   |
     * | COMMAND   | SEPARATOR  | COMMAND   |
     * | SEPARATOR | SEPARATOR  | ARG       |
     * | ARG       | ARG        | ARG       |
     */
    private static final Map<StateEnum, Map<CharTypeEnum, StateEnum>> table = Map.of(
        StateEnum.START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.START,
            CharTypeEnum.WORD, StateEnum.COMMAND
        ),
        StateEnum.COMMAND, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.SEPARATOR,
            CharTypeEnum.WORD, StateEnum.COMMAND
        ),
        StateEnum.SEPARATOR, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.SEPARATOR,
            CharTypeEnum.WORD, StateEnum.ARG
        ),
        StateEnum.ARG, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.ARG,
            CharTypeEnum.WORD, StateEnum.ARG
        )
    );

    @Override
    public Input parse(String input) {
        Input command = new Input();
        
        StringBuilder commandStringBuilder = new StringBuilder();
        StringBuilder argStringBuilder = new StringBuilder();
        StateEnum state = StateEnum.START;
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            state = table.get(state).get(getCharType(ch));
            if (StateEnum.COMMAND.equals(state)) {
                commandStringBuilder.append(ch);
            }
            if (StateEnum.ARG.equals(state)) {
                argStringBuilder.append(ch);
            }
        }

        command.setCommandName(commandStringBuilder.toString());
        command.setArg(argStringBuilder.toString());

        return command;
    }

    private CharTypeEnum getCharType(char ch) {
        return Character.isWhitespace(ch) ? CharTypeEnum.WHITESPACE : CharTypeEnum.WORD;
    }
  
}
