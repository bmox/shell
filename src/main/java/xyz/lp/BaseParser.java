package xyz.lp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseParser implements Parser {

    private enum StateEnum {
        START,
        TOKEN,
        SINGLE_QUOTE_START,
        IN_SINGLE_QUOTE,
        SINGLE_QUOTE_END,
        TOKEN_END;
    }
    private enum CharTypeEnum {
        WHITESPACE,
        NORMAL_CHAR,
        SIGNAL_QUOTE;
    }
    /**
     * |                    | WHITESPACE      | NORMAL_CHAR     | SIGNAL_QUOTE       |
     * | ------------------ | --------------- | --------------- | ------------------ |
     * | START              | START           | TOKEN           | SINGLE_QUOTE_START |
     * | TOKEN              | TOKEN_END       | TOKEN           | SINGLE_QUOTE_START |
     * | SINGLE_QUOTE_START | IN_SINGLE_QUOTE | IN_SINGLE_QUOTE | SINGLE_QUOTE_END   |
     * | IN_SINGLE_QUOTE    | IN_SINGLE_QUOTE | IN_SINGLE_QUOTE | SINGLE_QUOTE_END   |
     * | SINGLE_QUOTE_END   | TOKEN_END       | TOKEN           | SINGLE_QUOTE_START |
     * | TOKEN_END          | START           | TOKEN           | SINGLE_QUOTE_START |
     */
    private static final Map<StateEnum, Map<CharTypeEnum, StateEnum>> table = Map.of(
        StateEnum.START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.START,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START
        ),
        StateEnum.TOKEN, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START
        ),
        StateEnum.SINGLE_QUOTE_START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_END
        ),
        StateEnum.IN_SINGLE_QUOTE, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_END
        ),
        StateEnum.SINGLE_QUOTE_END, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START
        ),
        StateEnum.TOKEN_END, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.START,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START
        )
    );

    @Override
    public Input parse(String in) {
        List<String> tokens = new ArrayList<>();
        StringBuilder tokenBuilder = new StringBuilder();
        StateEnum state = StateEnum.START;
        for (int i = 0; i < in.length(); i++) {
            char ch = in.charAt(i);
            state = table.get(state).get(getCharType(ch));
            switch (state) {
                case TOKEN:
                case IN_SINGLE_QUOTE:
                    tokenBuilder.append(ch);
                    break;
                case TOKEN_END:
                    tokens.add(tokenBuilder.toString());
                    tokenBuilder = new StringBuilder();
                default:
                    break;
            }
        }

        if (!tokenBuilder.isEmpty()) {
            switch (state) {
                case TOKEN:
                case TOKEN_END:
                case SINGLE_QUOTE_END:
                    tokens.add(tokenBuilder.toString());
                    break;
                default:
                    break;
            }
        }

        Input input = new Input();
        input.setRawInput(in);
        input.setTokens(tokens);

        return input;
    }

    private CharTypeEnum getCharType(char ch) {
        if (Character.isWhitespace(ch)) {
            return CharTypeEnum.WHITESPACE;
        } else if ('\'' == ch) {
            return CharTypeEnum.SIGNAL_QUOTE;
        } else {
            return CharTypeEnum.NORMAL_CHAR;
        }
    }
  
}
