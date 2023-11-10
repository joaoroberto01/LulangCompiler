package src.analyzers.semantic;

import java.util.Arrays;
import java.util.List;

public enum SymbolType {
    VARIAVEL_INTEIRO, VARIAVEL_BOOLEANO, PROCEDIMENTO, FUNCAO_INTEIRO, FUNCAO_BOOLEANO, PROGRAMA;

    public final static List<SymbolType> integers = Arrays.asList(FUNCAO_INTEIRO, VARIAVEL_INTEIRO);
    public final static List<SymbolType> booleans = Arrays.asList(FUNCAO_BOOLEANO, VARIAVEL_BOOLEANO);
    public final static List<SymbolType> functions = Arrays.asList(FUNCAO_BOOLEANO, FUNCAO_INTEIRO);
    public final static List<SymbolType> variables = Arrays.asList(VARIAVEL_BOOLEANO, VARIAVEL_INTEIRO);
}
