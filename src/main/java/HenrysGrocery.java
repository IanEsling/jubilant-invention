import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class HenrysGrocery {

    public static final String QUIT_MESSAGE = "Goodbye, thanks for shopping at Henry's!";
    public static final String USAGE_MESSAGE = "USAGE: " + System.lineSeparator() +
            "list - list all items available" + System.lineSeparator() +
            "basket - list all items in your basket" + System.lineSeparator() +
            "add quantity item-name - adds the given number of items to your basket" + System.lineSeparator() +
            "clear - removes everything from your basket" + System.lineSeparator() +
            "price - calculates the price of the basket contents" + System.lineSeparator() +
            "date - show the current date of the grocery" + System.lineSeparator() +
            "setdate DD/MM/YYYY - set the date of the grocery" + System.lineSeparator() +
            "Q - Quit the program";
    public static final String ERROR_MESSAGE = "Error trying to process command %s.";

    private final Scanner scanner;
    private final PrintStream output;
    private final CommandExecutor commandExecutor;
    private boolean running = true;

    public HenrysGrocery(InputStream input, PrintStream output) {
        this(input, output, new CommandExecutor(output, GroceryOperations.liveDataOperations()));
    }

    public HenrysGrocery(InputStream input,
                         PrintStream output,
                         CommandExecutor commandExecutor) {
        this.scanner = new Scanner(input);
        this.output = output;
        this.commandExecutor = commandExecutor;
    }

    public static void main(String... args) {
        printWelcome(System.out);
        HenrysGrocery grocery = new HenrysGrocery(System.in, System.out);
        grocery.run();
    }

    private static void printWelcome(PrintStream output) {
        output.println("\n" +
                "\n" +
                " _   _                       _       _____                               \n" +
                "| | | |                     ( )     |  __ \\                              \n" +
                "| |_| | ___ _ __  _ __ _   _|/ ___  | |  \\/_ __ ___   ___ ___ _ __ _   _ \n" +
                "|  _  |/ _ \\ '_ \\| '__| | | | / __| | | __| '__/ _ \\ / __/ _ \\ '__| | | |\n" +
                "| | | |  __/ | | | |  | |_| | \\__ \\ | |_\\ \\ | | (_) | (_|  __/ |  | |_| |\n" +
                "\\_| |_/\\___|_| |_|_|   \\__, | |___/  \\____/_|  \\___/ \\___\\___|_|   \\__, |\n" +
                "                        __/ |                                       __/ |\n" +
                "                       |___/                                       |___/ \n" +
                "\n");
        output.println(USAGE_MESSAGE);
        output.println();
        output.println();
    }

    public void run() {
        run(() -> true);
    }

    public void run(KeepRunning keepRunning) {

        while (running && keepRunning.keepRunning()) {
            if (scanner.hasNextLine()) {

                String[] input = scanner.nextLine().split(" ");

                try {
                    running = commandExecutor.execute(input);
                } catch (Exception e) {
                    output.println(String.format(ERROR_MESSAGE, Arrays.toString(input)));
                    output.println(USAGE_MESSAGE);
                }
            }
        }
        output.println(QUIT_MESSAGE);
    }

    @FunctionalInterface
    interface KeepRunning {
        boolean keepRunning();
    }

}
