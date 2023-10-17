import java.util.AbstractMap;
import java.util.Map;

public class Token {
    public final String symbol;
    public final String lexeme;

    public Token(String symbol, String lexeme) {
        this.symbol = symbol;
        this.lexeme = lexeme;
    }

    public Boolean is(String symbol) {
        return this.symbol.equals(symbol);
    }

    @Override
    public String toString() {
        return String.format("[%s|%s]", symbol, lexeme);
    }

    public static final String SPONTO_VIRGULA = "sponto_virgula";
    public static final String SVIRGULA = "svirgula";
    public static final String SABRE_PARENTESES = "sabre_parenteses";
    public static final String SFECHA_PARENTESES = "sfecha_parenteses";
    public static final String SPONTO = "sponto";
    public static final String SDOISPONTOS = "sdoispontos";
    public static final String SATRIBUICAO = "satribuicao";
    public static final String SMAIS = "smais";
    public static final String SMENOS = "smenos";
    public static final String SMULT = "smult";
    public static final String SPROGRAMA = "sprograma";
    public static final String SSE = "sse";
    public static final String SENTAO = "sentao";
    public static final String SSENAO = "ssenao";
    public static final String SENQUANTO = "senquanto";
    public static final String SFACA = "sfaca";
    public static final String SINICIO = "sinicio";
    public static final String SFIM = "sfim";
    public static final String SESCREVA = "sescreva";
    public static final String SLEIA = "sleia";
    public static final String SVAR = "svar";
    public static final String SINTEIRO = "sinteiro";
    public static final String SBOOLEANO = "sbooleano";
    public static final String SVERDADEIRO = "sverdadeiro";
    public static final String SFALSO = "sfalso";
    public static final String SPROCEDIMENTO = "sprocedimento";
    public static final String SFUNCAO = "sfuncao";
    public static final String SDIV = "sdiv";
    public static final String SE = "se";
    public static final String SOU = "sou";
    public static final String SNAO = "snao";
    public static final String SIDENTIFICADOR = "sidentificador";
    public static final String SNUMERO = "snumero";
    public static final String SMAIOR = "smaior";
    public static final String SMAIORIG = "smaiorig";
    public static final String SIG = "sig";
    public static final String SMENOR = "smenor";
    public static final String SMENORIG = "smenorig";
    public static final String SDIF = "sdif";


    public static final Map<String, String> PUNCTUATION_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>(";", SPONTO_VIRGULA),
            new AbstractMap.SimpleImmutableEntry<>(",", SVIRGULA),
            new AbstractMap.SimpleImmutableEntry<>("(", SABRE_PARENTESES),
            new AbstractMap.SimpleImmutableEntry<>(")", SFECHA_PARENTESES),
            new AbstractMap.SimpleImmutableEntry<>(".", SPONTO)
    );

    public static final Map<String, String> ARITHMETHIC_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("+", SMAIS),
            new AbstractMap.SimpleImmutableEntry<>("-", SMENOS),
            new AbstractMap.SimpleImmutableEntry<>("*", SMULT)
    );

    public static final Map<String, String> RESERVED_SYMBOLS = Map.ofEntries(
            new AbstractMap.SimpleImmutableEntry<>("programa", SPROGRAMA),
            new AbstractMap.SimpleImmutableEntry<>("se", SSE),
            new AbstractMap.SimpleImmutableEntry<>("entao", SENTAO),
            new AbstractMap.SimpleImmutableEntry<>("senao", SSENAO),
            new AbstractMap.SimpleImmutableEntry<>("enquanto", SENQUANTO),
            new AbstractMap.SimpleImmutableEntry<>("faca", SFACA),
            new AbstractMap.SimpleImmutableEntry<>("inicio", SINICIO),
            new AbstractMap.SimpleImmutableEntry<>("fim", SFIM),
            new AbstractMap.SimpleImmutableEntry<>("escreva", SESCREVA),
            new AbstractMap.SimpleImmutableEntry<>("leia", SLEIA),
            new AbstractMap.SimpleImmutableEntry<>("var", SVAR),
            new AbstractMap.SimpleImmutableEntry<>("inteiro", SINTEIRO),
            new AbstractMap.SimpleImmutableEntry<>("booleano", SBOOLEANO),
            new AbstractMap.SimpleImmutableEntry<>("verdadeiro", SVERDADEIRO),
            new AbstractMap.SimpleImmutableEntry<>("falso", SFALSO),
            new AbstractMap.SimpleImmutableEntry<>("procedimento", SPROCEDIMENTO),
            new AbstractMap.SimpleImmutableEntry<>("funcao", SFUNCAO),
            new AbstractMap.SimpleImmutableEntry<>("div", SDIV),
            new AbstractMap.SimpleImmutableEntry<>("e", SE),
            new AbstractMap.SimpleImmutableEntry<>("ou", SOU),
            new AbstractMap.SimpleImmutableEntry<>("nao", SNAO)
    );


}
