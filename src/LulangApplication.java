package src;

import src.analyzers.Syntactic;

import java.nio.file.NoSuchFileException;

public class LulangApplication {

    public static void main(String[] args) {
        String filepath = args.length == 0 ? "source_code.ll" : args[0];
//        String filepath = args.length == 0 ? "sint1.txt" : args[0];
        //TODO barrar entrada de .obj (apenas saida)
        try {
            Syntactic.analyze(filepath);
            //sucesso
            System.out.println("AUUUUUUUUUUUUUUUUUUUU");
            System.out.println(CodeGenerator.codeBuilder.toString());
            CodeGenerator.saveFile(filepath);
        } catch (NoSuchFileException e) {
            System.err.printf("file '%s' not found%n", filepath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}