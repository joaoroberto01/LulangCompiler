import java.util.ArrayList;
import java.util.List;

public class Syntactic {

    private static Token currentToken;

    private static boolean expectedEOF;

    public static List<String> exp = new ArrayList<>();


    private static void nextToken() {
        currentToken = Lexical.nextToken();
        if (!expectedEOF && currentToken == null)
            throw new CompilerException("unexpected EOF");
    }

    public static void analyze() {
        Lexical.init("sint1.txt");
        //rotulo
        nextToken();
        if (!currentToken.is(Token.SPROGRAMA)) {
            throw new SyntacticException("programa");
        }
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }
        SymbolTable.insertSymbol(new Symbol(currentToken.lexeme, SymbolType.NOMEDEPROGRAMA));

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
        exp.add(currentToken.lexeme);

        nextToken();
    }

    private static void analyzeFunctionDeclaration() {
        nextToken();
        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }


        if(SymbolTable.searchDeclarationFuncTable(currentToken.lexeme))
        {
            throw new CompilerException("function already declared");
        }
        Symbol insertedSymbol = SymbolTable.insertSymbol(currentToken);

        nextToken();
        if (!currentToken.is(Token.SDOISPONTOS)) {
            throw new SyntacticException(":");
        }
        nextToken();
        if (!currentToken.is(Token.SINTEIRO) && !currentToken.is(Token.SBOOLEANO)) {
            throw new SyntacticException("tipo");
        }

        if (currentToken.is(Token.SINTEIRO)) {
            insertedSymbol.setType(SymbolType.FUNCAOINTEIRO);
        } else if (currentToken.is(Token.SBOOLEANO)) {
            insertedSymbol.setType(SymbolType.FUNCAOBOOLEANO);
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

    //FIXME Não sabemos se a implementação está correta (NAO ESTA!!!!)
    private static void analyzeProcedureCall() {

    }

    private static void analyzeProcedureDeclaration() {


        nextToken();

        if (!currentToken.is(Token.SIDENTIFICADOR)) {
            throw new SyntacticException();
        }


        if (SymbolTable.searchDeclarationProcTable(currentToken.lexeme)) {
            throw new CompilerException("procedure already declared");
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
        //TODO ?
        PosfixConverter.infixToPostfix(exp);//TODO converter(inf, pos_fixa)

        //TODO semantica(pos_fixa)

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
            exp.add(currentToken.lexeme);

            nextToken();


            analyzeSimpleExpression();
        }
    }

    private static void analyzeSimpleExpression() {

        if (currentToken.is(Token.SMAIS) || currentToken.is(Token.SMENOS)) {
            exp.add(currentToken.lexeme);
            nextToken();
        }
        analyzeTerm();
        while (currentToken.is(Token.SMAIS) || currentToken.is(Token.SMENOS) || currentToken.is(Token.SOU)) {
            exp.add(currentToken.lexeme);

            nextToken();


            analyzeTerm();
        }
    }

    private static void analyzeTerm() {
        analyzeFactor();
        while (currentToken.is(Token.SMULT) || currentToken.is(Token.SDIV) || currentToken.is(Token.SE)) {
            exp.add(currentToken.lexeme);
            nextToken();
            analyzeFactor();
        }
    }

    private static void analyzeFactor() {
        if (currentToken.is(Token.SIDENTIFICADOR)) {

            int index = SymbolTable.searchTable(currentToken.lexeme);

            if (index == -1) {
                throw new CompilerException("function " + currentToken.lexeme + " not declared");
            }
            SymbolType type = SymbolTable.getSymbol(index).getType();
            if (type.equals(SymbolType.FUNCAOINTEIRO)
                    || type.equals(SymbolType.FUNCAOBOOLEANO)) {
                analyzeFunctionCall();

            } else {
                nextToken();
            }


            //TODO sera que a função abaixo abrange a regra <variavel> do nao terminal <fator> ? aguarde os proximos capitulos...


        } else if (currentToken.is(Token.SNUMERO)) {
            exp.add(currentToken.lexeme);

            nextToken();

        } else if (currentToken.is(Token.SNAO)) {
            exp.add(currentToken.lexeme);

            nextToken();


            analyzeFactor();
        } else if (currentToken.is(Token.SABRE_PARENTESES)) {
            exp.add(currentToken.lexeme);

            nextToken();


            analyzeExpression();

            if (!currentToken.is(Token.SFECHA_PARENTESES)) {
                throw new SyntacticException(")");
            }
            exp.add(currentToken.lexeme);
            //TODO ?
            //PosfixConverter.infixToPostfix(exp);//TODO converter(inf, pos_fixa)
            //TODO semantica(pos_fixa)
            nextToken();


        } else if (currentToken.is(Token.SVERDADEIRO) || currentToken.is(Token.SFALSO)) {
            exp.add(currentToken.lexeme);
            nextToken();


        } else {
            throw new SyntacticException();
        }
    }


    private static void analyzeAttribution() {
        // por enquanto colocar analisa expressao mas nao é isso de fato o certo é a Analisa_atribuicao
        nextToken();
        analyzeExpression();
        //TODO ?
        PosfixConverter.infixToPostfix(exp);//TODO converter(inf, pos_fixa)
        //TODO semantica(pos_fixa)

    }


    private static void analyzeAtribCallProc() {
        String lexeme = currentToken.lexeme;

        nextToken();
        if(SymbolTable.searchTable(lexeme) == -1)
        {
            throw  new CompilerException("symbol not declared");
        }

        if (currentToken.is(Token.SATRIBUICAO)) {
            analyzeAttribution();
        } else {
            analyzeProcedureCall();
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

        if (!SymbolTable.searchDeclarationVarTable(currentToken.lexeme)) {
            throw new CompilerException("variable not found");
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

        if (!SymbolTable.searchDeclarationVarOrFuncTable(currentToken.lexeme)) {
            throw new CompilerException("variable or function not found ");
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
        //TODO ?
        PosfixConverter.infixToPostfix(exp);//TODO converter(inf, pos_fixa)
        //TODO semantica(pos_fixa)
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

            if(SymbolTable.searchDuplicityVarTable(currentToken.lexeme))
            {
                throw new CompilerException("variable already declared");
            }
            SymbolTable.insertSymbol(currentToken);

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
