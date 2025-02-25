package xyz.lp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseParser implements Parser {

    private enum StateEnum {
        START,
        TOKEN,
        SINGLE_QUOTE_START,
        IN_SINGLE_QUOTE,
        SINGLE_QUOTE_END,
        DOUBLE_QUOTE_START,
        IN_DOUBLE_QUOTE,
        DOUBLE_QUOTE_END,
        BACKSLASH,
        BACKSLASH_CHAR,
        BACKSLASH_IN_DOUBLE_QUOTE,
        BACKSLASH_CHAR_IN_DOUBLE_QUOTE,
        TOKEN_END;
    }
    private enum CharTypeEnum {
        WHITESPACE,
        NORMAL_CHAR,
        SIGNAL_QUOTE,
        DOUBLE_QUOTE,
        BACKSLASH;
    }
    /**
     * |                                | WHITESPACE                     | NORMAL_CHAR                    | SIGNAL_QUOTE                   | DOUBLE_QUOTE                   | BACKSLASH                      |
     * | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ | ------------------------------ |
     * | START                          | START                          | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     * | TOKEN                          | TOKEN_END                      | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     * | SINGLE_QUOTE_START             | IN_SINGLE_QUOTE                | IN_SINGLE_QUOTE                | SINGLE_QUOTE_END               | IN_SINGLE_QUOTE                | IN_SINGLE_QUOTE                |
     * | IN_SINGLE_QUOTE                | IN_SINGLE_QUOTE                | IN_SINGLE_QUOTE                | SINGLE_QUOTE_END               | IN_SINGLE_QUOTE                | IN_SINGLE_QUOTE                |
     * | SINGLE_QUOTE_END               | TOKEN_END                      | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     * | DOUBLE_QUOTE_START             | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | DOUBLE_QUOTE_END               | BACKSLASH_IN_DOUBLE_QUOTE      |
     * | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | DOUBLE_QUOTE_END               | BACKSLASH_IN_DOUBLE_QUOTE      |
     * | DOUBLE_QUOTE_END               | TOKEN_END                      | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     * | BACKSLASH                      | BACKSLASH_CHAR                 | BACKSLASH_CHAR                 | BACKSLASH_CHAR                 | BACKSLASH_CHAR                 | BACKSLASH_CHAR                 |
     * | BACKSLASH_CHAR                 | TOKEN_END                      | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     * | BACKSLASH_IN_DOUBLE_QUOTE      | BACKSLASH_CHAR_IN_DOUBLE_QUOTE | BACKSLASH_CHAR_IN_DOUBLE_QUOTE | BACKSLASH_CHAR_IN_DOUBLE_QUOTE | BACKSLASH_CHAR_IN_DOUBLE_QUOTE | BACKSLASH_CHAR_IN_DOUBLE_QUOTE |
     * | BACKSLASH_CHAR_IN_DOUBLE_QUOTE | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | IN_DOUBLE_QUOTE                | DOUBLE_QUOTE_END               | BACKSLASH_IN_DOUBLE_QUOTE      |
     * | TOKEN_END                      | START                          | TOKEN                          | SINGLE_QUOTE_START             | DOUBLE_QUOTE_START             | BACKSLASH                      |
     */
    private static Map<StateEnum, Map<CharTypeEnum, StateEnum>> table = new HashMap<>();
    static {
        table.put(StateEnum.START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.START,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
        table.put(StateEnum.TOKEN, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
        table.put(StateEnum.SINGLE_QUOTE_START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_END,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.BACKSLASH, StateEnum.IN_SINGLE_QUOTE
        ));
        table.put(StateEnum.IN_SINGLE_QUOTE, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_END,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.IN_SINGLE_QUOTE,
            CharTypeEnum.BACKSLASH, StateEnum.IN_SINGLE_QUOTE
        ));
        table.put(StateEnum.SINGLE_QUOTE_END, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
        table.put(StateEnum.DOUBLE_QUOTE_START, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_END,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH_IN_DOUBLE_QUOTE
        ));
        table.put(StateEnum.IN_DOUBLE_QUOTE, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_END,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH_IN_DOUBLE_QUOTE
        ));
        table.put(StateEnum.DOUBLE_QUOTE_END, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
        table.put(StateEnum.BACKSLASH, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.BACKSLASH_CHAR,
            CharTypeEnum.NORMAL_CHAR, StateEnum.BACKSLASH_CHAR,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.BACKSLASH_CHAR,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.BACKSLASH_CHAR,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH_CHAR
        ));
        table.put(StateEnum.BACKSLASH_CHAR, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.TOKEN_END,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
        table.put(StateEnum.BACKSLASH_IN_DOUBLE_QUOTE, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE
        ));
        table.put(StateEnum.BACKSLASH_CHAR_IN_DOUBLE_QUOTE, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.NORMAL_CHAR, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.IN_DOUBLE_QUOTE,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_END,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH_IN_DOUBLE_QUOTE
        ));
        table.put(StateEnum.TOKEN_END, Map.of(
            CharTypeEnum.WHITESPACE, StateEnum.START,
            CharTypeEnum.NORMAL_CHAR, StateEnum.TOKEN,
            CharTypeEnum.SIGNAL_QUOTE, StateEnum.SINGLE_QUOTE_START,
            CharTypeEnum.DOUBLE_QUOTE, StateEnum.DOUBLE_QUOTE_START,
            CharTypeEnum.BACKSLASH, StateEnum.BACKSLASH
        ));
    }

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
                case IN_DOUBLE_QUOTE:
                case BACKSLASH_CHAR:
                case BACKSLASH_CHAR_IN_DOUBLE_QUOTE:
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
                case DOUBLE_QUOTE_END:
                case BACKSLASH_CHAR:
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
        } else if ('\"' == ch) {
            return CharTypeEnum.DOUBLE_QUOTE;
        } else if ('\\' == ch) {
            return CharTypeEnum.BACKSLASH;
        } else {
            return CharTypeEnum.NORMAL_CHAR;
        }
    }
  
}
