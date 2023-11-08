package src.exceptions;

public class LexicalException extends CompilerException {

    public LexicalException(String message) {
        super(message);
    }
    public LexicalException(char unrecognizedSymbol) {
        super(String.format("unrecognized symbol '%c'", unrecognizedSymbol));
    }

    public static LexicalException unclosedCommentException() {
        return new LexicalException("unclosed comment. missing '}'");
    }

}
