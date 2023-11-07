import java.beans.Expression;
import java.util.ArrayList;
import java.util.Arrays;
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



    public static void semantic(List<Token> postfix){
        List<Symbol> symbols = preProcess(postfix);

        //TODO tratar unario +u -u
        for (int i = 0; i < symbols.size(); i++) {
            Symbol symbol = symbols.get(i);

            if (symbol.getType() != null) continue;

            ConsumeType consumeType = consumeType(symbol.identifier);

            if (symbol.identifier.equals("+u") || symbol.identifier.equals("-u") || symbol.identifier.equals("nao")) {
                Symbol s1 = symbols.get(i - 1);
                if (!s1.type.equals(consumeType.input)) {
                    throw new CompilerException("erro semantico: tipo");
                }
                symbols.remove(i - 1);
                i = i - 1;
                symbol.type = consumeType.output;
                continue;
            }

            Symbol s1 = symbols.get(i - 1);
            Symbol s2 = symbols.get(i - 2);

            if (!s1.type.equals(s2.type) || !s1.type.equals(consumeType.input)) {
                throw new CompilerException("erro semantico: tipo");
            }
            symbols.remove(i - 1);
            symbols.remove(i - 2);
            i = i - 2;

            symbol.type = consumeType.output;
        }

        return;
    }

    public static int consumeSize(String input) {
        if (input.equals("+u") || input.equals("-u") || input.equals("nao")) {
            return 1;
        }

        return 2;
    }

    public static ConsumeType consumeType(String input) {
        if (input.equals("e") || input.equals("ou") || input.equals("nao")) {
            return new ConsumeType(SymbolType.VARIAVELBOOLEANO);
        }

        if (input.equals(">") || input.equals("<") || input.equals(">=") || input.equals("<=") || input.equals("=") || input.equals("!=")) {
            return new ConsumeType(SymbolType.VARIAVELINTEIRO, SymbolType.VARIAVELBOOLEANO);
        }

        return new ConsumeType(SymbolType.VARIAVELINTEIRO);
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

        if (operator.equals("+u") || operator.equals("-u")
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
            } else if(term.is(Token.SNUMERO)) {
                symbol.setType(SymbolType.VARIAVELINTEIRO);
            } else if (term.is(Token.SVERDADEIRO) || term.is(Token.SFALSO)) {
                symbol.setType(SymbolType.VARIAVELBOOLEANO);
            }

            symbols.add(symbol);
        });
        return symbols;
    }




    /*
          +  1
          -  1
          *  2
          div  2
          +u 3
          -u 3

          relacionais 1

           nao 3
           e 2
           ou 1
         */
}
