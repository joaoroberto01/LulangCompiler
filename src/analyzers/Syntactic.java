package src.analyzers;

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
        expectedEOF = true;
        nextToken();
        if (currentToken != null) {
            throw new SyntacticException();
        }

        //sucesso
        System.out.println("AUUUUUUUUUUUUUUUUUUUU");
    }

    private static void analyzeBlock() {
        nextToken();
        analyzeVariablesStep();
        analyzeSubRoutine();
        analyzeCommands();
    }


    //FIXME Não sabemos se a implementação está correta (NAO ESTA!!!!)
    private static void analyzeFunctionCall() {
        exp.add(currentToken);

        nextToken();
    }

    private static void analyzeFunctionDeclaration() {
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        Symbol symbol = SymbolTable.getSymbol(currentToken.lexeme);
        if (symbol != null) {
            throw SemanticException.functionDeclaredException(symbol.identifier, true);
        }
        Symbol insertedSymbol = SymbolTable.insertSymbol(currentToken, true);

        nextToken();
        if (!currentToken.is(Token.SDOISPONTOS)) {
            throw new SyntacticException(":");
        }
        nextToken();
        if (!currentToken.is(Token.SINTEIRO) && !currentToken.is(Token.SBOOLEANO)) {
            throw new SyntacticException("tipo");
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

        SymbolTable.popUntilLocalScope();
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
        while (currentToken.is(Token.SPROCEDIMENTO) || currentToken.is(Token.SFUNCAO)) {
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
    }

    private static void analyzeProcedureCall(Symbol symbol) {
        //TODO verificar se o simbolo atual é um procedimento, senao ERROOOO
        if (!symbol.equivalentTypeTo(SymbolType.PROCEDIMENTO)) {
            throw new CompilerException(String.format("expected procedure, found symbol '%s' (%s)", symbol.identifier, symbol.type));
        }
    }

    private static void analyzeProcedureDeclaration() {
        nextToken();

        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        if (SymbolTable.hasProcedureDeclaration(currentToken.lexeme)) {
            throw SemanticException.procedureDeclaredException(currentToken.lexeme, true);
        }
        SymbolTable.insertSymbol(new Symbol(currentToken.lexeme, SymbolType.PROCEDIMENTO, true));

        nextToken();
        if (!currentToken.is(Token.SPONTO_VIRGULA)) {
            throw new SyntacticException(";");
        }
        analyzeBlock();

        SymbolTable.popUntilLocalScope();

    }


    private static void analyzeType() {
        if (!currentToken.is(Token.SINTEIRO) && !currentToken.is(Token.SBOOLEANO)) {
            throw new SyntacticException("tipo");
        }

        SymbolTable.putTypeTable(currentToken);
        nextToken();
    }

    private static void analyzeSimpleCommand() {
        if (currentToken.is(Token.SIDENTIFICADOR)) {
            analyzeAtribCallProc();
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
        nextToken();
        analyzeExpression();

        List<Token> postfixlist = PosfixConverter.infixToPostfix(exp);//TODO converter(inf, pos_fixa)
        PosfixConverter.semantic(postfixlist);

        if (!currentToken.is(Token.SFACA)) {
            throw new SyntacticException("faca");
        }
        nextToken();
        analyzeSimpleCommand();
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
            SymbolType type = SymbolTable.getSymbol(index).getType();
            if (SymbolType.functions.contains(type)) {
                analyzeFunctionCall();
            } else if (SymbolType.variables.contains(type)) {
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

        List<Token> postfixlist = PosfixConverter.infixToPostfix(exp);
        SymbolType returnType = PosfixConverter.semantic(postfixlist);

        if (!symbol.equivalentTypeTo(returnType)) {
            throw SemanticException.incompatibleTypesException(symbol, returnType);
        }
    }


    private static void analyzeAtribCallProc() {
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

        if (!(SymbolTable.hasVarDeclaration(currentToken.lexeme, false) ||
                SymbolTable.isReturnVar(currentToken.lexeme))) {
            throw SemanticException.variableDeclaredException(currentToken.lexeme, false);
        }

        nextToken();
        if (!currentToken.is(Token.SFECHA_PARENTESES)) {
            throw new SyntacticException(")");
        }
        nextToken();
    }

    private static void analyzeWrite() {
        nextToken();
        if (!currentToken.is(Token.SABRE_PARENTESES)) {
            throw new SyntacticException();
        }
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }

        if (!SymbolTable.hasVarOrFunctionDeclaration(currentToken.lexeme)) {
            throw SemanticException.symbolDeclaredException("symbol", currentToken.lexeme, false);
        }
        nextToken();
        if (!currentToken.is(Token.SFECHA_PARENTESES)) {
            throw new SyntacticException(")");
        }
        nextToken();
    }

    private static void analyzeIf() {
        nextToken();
        analyzeExpression();

        List<Token> postfixlist  =  PosfixConverter.infixToPostfix(exp);
        PosfixConverter.semantic(postfixlist);
        if (!currentToken.is(Token.SENTAO)) {
            throw new SyntacticException("entao");
        }

        nextToken();
        analyzeSimpleCommand();
        if (currentToken.is(Token.SSENAO)) {
            nextToken();
            analyzeSimpleCommand();
        }
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
    }

}
