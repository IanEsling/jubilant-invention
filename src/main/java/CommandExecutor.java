import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CommandExecutor {

    private final Items items;
    private final Basket basket;

    private OfferScheduler offerScheduler;
    private Clock clock;

    private CommandExecutor(Items items,
                            OfferScheduler offerScheduler,
                            Clock clock) {
        this.items = items;
        this.offerScheduler = offerScheduler;
        this.clock = clock;
        this.basket = new Basket();
    }

    public static CommandExecutor forItemsAndOffers(Items items, Offer... offers) {
        return forItemsAndOffers(items, OfferScheduler.forOffers(offers), Clock.systemDefaultZone());
    }

    public static CommandExecutor forItemsAndOffers(Items items, OfferScheduler offers) {
        return forItemsAndOffers(items, offers, Clock.systemDefaultZone());
    }

    public static CommandExecutor forItemsAndOffers(Items items, OfferScheduler offers,
                                                    Clock clock) {
        if (items == null || items.getItems() == null || items.getItems().size() == 0)
            throw new UnsupportedOperationException("need at least one item available");
        return new CommandExecutor(items, offers, clock);
    }

    public static CommandExecutor forItems(Items items) {
        return forItemsAndOffers(items, OfferScheduler.empty(), Clock.systemDefaultZone());
    }

    public static CommandExecutor forItems(Items items, Clock clock) {
        return forItemsAndOffers(items, OfferScheduler.empty(), clock);
    }


    public static CommandExecutor liveDataExecutor() {

        return forItemsAndOffers(Items.liveItems(),
                OfferScheduler.liveOffers());
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

    public LocalDate getDate() {
        return LocalDate.now(clock);
    }

    public void setDate(Clock newClock) {
        this.clock = newClock;
    }

    public BigDecimal priceBasket() {
        return applyOffers(basket).entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey().getCost().multiply(BigDecimal.valueOf(e.getValue()))))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Map<Item, Integer> applyOffers(Basket basket) {
        Map<Item, Integer> offerItems = new HashMap<>();
        offerScheduler.getOffersForDate(LocalDate.now(clock)).forEach(o -> offerItems.putAll(o.apply(basket)));

        offerItems.putAll(basket.getItems());
        return offerItems;
    }
}
