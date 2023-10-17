import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class PosfixConverter {
    public static String infixToPostfix(List<String> expressionList) {
        expressionList = Arrays.stream("( x + 7 * 5 div ( 30 + y ) <= ( x * a + 2 ) ) e ( z > 0 )".split(" ")).toList();
        StringBuilder postfix = new StringBuilder();
        Stack<String> stack = new Stack<>();


        //infix: (3 * 3 + juninho)

        //posfix: 3 3 * juninho +

        for (String term : expressionList) {
            if (precedence(term) != -1){
                //operador
                while (!stack.isEmpty() && !stack.peek().equals("(") && precedence(stack.peek()) >= precedence(term)) {
                    postfix.append(stack.pop());
                }
                stack.push(term);
            } else if (term.equals("(")) {
                stack.push(term);
            } else if (term.equals(")")) {
                while (!stack.isEmpty()) {
                    String pop = stack.pop();
                    if (pop.equals("(")) break;

                    postfix.append(pop);
                }
            } else if (Character.isLetterOrDigit(term.charAt(0))){
                postfix.append(term);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
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
