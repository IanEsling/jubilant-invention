public class UnknownItemException extends RuntimeException {

    public UnknownItemException(String itemName) {
        super("Unknown item: " + itemName);
    }
}
