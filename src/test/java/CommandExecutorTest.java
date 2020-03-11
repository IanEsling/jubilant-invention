import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandExecutorTest {

    private Supplier<Collection<Item>> itemsSupplier;

    @Test
    void listItems() {
        itemsSupplier = () -> List.of(new Item("1"), new Item("2"));
        CommandExecutor testee = new CommandExecutor(itemsSupplier);
        Collection<Item> items = testee.listItems();
        assertThat(items).containsExactlyInAnyOrderElementsOf(itemsSupplier.get());
    }

    @Test
    void throwsIfNoItemsSupplied() {
        itemsSupplier = () -> null;
        assertThrows(NullPointerException.class, () -> new CommandExecutor(itemsSupplier));
        itemsSupplier = List::of;
        assertThrows(UnsupportedOperationException.class, () -> new CommandExecutor(itemsSupplier));
    }
}