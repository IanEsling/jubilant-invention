import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandExecutorTest {

    private final Item item1 = Item.item("1", 12.34);
    private final Item item2 = Item.item("2", 56.78);
    private final Item item3 = Item.item("3", 90.12);
    private Items items = Items.of(item1, item2, item3);

    @Test
    void listItems() {
        Collection<Item> items = new CommandExecutor(this.items).listItems();
        assertThat(items).containsExactlyInAnyOrderElementsOf(this.items.getItems());
    }

    @Test
    void throwIfNoItemsToBeSupplied() {
        assertThrows(NullPointerException.class, () -> new CommandExecutor(null));
        assertThrows(UnsupportedOperationException.class, () -> new CommandExecutor(new Items(List.of())));
    }

    @Test
    void addItemsToBasket() {
        CommandExecutor testee = new CommandExecutor(items);
        testee.addToBasket(2, "1");
        Basket basket = testee.getBasket();
        assertThat(basket.getItems()).hasSize(1);
        assertThat(basket.getItems().get(item1)).isEqualTo(2);
        testee.addToBasket(3, "1");
        assertThat(basket.getItems()).hasSize(1);
        assertThat(basket.getItems().get(item1)).isEqualTo(5);
        testee.addToBasket(4, "2");
        assertThat(basket.getItems()).hasSize(2);
        assertThat(basket.getItems().get(item1)).isEqualTo(5);
        assertThat(basket.getItems().get(item2)).isEqualTo(4);
    }

    @Test
    void throwIfTryToAddUnknownItemNameToBasket() {
        CommandExecutor testee = new CommandExecutor(items);
        assertThrows(UnknownItemException.class, () -> testee.addToBasket(2, "Unknown"));
    }

    @Test
    void throwIfTryToAddLessThan1ItemNameToBasket() {
        CommandExecutor testee = new CommandExecutor(items);
        assertThrows(UnsupportedOperationException.class, () -> testee.addToBasket(0, "1"));
        assertThrows(UnsupportedOperationException.class, () -> testee.addToBasket(-10, "1"));
    }

    @Test
    void clearBasket() {
        CommandExecutor testee = new CommandExecutor(items);
        testee.addToBasket(2, "1");
        Basket basket = testee.getBasket();
        assertThat(basket.getItems()).hasSize(1);
        testee.emptyBasket();
        assertThat(basket.getItems()).hasSize(0);
    }

    @Test
    void calculatePriceOfBasketWithSingleItem() {
        double x2cost = 987.65;
        CommandExecutor testee = new CommandExecutor(Items.of(Item.item("Single", x2cost)));
        testee.addToBasket(2, "Single");
        BigDecimal price = testee.priceBasket();
        assertThat(price).isEqualTo(BigDecimal.valueOf(x2cost * 2).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    void calculatePriceOfBasketWithMultipleItems() {
        String times2 = "x2", times3 = "x3", times4 = "x4";
        double x2cost = 123.45, x3cost = 678.90, x4cost = 33.33;
        CommandExecutor testee = new CommandExecutor(Items.of(Item.item(times2, x2cost),
                Item.item(times3, x3cost),
                Item.item(times4, x4cost)));
        testee.addToBasket(2, times2);
        testee.addToBasket(3, times3);
        testee.addToBasket(4, times4);
        BigDecimal price = testee.priceBasket();
        assertThat(price).isEqualTo(BigDecimal.valueOf(x2cost * 2 + x3cost * 3 + x4cost * 4));
    }

    @Test
    void applySpecialOffer() {
        BigDecimal offerValue = BigDecimal.valueOf(-2.5);
        Function<Basket, Map<Item, Integer>> offer = (b) -> Map.of(Item.item("offer", offerValue), 1);
        var withoutOffer = new CommandExecutor(items);
        var withOffer = new CommandExecutor(items, List.of(offer));
        List.of(withOffer, withoutOffer).forEach(ce -> {
            ce.addToBasket(2, item1.getName());
            ce.addToBasket(3, item2.getName());
            ce.addToBasket(4, item3.getName());
        });
        assertThat(withOffer.priceBasket()).isEqualTo(withoutOffer.priceBasket().add(offerValue));
    }

    @Test
    void applyMultipleSpecialOffer() {
        BigDecimal offer1Value = BigDecimal.valueOf(-2.5);
        BigDecimal offer2Value = BigDecimal.valueOf(-20.5);
        BigDecimal offer3Value = BigDecimal.valueOf(-200.5);
        Function<Basket, Map<Item, Integer>> offer1 = (b) -> Map.of(Item.item("offer1", offer1Value), 1);
        Function<Basket, Map<Item, Integer>> offer2 = (b) -> Map.of(Item.item("offer2", offer2Value), 1);
        Function<Basket, Map<Item, Integer>> offer3 = (b) -> Map.of(Item.item("offer3", offer3Value), 1);

        var withoutOffer = new CommandExecutor(items);
        var withOffer = new CommandExecutor(items, List.of(offer1, offer2, offer3));
        List.of(withOffer, withoutOffer).forEach(ce -> {
            ce.addToBasket(2, item1.getName());
            ce.addToBasket(3, item2.getName());
            ce.addToBasket(4, item3.getName());
        });

        assertThat(withOffer.priceBasket()).isEqualTo(withoutOffer.priceBasket()
                .add(offer1Value)
                .add(offer2Value)
                .add(offer3Value));
    }
}