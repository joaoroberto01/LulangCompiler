package src.exceptions;

import src.analyzers.Token;
import src.analyzers.semantic.Symbol;
import src.analyzers.semantic.SymbolType;

public class SemanticException extends CompilerException {


    public SemanticException(String message) {
        super(message);
    }

    public static SemanticException expressionNotAllowedException(Symbol operator, Symbol s1, Symbol s2) {
        if (s1.identifier == null || s2.identifier == null || operator.type != null) {
            return new SemanticException("expression not allowed");
        }
        return new SemanticException(String.format("expression not allowed: '%s %s %s'", s1.identifier, operator.identifier, s2.identifier));
    }

    public static SemanticException expressionNotAllowedException(Symbol operator, Symbol s1) {
        operator.identifier = operator.identifier.replace(Token.SPOSITIVO, "+").replace(Token.SNEGATIVO, "-");

        if (s1.type == null || operator.type != null) {
            return new SemanticException("expression not allowed");
        }
        return new SemanticException(String.format("expression not allowed: '%s %s'", operator.identifier, s1.identifier));
    }
    public static SemanticException incompatibleTypesException(Symbol symbol, SymbolType returnType) {
        return new SemanticException(String.format("incompatible types: '%s' %s and %s", symbol.identifier, symbol.type, returnType));
    }

    public static SemanticException variableDeclaredException(String lexeme, boolean declared) {
        return symbolDeclaredException("variable", lexeme, declared);
    }

    public static SemanticException functionDeclaredException(String lexeme, boolean declared) {
        return symbolDeclaredException("function", lexeme, declared);
    }

    public static SemanticException procedureDeclaredException(String lexeme, boolean declared) {
        return symbolDeclaredException("procedure", lexeme, declared);
    }

    public static SemanticException symbolDeclaredException(String name, String identifier, boolean declared) {
        return new SemanticException(String.format("%s '%s' %s declared", name, identifier, declared ? "already" : "not"));
    }

    public static SemanticException unavailableVariable(String lexeme) {
        return new SemanticException(String.format("unavailable variable name: '%s' (function return)", lexeme));
    }
}
