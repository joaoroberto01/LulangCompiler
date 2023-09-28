import java.util.Stack;

public class SymbolTable {
  private final Stack<Symbol> symbolStack = new Stack<>();

    public void insertSymbol(Symbol symbol){
        symbolStack.add(symbol);
    }
    public int searchFirstLocalScopePosition(){
        int i = 0;
        for (Symbol symbol : symbolStack) {
            if (symbol.getType() == SymbolType.NAO_DEFINIDO)
                return i;
            i++;
        }
        return -1;
    }

}

