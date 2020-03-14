package model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PercentageDiscountOfferTest {

    public static final double DISCOUNT_ITEM_COST = 123.45;
    final Item discountItem = Item.item("discount", DISCOUNT_ITEM_COST);

    @Test
    void expectedOffersForQuantitiesOfOfferItems() {
        assertOffers(1, 10, -12.345);
        assertOffers(2, 10, -12.345);
        assertOffers(2, 20, -24.690);
        assertOffers(6, 16, -19.7520);
        assertOffers(3, 1, -1.2345);
        assertOffers(20, 99, -122.2155);
    }

    @Test
    void throwIfPercentageDiscountNotBetween1And99() {
        assertThrows(UnsupportedOperationException.class, () -> PercentageDiscountOffer.buildOffer(discountItem, 0));
        assertThrows(UnsupportedOperationException.class, () -> PercentageDiscountOffer.buildOffer(discountItem, 100));
        PercentageDiscountOffer.buildOffer(discountItem, 1);
        PercentageDiscountOffer.buildOffer(discountItem, 99);
    }

    private void assertOffers(int quantityDiscountItems,
                              int percentageDiscount,
                              double discountAmount) {
        var basket = new Basket().addItem(quantityDiscountItems, discountItem);
        var offer = PercentageDiscountOffer.buildOffer(discountItem, percentageDiscount);
        var expectedOffers = Map.of(Item.item(PercentageDiscountOffer.OFFER_ITEM_NAME, discountAmount),
                quantityDiscountItems);
        System.out.println(expectedOffers);
        assertThat(offer.apply(basket)).containsExactlyInAnyOrderEntriesOf(expectedOffers);
    }
}