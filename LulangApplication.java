public class LulangApplication {

    public static void main(String[] args) {
        //Lexical.analyze();
        try {
            Syntactic.analyze();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
