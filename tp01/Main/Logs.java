package Main;
public class Logs {

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    public static void Alert(String Message) {
        String msg = "\n" + Message;
        System.out.println(RED + msg + RESET);
    }

    public static void Succeed(String Message) {
        String msg = "\n" + Message;
        System.out.println(GREEN + msg + RESET);
    }

    public static void Details(String Message) {
        String msg = "\n" + Message;
        System.out.println(BLUE + msg + RESET);
    }

}
