import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

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
        assertThat(basket.getItems().get(new Item("1"))).isEqualTo(2);
        testee.addToBasket(3, "1");
        assertThat(basket.getItems()).hasSize(1);
        assertThat(basket.getItems().get(new Item("1"))).isEqualTo(5);
        testee.addToBasket(4, "2");
        assertThat(basket.getItems()).hasSize(2);
        assertThat(basket.getItems().get(new Item("1"))).isEqualTo(5);
        assertThat(basket.getItems().get(new Item("2"))).isEqualTo(4);
    }

    @Test
    void throwIfTryToAddUnknownItemNameToBasket() {
        CommandExecutor testee = new CommandExecutor(items);
        assertThrows(UnknownItemException.class, () -> testee.addToBasket(2, "Unknown"));
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
        CommandExecutor testee = new CommandExecutor(Items.of(Item.item("Single", 987.65)));
        testee.addToBasket(2, "Single");
        BigDecimal price = testee.priceBasket();
        assertThat(price).isEqualTo(new BigDecimal("1975.30"));
    }

    @Test
    void calculatePriceOfBasketWithMultipleItems() {
        String times2 = "x2", times3 = "x3", times4 = "x4";
        CommandExecutor testee = new CommandExecutor(Items.of(Item.item(times2, 123.45),
                Item.item(times3, 678.90),
                Item.item(times4, 33.33)));
        testee.addToBasket(2, times2);
        testee.addToBasket(3, times3);
        testee.addToBasket(4, times4);
        BigDecimal price = testee.priceBasket();
        assertThat(price).isEqualTo(new BigDecimal("2416.92"));
    }
}