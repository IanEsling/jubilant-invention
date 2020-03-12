import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Buy2GetHalfOffOffer implements Function<Basket, Map<Item, Integer>> {

    public static final String OFFER_ITEM_NAME = "Buy2Offer";
    private final Item buy2Item;
    private final Item halfOffItem;

    public Buy2GetHalfOffOffer(Item buy2Item, Item halfOffItem) {
        this.buy2Item = buy2Item;
        this.halfOffItem = halfOffItem;
    }

    @Override
    public Map<Item, Integer> apply(Basket basket) {
        Map<Item, Integer> offerItems = new HashMap<>();
        int noOfQualifyingPairs = basket.getItems().get(buy2Item) / 2;
        int noOfHalfOffItems = basket.getItems().get(halfOffItem);
        if (noOfHalfOffItems > 0 && noOfQualifyingPairs > 0) {
            offerItems.put(Item.item(OFFER_ITEM_NAME, halfOffItem.getCost().multiply(BigDecimal.valueOf(-0.5d))),
                    Math.min(noOfHalfOffItems, noOfQualifyingPairs));
        }
        return offerItems;
    }
}
