import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;

public class CommandExecutor {

    private final PrintStream output;
    private final GroceryOperations groceryOperations;

    public CommandExecutor(PrintStream output, GroceryOperations groceryOperations) {

        this.output = output;
        this.groceryOperations = groceryOperations;
    }

    public boolean execute(String[] input) {
        return Command.getCommandFor(input[0]).execute(this, input);
    }

    @SuppressWarnings("unused") // all enums used just not explicitly referenced
    enum Command {
        Quit(Inputs.QUIT) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                return false;
            }
        },
        Add(Inputs.ADD) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                int quantity = Integer.parseInt(input[1]);
                String itemName = input[2];
                ce.groceryOperations.addToBasket(quantity, itemName);
                ce.output.println(String.format(Messages.ITEM_ADDED, quantity, itemName));
                return true;
            }
        },
        List(Inputs.LIST) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(Messages.ITEM_LIST_START);
                ce.groceryOperations.listItems().forEach(i ->
                        ce.output.println(String.format(Messages.ITEM_LIST_CONTENT, i.getName(), i.getUnit(), i.getCost().toString())));
                ce.output.println(Messages.ITEM_LIST_END);
                return true;
            }
        },
        Clear(Inputs.CLEAR) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.groceryOperations.getBasket().clearItems();
                ce.output.println(Messages.BASKET_EMPTIED);
                return true;
            }
        },
        Basket(Inputs.BASKET) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(Messages.BASKET_CONTENT_START);
                ce.groceryOperations.getBasket().getItems().forEach((key, value) ->
                        ce.output.println(String.format(Messages.BASKET_CONTENT, key.getName(), value)));
                return true;
            }
        },
        Price(Inputs.PRICE) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(Messages.BASKET_PRICE, ce.groceryOperations.priceBasket()));
                return true;
            }
        },
        SetDate(Inputs.SET_DATE) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.groceryOperations.setDate(LocalDate.parse(input[1]));
                ce.output.println(String.format(Messages.DATE_TODAY, ce.groceryOperations.getDate()));
                return true;
            }
        },
        Date(Inputs.DATE) {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(Messages.DATE_TODAY, ce.groceryOperations.getDate()));
                return true;
            }
        },
        Unknown("") {
            @Override
            boolean execute(CommandExecutor ce, String[] input) {
                ce.output.println(String.format(Messages.UNKNOWN_COMMAND, Arrays.toString(input)));
                ce.output.println(HenrysGrocery.USAGE_MESSAGE);
                return true;
            }
        };

        private final String commandFor;

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

    static class Messages {

        public static final String ERROR = "Error trying to process command %s.";
        public static final String UNKNOWN_COMMAND = "unknown command: %s.";
        public static final String ITEM_LIST_START = "Items available:";
        public static final String ITEM_LIST_CONTENT = "%s! 1 %s for %s";
        public static final String ITEM_LIST_END = "use the 'add quantity item-name' command to add to your basket.";
        public static final String BASKET_PRICE = "total price = %s";
        public static final String DATE_TODAY = "today's date is %s";
        public static final String ITEM_ADDED = "added to basket: %s %s";
        public static final String BASKET_CONTENT_START = "you have the following items in your basket:";
        public static final String BASKET_CONTENT = "%s: %s";
        public static final String BASKET_EMPTIED = "your basket has been emptied.";
    }

    static class Inputs {

        public static final String DATE = "date";
        public static final String LIST = "list";
        public static final String BASKET = "basket";
        public static final String PRICE = "price";
        public static final String QUIT = "Q";
        public static final String ADD = "add";
        public static final String CLEAR = "clear";
        public static final String SET_DATE = "setdate";
    }
}
