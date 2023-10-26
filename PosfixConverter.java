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
                while (!stack.isEmpty() && !stack.peek().equals("(") && precedence(stack.peek().lexeme) >= precedence(term.lexeme)) {
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
            } else if (term.is(Token.SIDENTIFICADOR) || term.is(Token.SNUMERO) || term.is(Token.SBOOLEANO) || term.is(Token.SFUNCAO)){
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
        return;
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
                // TODO -> BUSCAR TIPO NA TABELA
            } else if(term.is(Token.SNUMERO)) {
                symbol.setType(SymbolType.VARIAVELINTEIRO);
            } else if (term.is(Token.SBOOLEANO)) {
                symbol.setType(SymbolType.VARIAVELBOOLEANO);
            }
            // TODO -> SFUNCAO


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
