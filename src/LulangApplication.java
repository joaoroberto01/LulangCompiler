package src;

import src.analyzers.Syntactic;
import src.exceptions.CompilerException;

import java.nio.file.NoSuchFileException;

public class LulangApplication {

    public static void main(String[] args) {
        String filepath = args.length == 0 ? "ricardo" : args[0];
        try {
            Syntactic.analyze(filepath);

            System.out.println("successfully compiled source code");
            CodeGenerator.saveFile(filepath);
        } catch (NoSuchFileException e) {
            System.err.printf("file '%s' not found%n", filepath);
        } catch (CompilerException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("internal compiler error: " + e.getMessage());
        }

    }
}
