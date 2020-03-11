import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class CommandExecutor {

    private final Items items;
    private final Basket basket;

    public CommandExecutor(Items items) {

        this.items = Objects.requireNonNull(items);
        if (items.getItems().size() == 0) throw new UnsupportedOperationException("need at least one item available");
        basket = new Basket();
    }

    public Collection<Item> listItems() {
        return items.getItems();
    }

    public void addToBasket(int quantity, String itemName) {
        basket.addItem(quantity,
                items.getItemByName(itemName).orElseThrow(() -> new UnknownItemException(itemName)));
    }

    public Basket getBasket() {
        return basket;
    }

    public void emptyBasket() {
        basket.clearItems();
    }

    public BigDecimal priceBasket() {
        return basket.getItems().entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey().getCost().multiply(BigDecimal.valueOf(e.getValue()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
