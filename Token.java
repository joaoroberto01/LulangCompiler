import java.util.AbstractMap;
import java.util.Map;

public class Token {
    private String symbol;
    private String lexeme;

    public Token(String symbol, String lexeme) {
        this.symbol = symbol;
        this.lexeme = lexeme;
    }

    public Token(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return String.format("[%s|%s]", symbol, lexeme);
    }

    public static final Map<String, String> PUNCTUATION_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>(";", "sponto_virgula"),
            new AbstractMap.SimpleImmutableEntry<>(",", "svirgula"),
            new AbstractMap.SimpleImmutableEntry<>("(", "sabre_parenteses"),
            new AbstractMap.SimpleImmutableEntry<>(")", "sfecha_parenteses"),
            new AbstractMap.SimpleImmutableEntry<>(".", "sponto")
    );

    public static final Map<String, String> ARITHMETHIC_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("+", "smais"),
            new AbstractMap.SimpleImmutableEntry<>("-", "smenos"),
            new AbstractMap.SimpleImmutableEntry<>("*", "smult")
    );

    public static final Map<String, String> RESERVED_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("programa", "sprograma"),
            new AbstractMap.SimpleImmutableEntry<>("se", "sse"),
            new AbstractMap.SimpleImmutableEntry<>("entao", "sentao"),
            new AbstractMap.SimpleImmutableEntry<>("senao", "ssenao"),
            new AbstractMap.SimpleImmutableEntry<>("enquanto", "senquanto"),
            new AbstractMap.SimpleImmutableEntry<>("faca", "sfaca"),
            new AbstractMap.SimpleImmutableEntry<>("inicio", "sinicio"),
            new AbstractMap.SimpleImmutableEntry<>("fim", "sfim"),
            new AbstractMap.SimpleImmutableEntry<>("escreva", "sescreva"),
            new AbstractMap.SimpleImmutableEntry<>("leia", "sleia"),
            new AbstractMap.SimpleImmutableEntry<>("var", "svar"),
            new AbstractMap.SimpleImmutableEntry<>("inteiro", "sinteiro"),
            new AbstractMap.SimpleImmutableEntry<>("booleano", "sbooleano"),
            new AbstractMap.SimpleImmutableEntry<>("verdadeiro", "sverdadeiro"),
            new AbstractMap.SimpleImmutableEntry<>("falso", "sfalso"),
            new AbstractMap.SimpleImmutableEntry<>("procedimento", "sprocedimento"),
            new AbstractMap.SimpleImmutableEntry<>("funcao", "sfuncao"),
            new AbstractMap.SimpleImmutableEntry<>("div", "sdiv"),
            new AbstractMap.SimpleImmutableEntry<>("e", "se"),
            new AbstractMap.SimpleImmutableEntry<>("ou", "sou"),
            new AbstractMap.SimpleImmutableEntry<>("nao", "snao")
    );
}
