import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

public class CommandExecutor {

    private final Collection<Item> items;

    public CommandExecutor(Supplier<Collection<Item>> itemsSupplier) {

        this.items = Objects.requireNonNull(itemsSupplier.get());
        if (items.size() == 0) throw new UnsupportedOperationException("need at least one item available");
    }

    public Collection<Item> listItems() {
        return items;
    }
}
