package src.exceptions;

public class SyntacticException extends CompilerException {
    public SyntacticException(String expectedLexeme) {
        super(String.format("expected '%s' in declaration", expectedLexeme));
    }

    public SyntacticException() {
        super("unexpected token in declaration");
    }


}
