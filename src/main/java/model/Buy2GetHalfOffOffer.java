package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Buy2GetHalfOffOffer implements Offer {

    public static final String OFFER_ITEM_NAME = "Buy2Offer";

    private final Item buy2Item;
    private final Item halfOffItem;

    private Buy2GetHalfOffOffer(Item buy2Item, Item halfOffItem) {
        this.buy2Item = buy2Item;
        this.halfOffItem = halfOffItem;
    }

    public static Buy2GetHalfOffOffer forItems(Item buy2Item, Item halfOffItem) {
        if (buy2Item == null || halfOffItem == null) {
            throw new UnsupportedOperationException("need 2 items to create offer, got: " + buy2Item + ", " + halfOffItem);
        }
        return new Buy2GetHalfOffOffer(buy2Item, halfOffItem);
    }

    @Override
    public Map<Item, Integer> apply(Basket basket) {
        Map<Item, Integer> offerItems = new HashMap<>();
        if (basket.getItems().containsKey(buy2Item) &&
                basket.getItems().containsKey(halfOffItem)) {
            int noOfQualifyingPairs = basket.getItems().get(buy2Item) / 2;
            int noOfHalfOffItems = basket.getItems().get(halfOffItem);
            if (noOfHalfOffItems > 0 && noOfQualifyingPairs > 0) {
                offerItems.put(Item.item(OFFER_ITEM_NAME, halfOffItem.getCost().multiply(BigDecimal.valueOf(-0.5d))),
                        Math.min(noOfHalfOffItems, noOfQualifyingPairs));
            }
        }
        return offerItems;
    }

    @Override
    public String toString() {
        return "model.Buy2GetHalfOffOffer{" +
                "buy2Item=" + buy2Item +
                ", halfOffItem=" + halfOffItem +
                '}';
    }
}
