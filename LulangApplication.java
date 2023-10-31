import java.util.ArrayList;
import java.util.List;

public class LulangApplication {

    public static void main(String[] args) {
        try {
//            Syntactic.analyze();
            List<Token> in = new ArrayList<>(List.of(
                    new Token(Token.SNUMERO, "100"),
                    new Token(Token.SNUMERO, "7"),
                    new Token(Token.SNUMERO, "5"),
                    new Token(Token.SMULT, "*"),
                    new Token(Token.SNUMERO, "30"),
                    new Token(Token.SNUMERO, "200"),
                    new Token(Token.SMAIS, "+"),
                    new Token(Token.SDIV, "div"),
                    new Token(Token.SMAIS, "+"),
                    new Token(Token.SNUMERO, "100"),
                    new Token(Token.SNUMERO, "999"),
                    new Token(Token.SMULT, "*"),
                    new Token(Token.SNUMERO, "2"),
                    new Token(Token.SMAIS, "+"),
                    new Token(Token.SMENORIG, "<="),
                    new Token(Token.SNUMERO, "300"),
                    new Token(Token.SNUMERO, "0"),
                    new Token(Token.SMAIOR, ">"),
                    new Token(Token.SE, "e")
            ));
//            List<Token> postfix = PosfixConverter.infixToPostfix(in);
            PosfixConverter.semantic(in);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}



//infixo: 4 - 3
//3 +u 3 -