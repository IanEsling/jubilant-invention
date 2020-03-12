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
        for (int i = 0; i < noOfHalfOffItems && i < noOfQualifyingPairs; i++) {
            offerItems.merge(Item.item(OFFER_ITEM_NAME, halfOffItem.getCost().multiply(BigDecimal.valueOf(-0.5d))),
                    1,
                    Integer::sum);
        }
        return offerItems;
    }
}
