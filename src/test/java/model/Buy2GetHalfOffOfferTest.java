package model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Buy2GetHalfOffOfferTest {

    public static final double HALF_OFF_ITEM_COST = 75;
    public static final double HALF_OFF_ITEM_DISCOUNT = -37.5;
    final Item buy2OfferItem = Item.item("buy2", 123);
    final Item getHalfOffOfferItem = Item.item("halfOff", HALF_OFF_ITEM_COST);

    @Test
    void cannotCreateOfferWithoutItems() {
        assertThrows(UnsupportedOperationException.class, () -> Buy2GetHalfOffOffer.forItems(null, getHalfOffOfferItem));
        assertThrows(UnsupportedOperationException.class, () -> Buy2GetHalfOffOffer.forItems(buy2OfferItem, null));
    }

    @Test
    void nothingTerribleHappensIfHalfOffItemNotPresent() {
        assertOffers(0, 1, Map.of());
    }

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
        var basket = new Basket();
        if (quantityBuy2OfferItems > 0) {
            basket.addItem(quantityBuy2OfferItems, buy2OfferItem);
        }
        if (quantityHalfOffOfferItems > 0) {
            basket.addItem(quantityHalfOffOfferItems, getHalfOffOfferItem);
        }
        var offer = Buy2GetHalfOffOffer.forItems(buy2OfferItem, getHalfOffOfferItem);

        assertThat(offer.apply(basket)).containsExactlyInAnyOrderEntriesOf(expectedOffers);
    }


}