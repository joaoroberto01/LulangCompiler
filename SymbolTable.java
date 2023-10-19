import java.util.Stack;

public class SymbolTable {
    private static final Stack<Symbol> symbolStack = new Stack<>();

    public static void insertSymbol(Symbol symbol) {
        symbolStack.add(symbol);
    }


    public static Symbol insertSymbol(Token token) {
        Symbol symbol = new Symbol(token.lexeme);
        symbolStack.add(symbol);
        return symbol;
    }

    public static Symbol getSymbol(int index)
    {
        return symbolStack.get(index);
    }

    public static int searchTable(String lexeme)
    {

        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return i;
            }

        }
        return -1;

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
    //todo verificar ordem de interacao


    public static boolean searchDuplicityVarTable(String lexeme) {

        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
            if (symbol.getLocalScope()) {
                break;
            }

        }
        return false;
    }

    public static boolean searchDeclarationFuncTable(String lexeme) {

        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }

        }
        return false;
    }

    // procurar se existe declaracao de var em nivel



    //funcao para procurar se existe funcao ou variavel a nivel GLOBAL
    public static boolean searchDeclarationVarOrFuncTable(String lexeme) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme) && (symbol.getType() != null && !symbol.getType().equals(SymbolType.PROCEDIMENTO))) {
                return true;
            }
//            if (symbol.getLocalScope()) {
//                break;
//            }
        }
        return false;
    }


    public static boolean searchDeclarationProcTable(String lexeme) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchDuplicityProcTable(String lexeme) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
//            if (symbol.getLocalScope()) {
//                break;
//            }
        }
        return false;
    }

    public static boolean searchDeclarationVarTable(String lexeme) {
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getIdentifier().equals(lexeme)) {
                return true;
            }
        }
        return false;
    }

    //coloca tipo em todos que estão sem tipo até o primeiro com tipo
    public static void putTypeTable(Token token) {
        SymbolType type = null;
        if (token.is(Token.SINTEIRO)) {
            type = SymbolType.VARIAVELINTEIRO;
        } else if (token.is(Token.SBOOLEANO)) {
            type = SymbolType.VARIAVELBOOLEANO;
        }
        for (int i = symbolStack.size() - 1; i > 0; i--) {
            Symbol symbol = symbolStack.get(i);
            if (symbol.getType() != null) {
                break;
            }
            symbol.setType(type);
        }
    }

    public  static  void popUntilLocalScope()
    {
        while (!symbolStack.isEmpty() && (!symbolStack.peek().getLocalScope()))
        {
            symbolStack.pop();
        }
        if(!symbolStack.isEmpty())
        {
            symbolStack.peek().setLocalScope(false);
        }

    }

}

