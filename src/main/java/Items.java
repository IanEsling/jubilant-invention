import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Items {
    private final Collection<Item> items;

    public Items() {
        this(List.of(new Item("soup", 0.65d),
                new Item("bread", 0.80),
                new Item("milk", 1.30),
                new Item("apples", 0.10)));
    }

    public Items(Collection<Item> items) {
        this.items = items;
    }

    public static Items of(Item... items) {
        return new Items(Arrays.asList(items));
    }

    public Collection<Item> getItems() {
        return items;
    }

    public Optional<Item> getItemByName(String itemName) {
        return items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(itemName))
                .findFirst();
    }

}
