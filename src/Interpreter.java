import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Interpreter {
    private static final String VERSION_CODE = "0.1 Unstable";
    private static final String AUTHOR = "Luis Hoderlein";

    private static int[] memory;
    private static String[] instructions;
    private static int line;

    public static void init(File file) throws IOException {
        printLine();
        Scanner scanner = new Scanner(file);
        String def = scanner.nextLine();

        if (!def.startsWith("MACHINE ")) throw new IOException("NOT A VALID MACHINE");

        try {
            System.out.println("ARRAYSCRIPT VERSION: " + VERSION_CODE);
            System.out.println("ARRAYSCRIPT AUTHOR: " + AUTHOR);
            System.out.println("PROGRAM OUTPUT FOR: " + file.getName());

            printLine();

            memory = new int[Integer.parseInt(def.substring(8))];
            System.out.println("CREATED A MACHINE WITH SIZE: " + memory.length);

            printLine();
        } catch (Exception e) {
            System.out.println("ERROR INITIALIZING MEMORY");
            return;
        }

        List<String> inst = new ArrayList<String>();
        while (scanner.hasNext()) {
            inst.add(scanner.nextLine());
        }

        instructions = new String[inst.size()];
        for (int i = 0; i < inst.size(); i++) {
            instructions[i] = inst.get(i);
        }

        line = 0;

        while (line < instructions.length) {
            if (instructions[line].startsWith("#")) {
            } else if (exec(instructions[line].split(" ")).equals("FAIL")) {
                System.out.println("FAILURE IN LINE: " + (line + 1) + ": " + instructions[line]);
            }
            line++;
        }

        printLine();
    }

    private static String exec(String[] tokens) {
        String current;

        if (tokens.length == 1)
            return tokens[0];

        if (tokens[0].equals("PRINT")) {
            if((current = exec(cutDown(tokens, 1, tokens.length))).equals("FAIL")) {
                return "FAIL";
            } else {
                System.out.println(current);
                return current;
            }
        } else if (tokens[0].equals("READ")) {
            try {
                return "" + read(Integer.parseInt(exec(cutDown(tokens, 1, tokens.length))));
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("WRITE")) {
            try {
                if (tokens.length == 2)
                    return "FAIL";
                int index = select(tokens);
                int output = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                write(Integer.parseInt(exec(cutDown(tokens, 1, index))), output);
                return "" + output;
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("STRING")) {
            try {
                String out = "";

                int index = select(tokens);
                int begin = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int end = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));

                for (int i = begin; i < end; i++) {
                    if (read(i) != 0)
                        out += (char)read(i);
                }

                return out;
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("EXEC")) {
            try {
                return exec(instructions[Integer.parseInt(exec(cutDown(tokens, 1, tokens.length))) - 2]
                        .split(" "));
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("GOTO")) {
            try {
                line = Integer.parseInt(exec(cutDown(tokens, 1, tokens.length))) - 3;
                return instructions[line + 1];
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("ADD")) {
            int index = select(tokens);
            try {
                int a = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int b = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                return a + b + "";
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("SUB")) {
            int index = select(tokens);
            try {
                int a = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int b = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                return a - b + "";
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("MUL")) {
            int index = select(tokens);
            try {
                int a = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int b = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                return a * b + "";
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("DIV")) {
            int index = select(tokens);
            try {
                int a = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int b = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                return a / b + "";
            } catch (Exception e) {
                return "FAIL";
            }
        } else if (tokens[0].equals("MOD")) {
            int index = select(tokens);
            try {
                int a = Integer.parseInt(exec(cutDown(tokens, 1, index)));
                int b = Integer.parseInt(exec(cutDown(tokens, index, tokens.length)));
                return a % b + "";
            } catch (Exception e) {
                return "FAIL";
            }
        }

        return "FAIL";
    }

    private static void write(int loc, int val) {
        memory[loc] = val;
    }

    private static int read(int loc) {
        return memory[loc];
    }

    private static void printLine() {
        for (int i = 0; i < 44; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    private static int select(String[] tokens) {
        for (int i = 1; i < tokens.length; i++) {
            try {
                Integer.parseInt(tokens[i]);
                return i + 1;
            } catch (Exception e) {}
        }

        return -1;
    }

    private static String[] cutDown(String[] in, int a, int b) {
        String[] cutDown = new String[b - a];
        for (int i = 0; i < b - a; i++)
            cutDown[i] = in[a + i];
        return cutDown;
    }
}
