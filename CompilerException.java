public class CompilerException extends RuntimeException {
    public CompilerException(String message) {
        super(String.format("%s:%d:%d\nerror: %s\n\n", Lexical.filename, Lexical.lineCounter, Lexical.columnCounter, message));
    }




}
