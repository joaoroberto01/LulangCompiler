package src;

import src.analyzers.Syntactic;

import java.nio.file.NoSuchFileException;

public class LulangApplication {

    public static void main(String[] args) {
        String filepath = args.length == 0 ? "sint1.txt" : args[0];
        try {
            Syntactic.analyze(filepath);
        } catch (NoSuchFileException e) {
            System.err.printf("file '%s' not found%n", filepath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}