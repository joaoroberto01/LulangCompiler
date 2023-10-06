import java.util.Stack;

public class SymbolTable {
  private final Stack<Symbol> symbolStack = new Stack<>();

    public void insertSymbol(Symbol symbol){
        symbolStack.add(symbol);
    }

//    public int searchFirstLocalScopePosition(){
//        int i = 0;
//        for (Symbol symbol : symbolStack) {
//            if (symbol.getType() == null)
//                return i;
//            i++;
//        }
//        return -1;
//    }

    // procurar se existe duplicidade de var em nivel LOCAL

    public boolean searchDuplicityVarTable(String lexeme) {
        for (Symbol symbol : symbolStack) {
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
            if (symbol.getLocalScope()) {
                break;
            }
        }
        return false;
    }

    // procurar se existe declaracao de var em nivel GLOBAL
    public boolean searchDeclarationVarTable(String lexeme) {
        for (Symbol symbol : symbolStack) {
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
        }
        return false;
    }


    //funcao para procurar se existe funcao ou variavel a nivel GLOBAL
    public boolean searchDeclarationVarOrFuncTable(String lexeme) {
        for (Symbol symbol : symbolStack) {
            //nao entendi
            if (symbol.getIdentifier().equals(lexeme) && (!symbol.getType().equals(SymbolType.PROCEDIMENTO) || !symbol.getType().equals(null) )) {
                return true;
            }
            if (symbol.getLocalScope()) {
                break;
            }
        }
        return false;
    }


    public boolean searchDuplicityProcTable(String lexeme) {
        for (Symbol symbol : symbolStack) {
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
            if (symbol.getLocalScope()) {
                break;
            }
        }
        return false;
    }

    //coloca tipo em todos que estão sem tipo até o primeiro com tipo
    public void putTypeTable(SymbolType Type){
        for (Symbol symbol : symbolStack) {

            if (symbol.getType() != null) {
                break;
            }
            symbol.setType(Type);
        }

    }

}

