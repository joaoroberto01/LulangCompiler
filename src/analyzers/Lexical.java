package src.analyzers;

import src.exceptions.LexicalException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Lexical {
    public static String filename;
    private static String sourceCode;
    private static int length;

    private static final List<Character> RELATIONAL_OPERATORS = Arrays.asList('!', '<', '>', '=');
    private static final List<Token> tokenList = new ArrayList<>();
    private static int currentIndex = -1;

    private static int currentLine = 1;
    private static int currentColumn = 1;

    public static boolean eof;

    private static boolean unclosedComment;

    private static Character currentChar;

    public static int lineCounter = 1;
    public static int columnCounter = 1;

    public static void init(String filepath) throws IOException {
        File file = new File(filepath);
        filename = file.getName();
        sourceCode = readFileAsString(filepath);
        length = sourceCode.length();

        read();
    }

//    public static void analyze() {
//        init("source_code.ll");
//        while (notEof()) {
//            handleCommentsAndWhitespaces();
//            if(unclosedComment || eof)
//                break;
//            Token token = getToken();
//
//            tokenList.add(token);
//        }
//        System.out.println("\nacabou o semestre");
//    }

    public static Token nextToken() {
        lineCounter = currentLine;
        columnCounter = currentColumn;

        handleCommentsAndWhitespaces();
        if (eof)
            return null;
        return getToken();
    }

    private static void read() {
        if (eof) return;

        currentIndex++;
        eof = currentIndex >= length;

        if (eof) return;

        currentColumn++;
        if (currentChar != null && currentChar == '\n') {
            currentLine++;
            currentColumn = 1;
        }

        currentChar = sourceCode.charAt(currentIndex);
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

        throw new LexicalException(currentChar);
    }

    private static Token handleDigit() {
        StringBuilder number = new StringBuilder(currentChar.toString());

        read();
        while (Character.isDigit(currentChar) && notEof()) {
            number.append(currentChar.toString());
            read();
        }

        return new Token(Token.SNUMERO, number.toString());
    }

    private static Token handleRelationalOperators() {
        Token token = null;

        switch (currentChar) {
            case '>' -> {
                read();
                if (currentChar == '=') {
                    token = new Token(Token.SMAIORIG, ">=");
                } else {
                    return new Token(Token.SMAIOR, ">");
                }
            }
            case '<' -> {
                read();
                if (currentChar == '=') {
                    token = new Token(Token.SMENORIG, "<=");
                } else {
                    return new Token(Token.SMENOR, "<");
                }
            }
            case '!' -> {
                read();
                if (currentChar == '=') {
                    token = new Token(Token.SDIF, "!=");
                } else {
                    throw new LexicalException('!');
                }
            }

            case '=' -> token = new Token(Token.SIG, "=");
        }
        read();

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
            return new Token(Token.SATRIBUICAO,":=");
        }

        return new Token(Token.SDOISPONTOS,":");
    }

    private static Token handleIdentifierAndReservedWord() {
        StringBuilder identifier = new StringBuilder(currentChar.toString());

        read();
        while ((isAlphabetCharacter(currentChar) || isNumber(currentChar) || currentChar == '_') && notEof()) {
            identifier.append(currentChar);
            read();
        }

        String symbol = Token.RESERVED_SYMBOLS.getOrDefault(identifier.toString(), Token.SIDENTIFICADOR);

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
                        throw LexicalException.unclosedCommentException();
                    }
                }
                read();
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

    private static String readFileAsString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}
