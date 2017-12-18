import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] arg0) throws IOException {
        Interpreter.init(new File("fib"));
    }
}
