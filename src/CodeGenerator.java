package src;

import src.analyzers.Token;
import src.analyzers.semantic.Symbol;
import src.analyzers.semantic.SymbolType;

import java.util.Stack;

public class CodeGenerator {
   public static StringBuilder codeBuilder = new StringBuilder();


   public static void generate(Token token) {
       generate(token, null);
   }

   public static  void  generate(Token token, Symbol symbol){
       switch (token.symbol){
           case Token.SNUMERO:
              generate("LDC", token.lexeme,"","");
               break;
           case Token.SVERDADEIRO:
               generate("LDC", "1","","");
               break;
           case Token.SFALSO:
               generate("LDC", "0","","");
               break;
           case Token.SIDENTIFICADOR:
               if (symbol.type == SymbolType.VARIAVEL_BOOLEANO || symbol.type == SymbolType.VARIAVEL_INTEIRO) {
                   generate("LDV", String.valueOf(symbol.address), "", "");
               }
               else if(symbol.type == SymbolType.PROCEDIMENTO || symbol.type == SymbolType.FUNCAO_BOOLEANO || symbol.type == SymbolType.FUNCAO_INTEIRO)
               {
                   generate("CALL", String.valueOf(symbol.address), "", "");
               }
               break;
           case Token.SMAIS:
               generate("ADD", "","","");
               break;
           case Token.SMENOS:
               generate("SUB", "","","");
               break;
           case Token.SMULT:
               generate("MULT", "","","");
               break;
           case Token.SDIV:
               generate("DIVI", "","","");
               break;
           case Token.SE:
               generate("AND", "","","");
               break;
           case Token.SOU:
               generate("OR", "","","");
               break;
           case Token.SNAO:
               generate("NEG", "","","");
               break;
           case Token.SMENOR:
               generate("CME", "","","");
               break;
           case Token.SMAIOR:
               generate("CMA", "","","");
               break;
           case Token.SIG:
               generate("CEQ", "","","");
               break;
           case Token.SDIF:
               generate("CDIF", "","","");
               break;
           case Token.SMENORIG:
               generate("CMEQ", "","","");
               break;
           case Token.SMAIORIG:
               generate("CMAQ", "","","");
               break;
           case Token.SPROGRAMA:
               generate("START", "","","");
               break;
           default:
               if (token.lexeme.equals(Token.SNEGATIVO))
               {
                   generate("INV", "","","");
               }
               break;
       }
   }

   private static void generate(String instruction, String attribute1, String attribute2, String label) {
       String objInstruction = String.format("%4s%8s%4s%4s\n", label, instruction, attribute1, attribute2);
       codeBuilder.append(objInstruction);
   }
}
