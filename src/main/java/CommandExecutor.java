import java.io.PrintStream;
import java.util.Arrays;

public class CommandExecutor {

    public static final String ERROR_MESSAGE = "Error trying to process command %s.";
    public static final String UNKNOWN_COMMAND_MESSAGE = "unknown command: %s.";
    public static final String ITEM_LIST_START_MESSAGE = "Items available:";
    public static final String ITEM_LIST_CONTENT_MESSAGE = "%s! 1 %s for %s";
    public static final String ITEM_LIST_END_MESSAGE = "use the 'add quantity item-name' command to add to your basket.";
    public static final String BASKET_PRICE_MESSAGE = "total price = %s";
    public static final String DATE_MESSAGE = "today's date is %s";
    public static final String LIST_COMMAND = "list";
    public static final String PRICE_COMMAND = "price";
    public static final String DATE_COMMAND = "date";
    public static final String QUIT_COMMAND = "Q";
    private final PrintStream output;
    private final GroceryOperations groceryOperations;

    public CommandExecutor(PrintStream output, GroceryOperations groceryOperations) {

        this.output = output;
        this.groceryOperations = groceryOperations;
    }

    public boolean execute(String[] input) {
        return Command.getCommandFor(input[0]).execute(this, input);
    }


    enum Command {
        Quit(QUIT_COMMAND) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                return false;
            }
        },
        List(LIST_COMMAND) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(ITEM_LIST_START_MESSAGE);
                ce.groceryOperations.listItems().forEach(i ->
                        ce.output.println(String.format(ITEM_LIST_CONTENT_MESSAGE, i.getName(), i.getUnit(), i.getCost().toString())));
                ce.output.println(ITEM_LIST_END_MESSAGE);
                return true;
            }
        },
        Price(PRICE_COMMAND) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(BASKET_PRICE_MESSAGE, ce.groceryOperations.priceBasket()));
                return true;
            }
        },
        Date(DATE_COMMAND) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(DATE_MESSAGE, ce.groceryOperations.getDate()));
                return true;
            }
        },
        Unknown("") {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(UNKNOWN_COMMAND_MESSAGE, Arrays.toString(input)));
                ce.output.println(HenrysGrocery.USAGE_MESSAGE);
                return true;
            }
        };

        private String commandFor;

        Command(String commandFor) {
            this.commandFor = commandFor;
        }

        static Command getCommandFor(String input) {
            for (Command command : Command.values()) {
                if (command.commandFor.equalsIgnoreCase(input)) return command;
            }
            return Unknown;
        }

        abstract boolean execute(CommandExecutor ce, String[] input);

    }

}
