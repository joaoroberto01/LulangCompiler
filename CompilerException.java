public class CompilerException extends RuntimeException {
    public CompilerException(String message) {
        super(String.format("%s:%d:%d\nerror: %s\n\n", Lexical.filepath, Lexical.lineCounter, Lexical.columnCounter, message));
    }




}
