import java.awt.image.WritableRenderedImage;

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
        analyzeSubRoutine();
        analyzeCommands();

    }
    private static void analyzeDecFunction() {
                read();
                if (!currentToken.is("sidentificador"))
                {
                    //ERRO
                    return;
                }
                read();
                if (!currentToken.is("sdoispontos"))
                {
                    //ERRO
                    return;
                }
                read();
                 if (currentToken.is("sinteiro") || currentToken.is("sbooleano"))
                 {

                     read();
                     if (currentToken.is("sponto_virgula"))
                     {
                         analyzeBlock();
                     }

                 }
                 else
                 {
                     //erro
                     return;
                 }


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
    private static void analyzeSubRoutine() {
        while (currentToken.is("sprocedimento") || currentToken.is("sfunção"))
        {
            if (currentToken.is("sprocedimento"))
            {
                //analisa_declaracao_procedimento
                analyzeDecProc();
            }
            else {
                //analisa_ declaração_função
                analyzeDecFunction();
            }

            if(currentToken.is("sponto-vírgula"))
            {
                read();
            }
            else {
                //erro
                return;
            }



        }
    }

    private static void analyzeDecProc() {
        read();
        if(!currentToken.is("sidentificador"))
        {
            //erro
            return;
        }
        read();
        if(!currentToken.is("sponto_vírgula"))
        {
            //erro
            return;
        }
        analyzeBlock();

    }


    private static void analyzeType() {

            if (currentToken.is("sinteiro") && currentToken.is("sbooleano"))
            {
                //colocatipotabela
                read();
            }
            else
            {
                //erro
                return;
            }
    }


        private static void analyzeSimpleCommand() {
        if (currentToken.is("sidentificador"))
        {
            analyzeAtribCallProc();
        }
        else if(currentToken.is("sse")){
           analyzeIf();
        }
        else if(currentToken.is("senquanto")){
            analyzeWhile();
        }
        else if(currentToken.is("sleia")){
            analyzeRead();
        }
        else if(currentToken.is("sescreva")){
            analyzeWrite();
        }
        else
        {
            analyzeComand();
        }
    }

    private static void analyzeWhile() {
        read();
        analyzeExpression();
        if (!currentToken.is("sfaca"))
        {
            //erro
            return;
        }
        read();
        //analisa_comando_simples


    }

    private static void analyzeExpression() {

        analyzeSimpleExpression();
        if(       currentToken.is("smaior")
                ||currentToken.is("smaiorig")
                ||currentToken.is("sig")
                ||currentToken.is("smenor")
                ||currentToken.is("smenorig")
                ||currentToken.is("sdif"))
        {
            read();
            analyzeSimpleExpression();
        }
    }
    private static void analyzeSimpleExpression() {
        if(       currentToken.is("smais")
                ||currentToken.is("smenos"))
        {
            read();
            analyzeTerm();
            while (currentToken.is("smais")
                    ||currentToken.is("smenos")||currentToken.is("sou"))
            {
                read();
                analyzeTerm();
            }
        }
    }

    private static void analyzeTerm() {
        analyzeFactor();
        while (currentToken.is("smult")
                ||currentToken.is("sdiv")||currentToken.is("sse"))
        {
            read();
            analyzeFactor();
        }

    }
    private static void analyzeFactor() {
        if (currentToken.is("sidentificador"))
        {
            // analisa_chamada_funcao
        }
        else {
            if(currentToken.is("snumero"))
            {
            read();
            }
            else
            {
                if(currentToken.is("snao"))
                {
                    read();
                    analyzeFactor();
                }
                else
                {
                    //nao entendi
                    if(currentToken.is("sabre_parenteses"))
                    {
                        read();
                        //analisaExpresao
                        if(!currentToken.is("sfecha_parenteses"))
                        {
                            //erro
                            return;
                        }
                        read();


                    }
                    else
                    {
                        if(currentToken.isLexema("verdadeiro") || currentToken.isLexema("falso"))
                        {
                            read();
                        }
                        else
                        {
                            //erro
                            return;
                        }
                    }

                }

            }
        }
    }

        private static void analyzeComand() {

        if(!currentToken.is("sinicio"))
        {
            //erro
            return;
        }

        read();
        // analisa_comando_simples
        while (currentToken.is("sfim"))
        {
            if(!currentToken.is("spontovirgula"))
            {
                //erro
                return;
            }
            read();
            if(!currentToken.is("sfim"))
            {
                //analisa comando simples
            }
            read();
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
        read();
        //analisaExpressao
        if (!currentToken.is("sentao")) {
            // erro
            return;
        }

        read();
        //analisa comando simples
        if (currentToken.is("ssenão")) {

            read();
            // ANALISA_COMANDO_SIMPLESS
        }



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


}