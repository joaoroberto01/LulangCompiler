public class SyntacticException extends RuntimeException {
    public SyntacticException(String expectedLexeme) {
        super(String.format("%s:%d:%d\nerror: expected '%s' in declaration\n\n", Lexical.filepath, Lexical.lineCounter, Lexical.columnCounter, expectedLexeme));
    }

    public SyntacticException() {
        super(String.format("%s:%d:%d\nerror: unexpected token in declaration\n\n", Lexical.filepath, Lexical.lineCounter, Lexical.columnCounter));
    }
}
