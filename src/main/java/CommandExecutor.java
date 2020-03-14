import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class CommandExecutor {

    public static final String ERROR_MESSAGE = "Error trying to process command %s.";
    public static final String ITEM_LIST_START_MESSAGE = "Items available:";
    public static final String ITEM_LIST_CONTENT_MESSAGE = "%s! 1 %s for %s";
    public static final String ITEM_LIST_END_MESSAGE = "use the 'add quantity item-name' command to add to your basket.";
    public static final String LIST_COMMAND = "list";
    private final PrintStream output;
    private final GroceryOperations groceryOperations;

    public CommandExecutor(PrintStream output, GroceryOperations groceryOperations) {

        this.output = output;
        this.groceryOperations = groceryOperations;
    }

    public void execute(String[] input) {
        Command.getCommandFor(input[0]).execute(this, input);
    }


    enum Command {
        List(LIST_COMMAND) {
            @Override
            void execute(CommandExecutor ce, String[] input) {
                ce.output.println(ITEM_LIST_START_MESSAGE);
                ce.groceryOperations.listItems().forEach(i ->
                        ce.output.println(String.format(ITEM_LIST_CONTENT_MESSAGE, i.getName(), i.getUnit(), i.getCost().toString())));
                ce.output.println(ITEM_LIST_END_MESSAGE);
            }
        },
        Unknown("") {
            @Override
            void execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(ERROR_MESSAGE, Arrays.toString(input)));
                ce.output.println(HenrysGrocery.USAGE_MESSAGE);
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

        abstract void execute(CommandExecutor ce, String[] input);

    }

}
