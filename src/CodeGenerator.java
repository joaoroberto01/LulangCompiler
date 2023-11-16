package src;

import src.analyzers.Token;
import src.analyzers.semantic.Symbol;
import src.analyzers.semantic.SymbolType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CodeGenerator {
    public static StringBuilder codeBuilder = new StringBuilder();


    public static void saveFile(String filepath) throws IOException {
        String[] parts = filepath.split("\\.");
        if (parts.length == 0) return;
        filepath = parts[0].concat(".obj");

        if (codeBuilder.length() == 0) return;

        Path path = Paths.get(filepath);
        Files.writeString(path, codeBuilder.toString());
    }

    public static void generate(Token token) {
        switch (token.symbol) {
            case Token.SNUMERO:
                appendCode("LDC", token.lexeme, "", "");
                break;
            case Token.SVERDADEIRO:
                appendCode("LDC", "1", "", "");
                break;
            case Token.SFALSO:
                appendCode("LDC", "0", "", "");
                break;
            case Token.SPROGRAMA:
                appendCode("START", "", "", "");
                break;
            case Token.SPONTO:
                appendCode("HLT", "", "", "");
                break;
            default:
                break;
        }
    }

    public static void generateJMP(int label) {
        appendCode("JMP",   ""+label , "", "");
    }

    public static void generateCall(int label) {
        appendCode("CALL", "" + label, "", "");
    }

    public static void generateJMPF(int label) {
        appendCode("JMPF", "" + label, "", "");
    }

    public static void generateALLOC(int address, int size) {
        appendCode("ALLOC", String.valueOf(address), String.valueOf(size), "");
    }

    public static void generateDALLOC(int address, int size) {
        appendCode("DALLOC", String.valueOf(address), String.valueOf(size), "");
    }

    public static void generateStore(Symbol symbol) {
        appendCode("STR", String.valueOf(symbol.address), "", "");
    }

    public static void generateRead(Symbol symbol) {
        appendCode("RD", "", "", "");
        generateStore(symbol);
    }

    public static void generateLabel(int label) {
        appendCode("NULL", "", "", label + "");

    }

    public static void generateWrite(Symbol symbol) {
        appendCode("LDV", String.valueOf(symbol.address), "", "");
        appendCode("PRN", "", "", "");
    }

    public static void generateExpression(List<Symbol> symbols) {
        for (Symbol symbol : symbols) {
            generateTerm(symbol);
        }
    }

    private static void generateTerm(Symbol symbol) {
        if (symbol.type == null) {
            switch (symbol.identifier) {
                case "+":
                    appendCode("ADD", "", "", "");
                    break;
                case "-":
                    appendCode("SUB", "", "", "");
                    break;
                case "div":
                    appendCode("DIVI", "", "", "");
                    break;
                case "*":
                    appendCode("MULT", "", "", "");
                    break;
                case "e":
                    appendCode("AND", "", "", "");
                    break;
                case "ou":
                    appendCode("OR", "", "", "");
                    break;
                case "nao":
                    appendCode("NEG", "", "", "");
                    break;
                case "<":
                    appendCode("CME", "", "", "");
                    break;
                case ">":
                    appendCode("CMA", "", "", "");
                    break;
                case "=":
                    appendCode("CEQ", "", "", "");
                    break;
                case "!=":
                    appendCode("CDIF", "", "", "");
                    break;
                case "<=":
                    appendCode("CMEQ", "", "", "");
                    break;
                case ">=":
                    appendCode("CMAQ", "", "", "");
                    break;
                case Token.SNEGATIVO:
                    appendCode("INV", "", "", "");
                    break;
            }
            return;
        }

        //identificador ou numero
        if (symbol.identifier.matches("\\d+")) {
            appendCode("LDC", symbol.identifier, "", "");
        } else if (SymbolType.variables.contains(symbol.type)) {
            appendCode("LDV", String.valueOf(symbol.address), "", "");
        } else if (SymbolType.functions.contains(symbol.type)) {
            appendCode("CALL", String.valueOf(symbol.address), "", "");

        }
    }


    private static void appendCode(String instruction, String attribute1, String attribute2, String label) {
        String objInstruction = String.format("%-4s%-8s%-4s%-4s\n", label, instruction, attribute1, attribute2);
        codeBuilder.append(objInstruction);
    }


}
