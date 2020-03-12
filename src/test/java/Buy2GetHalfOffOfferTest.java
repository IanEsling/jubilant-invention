import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class Buy2GetHalfOffOfferTest {

    public static final double HALF_OFF_ITEM_COST = 75;
    public static final double HALF_OFF_ITEM_DISCOUNT = -37.5;
    Item buy2OfferItem = Item.item("buy2", 123);
    Item getHalfOffOfferItem = Item.item("halfOff", HALF_OFF_ITEM_COST);

    @Test
    void expectedOffersForQuantitiesOfOfferItems() {
        assertOffers(1, 1, Map.of());
        assertOffers(2, 1, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 1));
        assertOffers(2, 0, Map.of());
        assertOffers(3, 1, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 1));
        assertOffers(4, 1, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 1));
        assertOffers(4, 2, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 2));
        assertOffers(4, 3, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 2));
        assertOffers(5, 3, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 2));
        assertOffers(6, 0, Map.of());
        assertOffers(6, 3, Map.of(Item.item(Buy2GetHalfOffOffer.OFFER_ITEM_NAME, HALF_OFF_ITEM_DISCOUNT), 3));
    }

    private void assertOffers(int quantityBuy2OfferItems,
                              int quantityHalfOffOfferItems,
                              Map<Item, Integer> expectedOffers) {
        var basket = new Basket()
                .addItem(quantityBuy2OfferItems, buy2OfferItem)
                .addItem(quantityHalfOffOfferItems, getHalfOffOfferItem);
        var offer = new Buy2GetHalfOffOffer(buy2OfferItem, getHalfOffOfferItem);

        assertThat(offer.apply(basket)).containsExactlyInAnyOrderEntriesOf(expectedOffers);
    }


}