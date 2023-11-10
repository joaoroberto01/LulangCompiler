package src.analyzers.semantic;

import src.analyzers.Token;
import src.exceptions.CompilerException;
import src.exceptions.SemanticException;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PosfixConverter {

    public static List<Token> infixToPostfix(List<Token> expressionList) {
        List<Token> postfix = new ArrayList<>();
        Stack<Token> stack = new Stack<>();

        for (Token term : expressionList) {
            if (precedence(term.lexeme) != -1){
                //operador
                while (!stack.isEmpty() && !stack.peek().lexeme.equals("(") && precedence(stack.peek().lexeme) >= precedence(term.lexeme)) {
                    postfix.add(stack.pop());
                }
                stack.push(term);
            } else if (term.lexeme.equals("(")) {
                stack.push(term);
            } else if (term.lexeme.equals(")")) {
                while (!stack.isEmpty()) {
                    Token pop = stack.pop();
                    if (pop.lexeme.equals("(")) break;

                    postfix.add(pop);
                }
            } else if (term.is(Token.SIDENTIFICADOR) || term.is(Token.SNUMERO) ||
                    term.is(Token.SVERDADEIRO) || term.is(Token.SFALSO) || term.is(Token.SFUNCAO)){
                postfix.add(term);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        expressionList.clear();
        return postfix;
    }



    public static SymbolType semantic(List<Token> postfix){
        List<Symbol> symbols = preProcess(postfix);

        SymbolType returnType = null;

        for (int i = 0; i < symbols.size(); i++) {
            Symbol symbol = symbols.get(i);

            if (symbol.getType() != null) continue;

            ConsumeType consumeType = consumeType(symbol.identifier);

            if (symbol.identifier.equals(Token.SPOSITIVO) || symbol.identifier.equals(Token.SNEGATIVO) || symbol.identifier.equals("nao")) {
                Symbol s1 = symbols.get(i - 1);
                if (!s1.equivalentTypeTo(consumeType.input)) {
                    throw SemanticException.expressionNotAllowedException(symbol, s1);
                }
                symbols.remove(i - 1);
                i = i - 1;
                symbol.type = consumeType.output;
                continue;
            }

            Symbol s1 = symbols.get(i - 1);
            Symbol s2 = symbols.get(i - 2);

            if (!s1.equivalentTypeTo(s2.type) || !s1.equivalentTypeTo(consumeType.input)) {
                throw SemanticException.expressionNotAllowedException(symbol, s1, s2);
            }
            symbols.remove(i - 1);
            symbols.remove(i - 2);
            i = i - 2;

            symbol.identifier = null;
            returnType = symbol.type = consumeType.output;
        }

        if (returnType == null && !symbols.isEmpty()) {
            returnType = symbols.get(0).type;
        }

        return returnType;
    }

    public static int consumeSize(String input) {
        if (input.equals(Token.SPOSITIVO) || input.equals(Token.SNEGATIVO) || input.equals("nao")) {
            return 1;
        }

        return 2;
    }

    public static ConsumeType consumeType(String input) {
        if (input.equals("e") || input.equals("ou") || input.equals("nao")) {
            return new ConsumeType(SymbolType.VARIAVEL_BOOLEANO);
        }

        if (input.equals(">") || input.equals("<") || input.equals(">=") || input.equals("<=") || input.equals("=") || input.equals("!=")) {
            return new ConsumeType(SymbolType.VARIAVEL_INTEIRO, SymbolType.VARIAVEL_BOOLEANO);
        }

        return new ConsumeType(SymbolType.VARIAVEL_INTEIRO);
    }

    private static int precedence(String operator) {
        if (operator.equals("+") || operator.equals("-")
                || operator.equals(">") || operator.equals("<") || operator.equals(">=") || operator.equals("<=") || operator.equals("=") || operator.equals("!=")
                || operator.equals("ou")) {
            return 1;
        }

        if (operator.equals("*") || operator.equals("div")
                || operator.equals("e")) {
            return 2;
        }

        if (operator.equals(Token.SPOSITIVO) || operator.equals(Token.SNEGATIVO)
                || operator.equals("nao")) {
            return 3;
        }
        return -1;
    }

    private static List<Symbol> preProcess(List<Token> postfix) {
        List<Symbol> symbols = new ArrayList<>();

        postfix.forEach(term -> {
            Symbol symbol = new Symbol(term.lexeme);

            if (term.is(Token.SIDENTIFICADOR)){
                symbol = SymbolTable.getSymbol(term.lexeme);
                if (symbol == null) {
                    throw SemanticException.symbolDeclaredException("symbol", term.lexeme, false);
                }
            } else if(term.is(Token.SNUMERO)) {
                symbol.setType(SymbolType.VARIAVEL_INTEIRO);
            } else if (term.is(Token.SVERDADEIRO) || term.is(Token.SFALSO)) {
                symbol.setType(SymbolType.VARIAVEL_BOOLEANO);
            }

            symbols.add(symbol);
        });
        return symbols;
    }
}
