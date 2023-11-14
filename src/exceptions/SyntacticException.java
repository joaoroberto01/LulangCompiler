package src.exceptions;

import src.analyzers.Syntactic;

public class SyntacticException extends CompilerException {
    public SyntacticException(String expectedLexeme) {
        super(String.format("expected '%s' in declaration, found '%s'", expectedLexeme, Syntactic.getCurrentToken().lexeme));
    }

    public SyntacticException() {
        super("unexpected token in declaration");
    }


}
