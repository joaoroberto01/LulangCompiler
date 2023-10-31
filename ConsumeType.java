public class ConsumeType {
    public SymbolType input;
    public SymbolType output;

    public ConsumeType(SymbolType symbolType) {
        input = output = symbolType;
    }

    public ConsumeType(SymbolType input, SymbolType output) {
        this.input = input;
        this.output = output;
    }
}
