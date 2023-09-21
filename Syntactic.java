public class Syntactic {

    private static Token currentToken;

    private static void nextToken() {
        currentToken = Lexical.nextToken();
    }

    public static void analyze() {
        Lexical.init();
        //rotulo
        System.out.println("inicio");
        nextToken();
        if (!currentToken.is("sprograma")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        //insere_tabela
        nextToken();
        if (!currentToken.is("sponto_virgula")) {
            System.out.println("erro!");
            return;
        }
        analyzeBlock();
        if (!currentToken.is("sponto")) {
            System.out.println("erro!");
            return;
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

    private static void analyzeFunctionDeclaration() {
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sdoispontos")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sinteiro") && !currentToken.is("sbooleano")) {
            System.out.println("erro!");
            return;
        }

        nextToken();
        if (currentToken.is("sponto_virgula")) {
            analyzeBlock();
        }
    }

    private static void analyzeCommands() {
        if (!currentToken.is("sinicio")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        analyzeSimpleCommand();
        while (!currentToken.is("sfim")) {
            if (!currentToken.is("sponto_virgula")) {
                System.out.println("erro!");
                return;
            }
            nextToken();
            if (!currentToken.is("sfim")) {
                analyzeSimpleCommand();
            }
        }
        nextToken();
    }

    private static void analyzeSubRoutine() {
        while (currentToken.is("sprocedimento") || currentToken.is("sfuncao")) {
            if (currentToken.is("sprocedimento")) {
                analyzeProcedureDeclaration();
            } else {
               analyzeFunctionDeclaration();
            }

            if (!currentToken.is("sponto_virgula")) {
                System.out.println("erro!");
                return;
            }

            nextToken();
        }
    }

    private static void analyzeProcedureDeclaration() {
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sponto_virgula")) {
            System.out.println("erro!");
            return;
        }
        analyzeBlock();
    }


    private static void analyzeType() {
        if (!currentToken.is("sinteiro") && !currentToken.is("sbooleano")) {
            System.out.println("erro!");
            return;
        }

        //colocatipotabela
        nextToken();
    }

    private static void analyzeSimpleCommand() {
        if (currentToken.is("sidentificador")) {
            analyzeAtribCallProc();
        } else if (currentToken.is("sse")) {
            analyzeIf();
        } else if (currentToken.is("senquanto")) {
            analyzeWhile();
        } else if (currentToken.is("sleia")) {
            analyzeRead();
        } else if (currentToken.is("sescreva")) {
            analyzeWrite();
        } else {
            analyzeCommands();
        }
    }

    private static void analyzeWhile() {
        nextToken();
        analyzeExpression();
        if (!currentToken.is("sfaca")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        analyzeSimpleCommand();
    }

    private static void analyzeExpression() {
        analyzeSimpleExpression();
        if (currentToken.is("smaior")
                || currentToken.is("smaiorig")
                || currentToken.is("sig")
                || currentToken.is("smenor")
                || currentToken.is("smenorig")
                || currentToken.is("sdif")) {
            nextToken();
            analyzeSimpleExpression();
        }
    }

    private static void analyzeSimpleExpression() {
        if (currentToken.is("smais") || currentToken.is("smenos")) {
            nextToken();
        }
        analyzeTerm();
        while (currentToken.is("smais") || currentToken.is("smenos") || currentToken.is("sou")) {
            nextToken();
            analyzeTerm();
        }
    }

    private static void analyzeTerm() {
        analyzeFactor();
        while (currentToken.is("smult") || currentToken.is("sdiv") || currentToken.is("sse")) {
            nextToken();
            analyzeFactor();
        }
    }

    private static void analyzeFactor() {
        if (currentToken.is("sidentificador")) {
            //TODO if pesquisa tabela
            analyzeFunctionCall();
        } else if (currentToken.is("snumero")) {
            nextToken();
        } else if (currentToken.is("snao")) {
            nextToken();
            analyzeFactor();
        } else if (currentToken.is("sabre_parenteses")) {
            nextToken();
            analyzeExpression();
            if (!currentToken.is("sfecha_parenteses")) {
                System.out.println("erro!");
                return;
            }
            nextToken();
        } else if (currentToken.isLexema("verdadeiro") || currentToken.isLexema("falso")) {
            nextToken();
        } else {
            System.out.println("erro!");
            return;
        }
    }

    //TODO JOAO GAMEPLAYS FEZ ISSO. Fonte: Times New Roman 12
    private static void analyzeFunctionCall() {
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
    }

    private static void analyzeAttribution() {
        // por enquanto colocar analisa expressao mas nao é isso de fato o certo é a Analisa_atribuicao
        nextToken();
        analyzeExpression();

    }


    private static void analyzeAtribCallProc() {
        nextToken();
        if (currentToken.is("satribuicao")) {
            analyzeAttribution();
        } else {
            //TODO Chamada_procedimento
        }
    }

    private static void analyzeRead() {
        nextToken();
        if (!currentToken.is("sabre_parenteses")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        //if pesquisa

        nextToken();
        if (!currentToken.is("sfecha_parenteses")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
    }

    private static void analyzeWrite() {
        nextToken();
        if (!currentToken.is("sabre_parenteses")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }
        //TODO se pesquisa_ declvarfunc_tabela(token.lexema

        nextToken();
        if (!currentToken.is("sfecha_parenteses")) {
            System.out.println("erro!");
            return;

        }
        nextToken();

        //else erro

    }

    private static void analyzeIf() {
        nextToken();
        analyzeExpression();
        if (!currentToken.is("sentao")) {
            System.out.println("erro!");
            return;
        }

        nextToken();
        analyzeSimpleCommand();
        if (currentToken.is("ssenão")) {
            nextToken();
            analyzeSimpleCommand();
        }
    }


    private static void analyzeVariablesStep() {
        if (!currentToken.is("svar")) {
            System.out.println("erro!");
            return;
        }
        nextToken();
        if (!currentToken.is("sidentificador")) {
            System.out.println("erro!");
            return;
        }

        while (currentToken.is("sidentificador")) {
            analyzeVariables();
            if (!currentToken.is("sponto_virgula")) {
                System.out.println("erro!");
                return;
            }
            nextToken();
        }
    }

    private static void analyzeVariables() {
        while (!currentToken.is("sdoispontos")) {
            if (!currentToken.is("sidentificador")) {
                System.out.println("erro!");
                return;
            }

            nextToken();

            if (!currentToken.is("svirgula") && !currentToken.is("sdoispontos")) {
                System.out.println("erro!");
                return;
            }

            if (currentToken.is("svirgula")) {
                nextToken();
                if (currentToken.is("sdoispontos")) {
                    System.out.println("erro!");
                    return;
                }
            }
        }
        nextToken();
        analyzeType();
    }

}