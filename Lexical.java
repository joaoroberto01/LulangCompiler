import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class Lexical {
    private static final String sourceCode = readFileAsString("source_code.ll");
    private static final int length = sourceCode.length();

    private static final List<Character> ARITHMETHIC_OPERATORS = Arrays.asList('+', '-', '*');
    private static final List<Character> RELATIONAL_OPERATORS = Arrays.asList('!', '<', '>', '=');
    private static final List<Character> PUNCTUATION = Arrays.asList(';', ',', '(', ')', '.');
    private static int currentIndex = 0;
    private static List<Token> tokenList = new ArrayList<>();

    private static Character currentChar;


    public static void analyze() {


        while (!eof()) {
            removeTrash();
            Token token = getToken();

            tokenList.add(token);
        }
        System.out.println("\nacabou o semestre");
    }

    private static Token getToken() {
        if (Character.isDigit(currentChar)) {
            return handleDigit();
        } else if (Character.isLetter(currentChar)) {
            return handleIdentifierAndReservedWord();
        } else if (currentChar == ':') {
            //TODO tratar atribuiçao
        } else if (ARITHMETHIC_OPERATORS.contains(currentChar)) {
            //TODO trata operadores aritmetico
        } else if (RELATIONAL_OPERATORS.contains(currentChar)) {
            //TODO trata operadores relacional
        } else if (PUNCTUATION.contains(currentChar)) {
            //TODO trata pontuaçao
        }

        return null;
    }

    private static Token handleDigit() {
        StringBuilder number = new StringBuilder(currentChar);

//        read();
        while (Character.isDigit(currentChar)) {
            number.append(currentChar.toString());
            read();
        }

        return new Token("snumero", number.toString());
    }

    private static Token handleIdentifierAndReservedWord() {
        StringBuilder identifier = new StringBuilder(currentChar);

//        read();
        while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            identifier.append(currentChar.toString());
            read();
        }

        String symbol = Token.reservedSymbols.getOrDefault(identifier.toString(), "sidentificador");

        return new Token(symbol, identifier.toString());
    }

    private static void removeTrash() {
        read();
        //while(!eof()) {
            while ((currentChar == '{' || Character.isWhitespace(currentChar)) && !eof()) {
                if (currentChar == '{') {
                    while (currentChar != '}' && !eof()) {
                        read();
                    }
                    read(); //- JOAO E MATHEUS ACHAM LEGAM ISSO
                }
                while (Character.isWhitespace(currentChar) && !eof()) {
                    read();
                }
            }
//        }
    }

    //TODO trata atribuicao


    //TODO trata pontuacao



    private static void read() {
        currentChar = sourceCode.charAt(currentIndex++);
    }

    private static boolean eof() {
        return currentIndex == length;
    }

    private static String readFileAsString(String filename) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
