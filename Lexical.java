import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        read();
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
            return handleAttribution();
        } else if (ARITHMETHIC_OPERATORS.contains(currentChar)) {
            return handleArithmetic();
        } else if (RELATIONAL_OPERATORS.contains(currentChar)) {
            //TODO trata operadores relacional

            read();
        } else if (PUNCTUATION.contains(currentChar)) {
           return handlePunctuation();

        }

        return null;
    }

    private static Token handleDigit() {
        StringBuilder number = new StringBuilder(currentChar);

      // read();
        while (Character.isDigit(currentChar)) {
            number.append(currentChar.toString());
            read();
        }

        return new Token("snumero", number.toString());
    }


    private static Token handleArithmetic(){

        Token token = null;

        switch (currentChar) {
            case '+' -> {
                 token = new Token("smais", currentChar.toString());
            }
            case '-' -> {
                token = new Token("smenos", currentChar.toString());
            }
            case '*' -> {
                token = new Token("smult", currentChar.toString());
            }
        }
        read();
        return token;
    }

    private  static Token handleAttribution(){

        read();
        if (currentChar == '=')
        {
            return new Token("satribuição",":=");

        }
        return new Token("sdoispontos",":");


    }



    private static Token handleIdentifierAndReservedWord() {
        StringBuilder identifier = new StringBuilder(currentChar);

        //read();
        while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            identifier.append(currentChar.toString());
            read();
        }

        String symbol = Token.reservedSymbols.getOrDefault(identifier.toString(), "sidentificador");

        return new Token(symbol, identifier.toString());
    }

    private static void removeTrash() {
          // read();

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
    private static Token handlePunctuation(){

        Token token = null;
        switch (currentChar) {
            case ';' -> {
                token = new Token("sponto_virgula", currentChar.toString());
            }
            case ',' -> {
                token = new Token("svirgula", currentChar.toString());
            }
            case '(' -> {
                token = new Token("sabre_parenteses", currentChar.toString());
            }
            case ')' -> {
                token = new Token("sfecha_parenteses", currentChar.toString());
            }
            case '.' -> {
                token = new Token("sponto", currentChar.toString());
            }
        }
        read();
        return token;


    }


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
