import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Lexical {
    private static final String sourceCode = readFileAsString("source_code.ll");
    private static final int length = sourceCode.length();

    private static final List<Character> RELATIONAL_OPERATORS = Arrays.asList('!', '<', '>', '=');
    private static final List<Token> tokenList = new ArrayList<>();
    private static int currentIndex = -1;
    public static boolean eof;

    private static boolean unclosedComment;

    private static Character currentChar;

    public static int lineCount = 1;

    public static void init() {
        read();
    }

    public static void analyze() {
        init();
        while (notEof()) {
            handleCommentsAndWhitespaces();
            if(unclosedComment || eof)
                break;
            Token token = getToken();

            tokenList.add(token);
        }
        System.out.println("\nacabou o semestre");
    }

    public static Token nextToken() {
        handleCommentsAndWhitespaces();
        if(unclosedComment || eof)
            return null;
        return getToken();
    }

    private static void read() {
        if (eof) return;

        currentIndex++;
        eof = currentIndex >= length;

        if (eof) return;
        currentChar = sourceCode.charAt(currentIndex);

        if (currentChar == '\n') {
            lineCount++;
        }
    }

    private static boolean notEof() {
        return currentIndex < length;
    }

    private static boolean isAlphabetCharacter(char c) {
        return c >= 65 && c <= 90 || c >= 97 && c <= 122;
    }

    private static boolean isNumber(char c) {
        return c >= 48 && c <= 57;
    }

    private static Token getToken() {
        if (Character.isDigit(currentChar)) {
            return handleDigit();
        } else if (isAlphabetCharacter(currentChar)) {
            return handleIdentifierAndReservedWord();
        } else if (currentChar == ':') {
            return handleAttribution();
        } else if (Token.ARITHMETHIC_SYMBOLS.containsKey(currentChar.toString())) {
            return handleArithmetic();
        } else if (RELATIONAL_OPERATORS.contains(currentChar)) {
            return handleRelationalOperators();
        } else if (Token.PUNCTUATION_SYMBOLS.containsKey(currentChar.toString())) {
            return handlePunctuation();
        }

        Token errorToken = new Token("serro", currentChar.toString());
        read();
        return errorToken;
    }

    private static Token handleDigit() {
        StringBuilder number = new StringBuilder(currentChar.toString());

        read();
        while (Character.isDigit(currentChar) && notEof()) {
            number.append(currentChar.toString());
            read();
        }

        return new Token("snumero", number.toString());
    }

    private static Token handleRelationalOperators() {
        Token token = null;

        switch (currentChar) {
            case '>' -> {
                read();
                if (currentChar == '=') {
                    token = new Token("smaiorig", ">=");
                } else {
                    return new Token("smaior", ">");
                }
            }
            case '<' -> {
                read();
                if (currentChar == '=') {
                    token = new Token("smenorig", "<=");
                } else {
                    return new Token("smenor", "<");
                }
            }
            case '!' -> {
                read();
                if (currentChar == '=') {
                    token = new Token("sdif", "!=");
                } else {
                    return new Token("serro", "!");
                }
            }

            case '=' -> token = new Token("sig", "=");
        }
        read(); //Leitura em caso de simbolo composto

        return token;

    }


    private static Token handleArithmetic() {
        Token token = getTokenByKey(currentChar.toString(), Token.ARITHMETHIC_SYMBOLS);

        read();
        return token;
    }

    private static Token handleAttribution() {
        read();
        if (currentChar == '=') {
            read();
            return new Token("satribuicao",":=");
        }

        return new Token("sdoispontos",":");
    }

    private static Token handleIdentifierAndReservedWord() {
        StringBuilder identifier = new StringBuilder(currentChar.toString());

        read();
        while ((isAlphabetCharacter(currentChar) || isNumber(currentChar) || currentChar == '_') && notEof()) {
            identifier.append(currentChar.toString());
            read();
        }

        String symbol = Token.RESERVED_SYMBOLS.getOrDefault(identifier.toString(), "sidentificador");

        return new Token(symbol, identifier.toString());
    }

    private static Token handlePunctuation(){
        Token token = getTokenByKey(currentChar.toString(), Token.PUNCTUATION_SYMBOLS);

        read();
        return token;
    }

    private static boolean handleWhitespace() {
        return Character.isWhitespace(currentChar);
    }

    private static void handleCommentsAndWhitespaces() {
        while ((currentChar == '{' || handleWhitespace()) && notEof()) {
            if (currentChar == '{') {
                while (currentChar != '}' && notEof()) {
                    read();
                    if (eof) {
                        tokenList.add(new Token("serro_comentario", "falta}"));
                        unclosedComment = true;
                        return;
                    }
                }
                read(); //- JOAO E MATHEUS ACHAM LEGAM ISSO
            }
            while (handleWhitespace() && notEof()) {
                read();
            }
        }
    }

    private static Token getTokenByKey(String key, Map<String, String> symbolMap) {
        if (!symbolMap.containsKey(key)) return null;

        String symbol = symbolMap.get(key);
        return new Token(symbol, key);
    }

    private static String readFileAsString(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
