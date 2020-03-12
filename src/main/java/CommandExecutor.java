import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommandExecutor {

    private static final Buy2GetHalfOffOffer buy2SoupGetHalfPriceBreadOffer = Buy2GetHalfOffOffer.forItems(Items.SOUP, Items.BREAD);
    private static final PercentageDiscountOffer tenPercentOffApplesOffer = PercentageDiscountOffer.buildOffer(Items.APPLES, 10);

    private final Items items;
    private final Basket basket;

    private Collection<Function<Basket, Map<Item, Integer>>> offers;

    private CommandExecutor(Items items, Collection<Function<Basket, Map<Item, Integer>>> offers) {
        this.items = items;
        this.offers = offers == null ? new ArrayList<>() : offers;
        this.basket = new Basket();
    }

    public static CommandExecutor forItemsAndOffers(Items items, Collection<Function<Basket, Map<Item, Integer>>> offers) {
        if (items == null || items.getItems() == null || items.getItems().size() == 0)
            throw new UnsupportedOperationException("need at least one item available");
        return new CommandExecutor(items, offers);
    }

    public static CommandExecutor forItems(Items items) {
        return forItemsAndOffers(items, List.of());
    }

    public static CommandExecutor liveDataExecutor() {

        return forItemsAndOffers(Items.liveItems(),
                List.of(tenPercentOffApplesOffer, buy2SoupGetHalfPriceBreadOffer));
    }

    public Collection<Item> listItems() {
        return items.getItems();
    }

    public void addToBasket(int quantity, String itemName) {
        basket.addItem(quantity,
                items.getItemByName(itemName)
                        .orElseThrow(() -> new UnknownItemException(itemName)));
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
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Map<Item, Integer> applyOffers(Basket basket) {
        Map<Item, Integer> offerItems = new HashMap<>();
        offers.forEach(o -> offerItems.putAll(o.apply(basket)));
        offerItems.putAll(basket.getItems());
        return offerItems;
    }

    public LocalDate getDate() {
        return LocalDate.now();
    }
}
