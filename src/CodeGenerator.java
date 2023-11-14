package src;

import src.analyzers.Token;
import src.analyzers.semantic.Symbol;
import src.analyzers.semantic.SymbolType;

import java.util.List;
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

           case Token.SLEIA:
               generate("RD", "","","");
               break;
           case Token.SESCREVA:
               generate("PRN", "","","");
               break;

           case Token.SPROGRAMA:
               generate("START", "","","");
               break;
           case Token.SPONTO:
               generate("HLT", "","","");
               break;
           case Token.SATRIBUICAO:
               generate("STR",  String.valueOf(symbol.address),"","");
               break;
           default:
               if (token.lexeme.equals(Token.SNEGATIVO))
               {
                   generate("INV", "","","");
               }
               break;
       }
   }

    public static  void  generateJMP(int label){
        generate("JMP", "L" + label,"","");
    }
    public static  void  generateCall(int label){
        generate("CALL", "L" + label,"","");
    }
    public static  void  generateCallF(int label){
        generate("JMPF", "L" + label,"","");
    }
    public static  void  generateALLOC(int address,int size){
        generate("ALLOC", String.valueOf(address),String.valueOf(size),"");
    }
    public static  void  generateDALLOC(int address,int size){
        generate("ALLOC", String.valueOf(address),String.valueOf(size),"");
    }
    public static  void  generateExpression(List<Symbol> symbols){
        for (Symbol symbol : symbols) {
            generateTerm(symbol);
        }
    }

    private static void generateTerm(Symbol symbol) {
        if (symbol.type == null) {
            switch (symbol.identifier) {
                case "+":
                    generate("ADD", "", "", "");
                    break;
                case "-":
                    generate("SUB", "", "", "");
                    break;
                case "div":
                    generate("DIVI", "", "", "");
                    break;
                case "*":
                    generate("MULT", "", "", "");
                    break;
                case "e":
                    generate("AND", "","","");
                    break;
                case "ou":
                    generate("OR", "","","");
                    break;
                case "nao":
                    generate("NEG", "","","");
                    break;
                case "<":
                    generate("CME", "","","");
                    break;
                case ">":
                    generate("CMA", "","","");
                    break;
                case "=":
                    generate("CEQ", "","","");
                    break;
                case "!=":
                    generate("CDIF", "","","");
                    break;
                case "<=":
                    generate("CMEQ", "","","");
                    break;
                case ">=":
                    generate("CMAQ", "","","");
                    break;
                case Token.SNEGATIVO:
                    generate("INV", "", "", "");
                    break;
            }
            return;
        }
        //identificador
        if (SymbolType.variables.contains(symbol.type)) {
            generate("LDV", String.valueOf(symbol.address), "", "");
        } else if (SymbolType.functions.contains(symbol.type)) {
            generate("CALL", String.valueOf(symbol.address), "", "");

        }
    }


    private static void generate(String instruction, String attribute1, String attribute2, String label) {
       String objInstruction = String.format("%4s%8s%4s%4s\n", label, instruction, attribute1, attribute2);
       codeBuilder.append(objInstruction);
   }
}
