package model;

import java.math.BigDecimal;
import java.util.Map;

public class PercentageDiscountOffer implements Offer {
    public static final String OFFER_ITEM_NAME = "PercentageDiscount";

    private final Item discountedItem;
    private int percentageDiscount;

    private PercentageDiscountOffer(Item discountedItem, int percentageDiscount) {
        this.discountedItem = discountedItem;
        this.percentageDiscount = percentageDiscount;
    }

    public static PercentageDiscountOffer buildOffer(Item discountedItem, int percentageDiscount) {
        if (percentageDiscount < 1 || percentageDiscount > 99)
            throw new UnsupportedOperationException("percentage discount must be between 1 and 99");
        return new PercentageDiscountOffer(discountedItem, percentageDiscount);
    }

    @Override
    public Map<Item, Integer> apply(Basket basket) {
        if (basket.getItems().containsKey(discountedItem)) {
            return Map.of(Item.item(OFFER_ITEM_NAME,
                    discountedItem.getCost().multiply(BigDecimal.valueOf(-percentageDiscount / 100d))),
                    basket.getItems().get(discountedItem));
        } else {
            return Map.of();
        }
    }

    @Override
    public String toString() {
        return "model.PercentageDiscountOffer{" +
                "discountedItem=" + discountedItem +
                ", percentageDiscount=" + percentageDiscount +
                '}';
    }
}
