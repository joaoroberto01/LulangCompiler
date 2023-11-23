package src.analyzers;

import src.CodeGenerator;
import src.analyzers.semantic.PosfixConverter;
import src.analyzers.semantic.Symbol;
import src.analyzers.semantic.SymbolTable;
import src.analyzers.semantic.SymbolType;
import src.exceptions.CompilerException;
import src.exceptions.SemanticException;
import src.exceptions.SyntacticException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Syntactic {

    private static Token currentToken;

    private static boolean expectedEOF;


    public static List<Token> exp = new ArrayList<>();

    public static Token getCurrentToken() {
        return currentToken;
    }

    private static void nextToken() {
        currentToken = Lexical.nextToken();
        if (!expectedEOF && currentToken == null)
            throw new CompilerException("unexpected EOF");
    }

    public static void analyze(String filepath) throws IOException {
        Lexical.init(filepath);
        //rotulo
        nextToken();
        if (!currentToken.is(Token.SPROGRAMA)) {
            throw new SyntacticException("programa");
        }
        CodeGenerator.generateStart();
        nextToken();

        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }
        SymbolTable.insertSymbol(new Symbol(currentToken.lexeme, SymbolType.PROGRAMA));

        nextToken();
        if (!currentToken.is(Token.SPONTO_VIRGULA)) {
            throw new SyntacticException(";");
        }
        analyzeBlock();
        if (!currentToken.is(Token.SPONTO)) {
            throw new SyntacticException(".");
        }
        CodeGenerator.generateDalloc(0, 1);
        CodeGenerator.generateHalt();
        expectedEOF = true;
        nextToken();
        if (currentToken != null) {
            throw new SyntacticException();
        }
    }

    private static void analyzeBlock() {
        //todo rotulo
        nextToken();
        analyzeVariablesStep();
        analyzeSubRoutine();
        analyzeCommands();
        boolean shouldReturn = !SymbolTable.isMainScope();

        int deallocatedSize = SymbolTable.popUntilLocalScope();
        if (deallocatedSize != 0){
            Symbol.nextAvailableAddress -= deallocatedSize;

            CodeGenerator.generateDalloc((Symbol.nextAvailableAddress), deallocatedSize);
        }
        if(shouldReturn)
        {
            CodeGenerator.generateReturn();
        }

    }


    //FIXME Não sabemos se a implementação está correta (NAO ESTA!!!!)
    private static void analyzeFunctionCall(Symbol symbol) {
        if (!SymbolType.functions.contains(symbol.type)) {
            throw new CompilerException(String.format("expected function, found symbol '%s' (%s)", symbol.identifier, symbol.type));
        }
        exp.add(currentToken);

        nextToken();
        CodeGenerator.generateCall(symbol.address);
        CodeGenerator.generateLoadReturn();


    }

    private static void analyzeFunctionDeclaration() {
        int functionLabel = Symbol.nextAvailableLabel++;
        CodeGenerator.generateLabel(functionLabel);
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        Symbol symbol = SymbolTable.getSymbol(currentToken.lexeme);
        if (symbol != null) {
            throw SemanticException.functionDeclaredException(symbol.identifier, true);
        }
        Symbol insertedSymbol = SymbolTable.insertSymbol(currentToken, true);
        insertedSymbol.address = functionLabel;
        nextToken();
        if (!currentToken.is(Token.SDOISPONTOS)) {
            throw new SyntacticException(":");
        }
        nextToken();
        if (!currentToken.is(Token.SINTEIRO) && !currentToken.is(Token.SBOOLEANO)) {
            throw new SyntacticException("inteiro|booleano");
        }

        if (currentToken.is(Token.SINTEIRO)) {
            insertedSymbol.setType(SymbolType.FUNCAO_INTEIRO);
        } else if (currentToken.is(Token.SBOOLEANO)) {
            insertedSymbol.setType(SymbolType.FUNCAO_BOOLEANO);
        }

        nextToken();
        if (currentToken.is(Token.SPONTO_VIRGULA)) {
            analyzeBlock();
        }
    }

    private static void analyzeCommands() {
        if (!currentToken.is(Token.SINICIO)) {
            throw new SyntacticException("inicio");
        }
        nextToken();
        analyzeSimpleCommand();
        while (!currentToken.is(Token.SFIM)) {
            if (!currentToken.is(Token.SPONTO_VIRGULA)) {
                throw new SyntacticException(";");
            }
            nextToken();
            if (!currentToken.is(Token.SFIM)) {
                analyzeSimpleCommand();
            }
        }
        nextToken();
    }

    private static void analyzeSubRoutine() {
        boolean jumpped = false;
        int label = Symbol.nextAvailableLabel;
        while (currentToken.is(Token.SPROCEDIMENTO) || currentToken.is(Token.SFUNCAO)) {
          if(!jumpped)
          {
              CodeGenerator.generateJump(Symbol.nextAvailableLabel++);
              jumpped = true;
          }
            if (currentToken.is(Token.SPROCEDIMENTO)) {
                analyzeProcedureDeclaration();
            } else {
                analyzeFunctionDeclaration();
            }

            if (!currentToken.is(Token.SPONTO_VIRGULA)) {
                throw new SyntacticException(";");
            }

            nextToken();
        }
        if (jumpped) {
            CodeGenerator.generateLabel(label);
        }
    }

    private static void analyzeProcedureCall(Symbol symbol) {
        //TODO verificar se o simbolo atual é um procedimento, senao ERROOOO
        if (!symbol.equivalentTypeTo(SymbolType.PROCEDIMENTO)) {
            throw new CompilerException(String.format("expected procedure, found symbol '%s' (%s)", symbol.identifier, symbol.type));
        }
        CodeGenerator.generateCall(symbol.address);
    }

    private static void analyzeProcedureDeclaration() {

        nextToken();

        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        if (SymbolTable.hasProcedureDeclaration(currentToken.lexeme)) {
            throw SemanticException.procedureDeclaredException(currentToken.lexeme, true);
        }
        Symbol insertedSymbol = SymbolTable.insertSymbol(new Symbol(currentToken.lexeme, SymbolType.PROCEDIMENTO, true));
        insertedSymbol.address =  Symbol.nextAvailableLabel++;

        CodeGenerator.generateLabel( insertedSymbol.address);
        nextToken();
        if (!currentToken.is(Token.SPONTO_VIRGULA)) {
            throw new SyntacticException(";");
        }
        analyzeBlock();
    }


    private static void analyzeType() {
        if (!currentToken.is(Token.SINTEIRO) && !currentToken.is(Token.SBOOLEANO)) {
            throw new SyntacticException("inteiro|booleano");
        }

        SymbolTable.putVarTypesInTable(currentToken);
        nextToken();
    }

    private static void analyzeSimpleCommand() {
        if (currentToken.is(Token.SIDENTIFICADOR)) {
            analyzeAtribOrCallProc();
        } else if (currentToken.is(Token.SSE)) {
            analyzeIf();
        } else if (currentToken.is(Token.SENQUANTO)) {
            analyzeWhile();
        } else if (currentToken.is(Token.SLEIA)) {
            analyzeRead();
        } else if (currentToken.is(Token.SESCREVA)) {
            analyzeWrite();
        } else {
            analyzeCommands();
        }
    }

    private static void analyzeWhile() {
        int beginLabel = Symbol.nextAvailableLabel;
        CodeGenerator.generateLabel(Symbol.nextAvailableLabel++);

        nextToken();
        analyzeExpression();

        List<Symbol> postfixlist = PosfixConverter.infixToPostfix(exp);
        PosfixConverter.semantic(postfixlist);
        CodeGenerator.generateExpression(postfixlist);
        int outLabel = Symbol.nextAvailableLabel++;
        CodeGenerator.generateJumpF(outLabel);


        if (!currentToken.is(Token.SFACA)) {
            throw new SyntacticException("faca");
        }
        nextToken();
        analyzeSimpleCommand();
        CodeGenerator.generateJump(beginLabel);
        CodeGenerator.generateLabel(outLabel);

    }

    private static void analyzeExpression() {
        analyzeSimpleExpression();
        if (currentToken.is(Token.SMAIOR) || currentToken.is(Token.SMAIORIG) || currentToken.is(Token.SIG)
                || currentToken.is(Token.SMENOR) || currentToken.is(Token.SMENORIG)
                || currentToken.is(Token.SDIF)) {
            exp.add(currentToken);

            nextToken();


            analyzeSimpleExpression();
        }
    }

    private static void analyzeSimpleExpression() {
        if (currentToken.is(Token.SMAIS) || currentToken.is(Token.SMENOS)) {
            currentToken.lexeme = currentToken.is(Token.SMAIS) ? Token.SPOSITIVO : Token.SNEGATIVO;

            exp.add(currentToken);
            nextToken();
        }
        analyzeTerm();
        while (currentToken.is(Token.SMAIS) || currentToken.is(Token.SMENOS) || currentToken.is(Token.SOU)) {
            exp.add(currentToken);

            nextToken();


            analyzeTerm();
        }
    }

    private static void analyzeTerm() {
        analyzeFactor();
        while (currentToken.is(Token.SMULT) || currentToken.is(Token.SDIV) || currentToken.is(Token.SE)) {
            exp.add(currentToken);

            nextToken();
            analyzeFactor();
        }
    }

    private static void analyzeFactor() {
        if (currentToken.is(Token.SIDENTIFICADOR)) {
            int index = SymbolTable.searchSymbol(currentToken.lexeme, false);

            if (index == -1) {
                throw SemanticException.symbolDeclaredException("symbol", currentToken.lexeme, false);
            }
            Symbol symbol = SymbolTable.getSymbol(index);
            if (SymbolType.functions.contains(symbol.type)) {
                analyzeFunctionCall(symbol);
            } else if (SymbolType.variables.contains(symbol.type)) {
                exp.add(currentToken);
                nextToken();
            } else {
                nextToken();
            }
        } else if (currentToken.is(Token.SNUMERO)) {
            exp.add(currentToken);

            nextToken();
        } else if (currentToken.is(Token.SNAO)) {
            exp.add(currentToken);

            nextToken();
            analyzeFactor();
        } else if (currentToken.is(Token.SABRE_PARENTESES)) {
            exp.add(currentToken);

            nextToken();
            analyzeExpression();

            if (!currentToken.is(Token.SFECHA_PARENTESES)) {
                throw new SyntacticException(")");
            }
            exp.add(currentToken);

            nextToken();
        } else if (currentToken.is(Token.SVERDADEIRO) || currentToken.is(Token.SFALSO)) {
            exp.add(currentToken);
            nextToken();
        } else {
            throw new SyntacticException();
        }
    }


    private static void analyzeAttribution(Symbol symbol) {
        // por enquanto colocar analisa expressao mas nao é isso de fato o certo é a Analisa_atribuicao

        nextToken();
        analyzeExpression();

        List<Symbol> postfixlist = PosfixConverter.infixToPostfix(exp);
        SymbolType returnType = PosfixConverter.semantic(postfixlist);
        CodeGenerator.generateExpression(postfixlist);


        if (!symbol.equivalentTypeTo(returnType)) {
            throw SemanticException.incompatibleTypesException(symbol, returnType);
        }

        if (SymbolTable.isReturnVar(symbol.identifier)) {
            CodeGenerator.generateStoreFunction();
        }
        else{
            CodeGenerator.generateStore(symbol);
        }




    }


    private static void analyzeAtribOrCallProc() {
        String lexeme = currentToken.lexeme;
        Symbol symbol = SymbolTable.getSymbol(lexeme);

        nextToken();
        //Ler o token antes de dar o erro, para atualizar as coordenadas em caso de erro

        if(symbol == null) {
            throw SemanticException.symbolDeclaredException("symbol", lexeme, false);
        }

        if (currentToken.is(Token.SATRIBUICAO)) {
            analyzeAttribution(symbol);
        } else {
            analyzeProcedureCall(symbol);
        }
    }

    private static void analyzeRead() {
        nextToken();
        if (!currentToken.is(Token.SABRE_PARENTESES)) {
            throw new SyntacticException("(");
        }
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }


        if (!SymbolTable.hasVarDeclaration(currentToken.lexeme, false)) {
            throw SemanticException.variableDeclaredException(currentToken.lexeme, false);
        }

        Symbol symbol = SymbolTable.getSymbol(currentToken.lexeme);

        if (symbol == null) {
            throw new CompilerException("null symbol");
        }

        if (symbol.type != SymbolType.VARIAVEL_INTEIRO) {
            throw SemanticException.incompatibleTypesException(symbol, SymbolType.VARIAVEL_INTEIRO);
        }

        nextToken();
        if (!currentToken.is(Token.SFECHA_PARENTESES)) {
            throw new SyntacticException(")");
        }

        CodeGenerator.generateRead(symbol);

        nextToken();
    }

    private static void analyzeWrite() {
        nextToken();
        if (!currentToken.is(Token.SABRE_PARENTESES)) {
            throw new SyntacticException("(");
        }
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        if (!SymbolTable.hasVarOrFunctionDeclaration(currentToken.lexeme)) {
            throw SemanticException.symbolDeclaredException("symbol", currentToken.lexeme, false);
        }
        Symbol symbol = SymbolTable.getSymbol(currentToken.lexeme);

        nextToken();
        if (!currentToken.is(Token.SFECHA_PARENTESES)) {
            throw new SyntacticException(")");
        }

        CodeGenerator.generateWrite(symbol);

        nextToken();
    }

    private static void analyzeIf() {

        nextToken();
        analyzeExpression();

        List<Symbol>postfixlist  =  PosfixConverter.infixToPostfix(exp);
        PosfixConverter.semantic(postfixlist);
        CodeGenerator.generateExpression(postfixlist);

        if (!currentToken.is(Token.SENTAO)) {
            throw new SyntacticException("entao");
        }
        CodeGenerator.generateJumpF(Symbol.nextAvailableLabel);

        nextToken();
        analyzeSimpleCommand();
        if (currentToken.is(Token.SSENAO)) {
            CodeGenerator.generateJump(Symbol.nextAvailableLabel + 1);
            CodeGenerator.generateLabel(Symbol.nextAvailableLabel++);
            nextToken();
            analyzeSimpleCommand();
        }



        CodeGenerator.generateLabel(Symbol.nextAvailableLabel++);
    }


    private static void analyzeVariablesStep() {
        if (!currentToken.is(Token.SVAR)) {
            return;
        }

        nextToken();

        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }
        while (currentToken.is(Token.SIDENTIFICADOR)) {
            analyzeVariables();
            if (!currentToken.is(Token.SPONTO_VIRGULA)) {
                throw new SyntacticException(";");
            }
            nextToken();
        }
    }

    private static void analyzeVariables() {
        int variablesCount = 0;
        while (!currentToken.is(Token.SDOISPONTOS)) {
            if (!currentToken.is(Token.SIDENTIFICADOR)) {
                throw new SyntacticException();
            }

            if (SymbolTable.isReturnVar(currentToken.lexeme)) {
                throw SemanticException.unavailableVariable(currentToken.lexeme);
            }
            if(SymbolTable.hasVarDeclaration(currentToken.lexeme, true)) {
                throw SemanticException.variableDeclaredException(currentToken.lexeme, true);
            }
            SymbolTable.insertSymbol(currentToken, false);
            variablesCount++;

            nextToken();

            if (!currentToken.is(Token.SVIRGULA) && !currentToken.is(Token.SDOISPONTOS)) {
                throw new SyntacticException();
            }

            if (currentToken.is(Token.SVIRGULA)) {
                nextToken();
                if (currentToken.is(Token.SDOISPONTOS)) {
                    throw new SyntacticException(":");
                }
            }
        }
        nextToken();
        analyzeType();

        CodeGenerator.generateAlloc(Symbol.nextAvailableAddress, variablesCount);

        SymbolTable.putVarAddresses(variablesCount);

    }

}
