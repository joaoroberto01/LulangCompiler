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

    public static void generateStart() {
        appendCode("START", "", "", "");
        generateAlloc(0,1);
    }

    public static void generateHalt() {
        appendCode("HLT", "", "", "");
    }

    public static void generateJump(int label) {
        appendCode("JMP", "L" + label, "", "");
    }

    public static void generateCall(int label) {
        appendCode("CALL", "L" + label, "", "");
    }

    public static void generateReturn() {
        appendCode("RETURN", "", "", "");
    }
    public static void generateJumpF(int label) {
        appendCode("JMPF", "L" + label, "", "");
    }

    public static void generateAlloc(int address, int size) {
        appendCode("ALLOC", String.valueOf(address), String.valueOf(size), "");
    }

    public static void generateDalloc(int address, int size) {
        appendCode("DALLOC", String.valueOf(address), String.valueOf(size), "");
    }

    public static void generateStore(Symbol symbol) {
        appendCode("STR", String.valueOf(symbol.address), "", "");
    }
    public static void generateStoreFunction() {
        appendCode("STR", String.valueOf(0), "", "");
    }
    public static void generateRead(Symbol symbol) {
        appendCode("RD", "", "", "");
        generateStore(symbol);
    }

    public static void generateLabel(int label) {
        appendCode("NULL", "", "", "L" + label);

    }

    public static void generateLoadReturn() {
        appendCode("LDV", "0", "", "");

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

        //identificador, booleano ou numero
        if (symbol.identifier.matches("\\d+")) {
            appendCode("LDC", symbol.identifier, "", "");
        } else if (symbol.identifier.equals("verdadeiro")) {
            appendCode("LDC", "1", "", "");
        } else if (symbol.identifier.equals("falso")) {
            appendCode("LDC", "0", "", "");
        } else if (SymbolType.variables.contains(symbol.type)) {
            appendCode("LDV", String.valueOf(symbol.address), "", "");
        } else if (SymbolType.functions.contains(symbol.type)) {
            CodeGenerator.generateCall(symbol.address);
            CodeGenerator.generateLoadReturn();
        }
    }


    private static void appendCode(String instruction, String attribute1, String attribute2, String label) {
        String objInstruction = String.format("%-4s%-8s%-4s%-4s\n", label, instruction, attribute1, attribute2);
        codeBuilder.append(objInstruction);
    }


}
