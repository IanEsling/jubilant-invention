import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommandExecutor {

    private final Items items;
    private final Basket basket;
    private Collection<Function<Basket, Map<Item, Integer>>> offers;

    public CommandExecutor(Items items) {
        this(items, List.of());
    }

    public CommandExecutor(Items items, Collection<Function<Basket, Map<Item, Integer>>> offers) {

        this.items = Objects.requireNonNull(items);
        if (items.getItems().size() == 0) throw new UnsupportedOperationException("need at least one item available");
        this.offers = offers;
        this.basket = new Basket();
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
        return applyOffers(basket).entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey().getCost().multiply(BigDecimal.valueOf(e.getValue()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Item, Integer> applyOffers(Basket basket) {
        Map<Item, Integer> offerItems = new HashMap<>();
        offers.forEach(o -> offerItems.putAll(o.apply(basket)));
        offerItems.putAll(basket.getItems());
        return offerItems;
    }

}
