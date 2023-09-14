public class Syntactic {

    private static Token currentToken;

    private static void read() {
        currentToken = Lexical.nextToken();
    }


    public static void analyze() {
        //rotulo

        read();
        if (!currentToken.is("sprograma")) {
            // erro
            return;
        }
        read();
        if (!currentToken.is("sidentificador")) {
            // erro
            return;
        }
        //insere_tabela
        read();
        if (!currentToken.is("spontovirgula")) {
            // erro
            return;
        }
        analyzeBlock();
        if (!currentToken.is("sponto")) {
            // erro
            return;
        }
        //sucesso
        return;


    }

    private static void analyzeBlock() {
        read();
        analyzeVariablesStep();
        // ANALISA SUBROTINAS
        analyzeCommands();

    }

    private static void analyzeCommands() {
        if (!currentToken.is("sinicio")) {
         //ERRO
            return;
        }
        analyzeSimpleCommand() ;
        while (!currentToken.is("sfim"))
        {
            if (!currentToken.is("spontovirgula")) {
                //ERRO
                return;
            }
            read();
            if (!currentToken.is("sfim"))
            {
                analyzeSimpleCommand();
            }
        }
        read();
    }

    private static void analyzeSimpleCommand() {
        if (currentToken.is("sidentificador"))
        {
            analyzeAtribCallProc();
        }
        else if(currentToken.is("sse")){
            //Analisa_se
        }
        else if(currentToken.is("senquanto")){
//Analisa_enquanto
        }
        else if(currentToken.is("sleia")){
            analyzeRead();
        }
        else if(currentToken.is("sescreva")){
//Analisa_ escreva
        }
        else
        {
//Analisa_comandos
        }
    }

    private static void analyzeAtribCallProc() {
            read();
        if(currentToken.is("satribuição"))
        {
        //Analisa_atribuicao
        }
        else
        {
            //Chamada_procedimento
        }
    }

    private static void analyzeRead() {
    read();
        if(
                !currentToken.is("sabre_parenteses"))
        {
            //erro
            return;
        }
        read();
        if(
                !currentToken.is("sidentificador"))
        {
            //erro
            return;
        }
        //if pesquisa

        read();
        if(
                !currentToken.is("sfecha_parent"))
        {
            //erro
            return;
        }
        read();
    }

    private static void analyzeWrite() {
        read();
        if (!currentToken.is("sabre_parenteses")) {
//erro
            return;
        }
        read();
        if (!currentToken.is("sidentificador")){
            //erro
            return;
        }
        //se pesquisa_ declvarfunc_tabela(token.lexema

        read();
        if (!currentToken.is("sfecha_parenteses")) {
            //erro
            return;

        }
        read();

        //else erro

    }
    private static void analyzeIf() {

    }


        private static void analyzeVariablesStep() {
        if (!currentToken.is("svar")) {
            // erro
            return;
        }
        read();
        if (!currentToken.is("sidentificador")) {
            // erro
            return;
        }
        while (currentToken.is("sidentificador")) {
            analyzeVariables();
            if (!currentToken.is("spontvirg")) {
                //erro
                return;
            }
            read();
        }

    }

    private static void analyzeVariables() {
        while (!currentToken.is("sdoispontos")) {
            if (!currentToken.is("sidentificador")) {
                //ERRO
                return;
            }
            //Pesquisar_blabla

            read();

            if (!currentToken.is("Svírgula") && !currentToken.is("Sdoispontos")) {
                //ERRO
                return;
            }
            if (currentToken.is("Svírgula")) {
                read();
                if (currentToken.is("Sdoispontos")) {
                    //erro
                    return;
                }
            }
        }
        read();
        analyzeType();
    }

    private static void analyzeType() {

        if (!currentToken.is("sinteiro") && !currentToken.is("sbooleano"))
        {
            //erro
            return;
        }
        //coloca tipo tabela
        read();
    }
}