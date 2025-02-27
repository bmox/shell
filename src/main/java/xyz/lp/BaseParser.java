package xyz.lp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseParser implements Parser {

    private Set<Character> DOUBLE_QUOTE_SPECIAL_CHAR_SET = Set.of('\n', '\\', '$', '"');

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
                    tokenBuilder.append(ch);
                    break;
                case BACKSLASH_CHAR_IN_DOUBLE_QUOTE:
                    if (!DOUBLE_QUOTE_SPECIAL_CHAR_SET.contains(ch)) {
                        tokenBuilder.append('\\');
                    }
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

        tryRedirectStdout(tokens);

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

    private void tryRedirectStdout(List<String> tokens) {
        Iterator<String> it = tokens.iterator();
        while (it.hasNext()) {
            String token = it.next();
            String path = null;
            if (token.startsWith(">")) {
                path = token.substring(">".length());
            } else if (token.startsWith("1>")) {
                path = token.substring("1>".length());
            }
            if (path == null) {
                continue ;
            }
            it.remove();
            if (!path.isBlank()) {
                Context.getInstance().setRedirectOutputFile(getRedirectFile(path));
                break ;
            } else if (it.hasNext()) {
                path = it.next();
                Context.getInstance().setRedirectOutputFile(getRedirectFile(path));
                it.remove();
                break ;   
            }
        }
    }

    // private PrintStream getRedirectPrintStream(String path) {
    //     File redirectFile = getRedirectFile(path);
    //     try {
    //         return new PrintStream(redirectFile);
    //     } catch (FileNotFoundException e) {
    //         // ignore
    //     }
    //     return System.out;
    // }

    private File getRedirectFile(String path) {
        File redirectFile = new File(path);
        if (!redirectFile.isAbsolute()) {
            redirectFile = new File(Context.getInstance().getCurrentPath() + File.pathSeparator + path);
        }
        if (!redirectFile.exists()) {
            try {
                Files.createDirectories(Paths.get(redirectFile.getParent()));
                redirectFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return redirectFile;
    }
  
}
