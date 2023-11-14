package src.analyzers.semantic;

import src.analyzers.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SymbolTable {
    private static final Stack<Symbol> symbolStack = new Stack<>();

    public static void insertSymbol(Symbol symbol) {
        symbolStack.add(symbol);
    }

    public static Symbol insertSymbol(Token token, boolean localScope) {
        Symbol symbol = new Symbol(token.lexeme);
        symbol.setLocalScope(localScope);
        insertSymbol(symbol);
        return symbol;
    }

    public static Symbol getSymbol(int index) {
        return symbolStack.get(index);
    }

    public static Symbol getSymbol(String lexeme) {
        int index = searchSymbol(lexeme, false);
        if (index == -1) return null;

        return getSymbol(index);
    }

    //coloca tipo em todos que estão sem tipo até o primeiro com tipo
    public static void putVarTypesInTable(Token token) {
        SymbolType type = null;
        if (token.is(Token.SINTEIRO)) {
            type = SymbolType.VARIAVEL_INTEIRO;
        } else if (token.is(Token.SBOOLEANO)) {
            type = SymbolType.VARIAVEL_BOOLEANO;
        }
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getType() != null) {
                break;
            }
            symbol.setType(type);
        }
    }

    public static void putVarAddresses(int variablesCount) {
        int size = symbolStack.size();
        for (int i = size - variablesCount; i < size; i++) {
            Symbol symbol = symbolStack.get(i);
            symbol.setAddress(Symbol.nextAvailableAddress++);
        }
    }

    public static int popUntilLocalScope() {
        int variablesCount = 0;
        while (!symbolStack.isEmpty() && (!symbolStack.peek().getLocalScope())) {
            Symbol removedSymbol = symbolStack.pop();
            if (SymbolType.variables.contains(removedSymbol.type)) {
                variablesCount++;
            }
        }
        if(!symbolStack.isEmpty()) {
            symbolStack.peek().setLocalScope(false);
        }

        return variablesCount;
    }

    public static Symbol searchSymbol(String searchLexeme, SymbolType... searchType) {
        int index = searchSymbol(searchLexeme, false, searchType);
        if (index == -1) return null;

        return symbolStack.get(index);
    }
    public static int searchSymbol(String searchLexeme, boolean untilLocalScope, SymbolType... searchTypes) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.identifier.equals(searchLexeme)) {
                //Verifica se o identificador esta entre os tipos pesquisado (se nao forem informados os tipos, retornar true)
                List<SymbolType> searchTypeList  = Arrays.stream(searchTypes).toList();
                if (searchTypeList.isEmpty() || searchTypeList.contains(symbol.type)) {
                    return i;
                }
            }
            if (untilLocalScope && symbol.getLocalScope()) {
                break;
            }
        }
        return -1;
    }

    public static boolean isReturnVar(String lexeme) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);

            if (symbol.localScope) {
                return symbol.identifier.equals(lexeme);
            }
        }

        return false;
    }

    // procurar se existe duplicidade de var em nivel LOCAL
    public static boolean searchDuplicityVarTable(String lexeme) {
        return has(lexeme, true, SymbolType.VARIAVEL_BOOLEANO, SymbolType.VARIAVEL_INTEIRO);
    }

    public static boolean hasFunctionDeclaration(String lexeme) {
        return has(lexeme, false, SymbolType.FUNCAO_BOOLEANO, SymbolType.FUNCAO_INTEIRO);
    }

    //funcao para procurar se existe funcao ou variavel a nivel GLOBAL
    public static boolean hasVarOrFunctionDeclaration(String lexeme) {
        return has(lexeme, false, SymbolType.VARIAVEL_BOOLEANO, SymbolType.VARIAVEL_INTEIRO, SymbolType.FUNCAO_BOOLEANO, SymbolType.FUNCAO_INTEIRO);
    }

    public static boolean hasProcedureDeclaration(String lexeme) {
        return has(lexeme, false, SymbolType.PROCEDIMENTO);
    }

    public static boolean hasVarDeclaration(String lexeme, boolean localScope) {
        return has(lexeme, localScope, SymbolType.VARIAVEL_BOOLEANO, SymbolType.VARIAVEL_INTEIRO);
    }

    private static boolean has(String searchLexeme, boolean untilLocalScope, SymbolType... searchType) {
        return searchSymbol(searchLexeme, untilLocalScope, searchType) != -1;
    }
}

