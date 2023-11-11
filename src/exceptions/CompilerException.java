package src.exceptions;

import src.analyzers.Lexical;

public class CompilerException extends RuntimeException {
    public CompilerException(String message) {
        super(String.format("%s:%d:%d\nerror: %s", Lexical.filename, Lexical.lineCounter, Lexical.columnCounter, message));
    }
}
