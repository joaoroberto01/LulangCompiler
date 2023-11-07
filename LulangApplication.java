import java.util.ArrayList;
import java.util.List;

public class LulangApplication {

    public static void main(String[] args) {
        try {
            Syntactic.analyze();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}