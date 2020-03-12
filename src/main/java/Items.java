import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Items {
    public static final Item SOUP = new Item("soup", 0.65d);
    public static final Item BREAD = new Item("bread", 0.80d);
    public static final Item MILK = new Item("milk", 1.30d);
    public static final Item APPLES = new Item("apples", 0.10d);
    private final Collection<Item> items;

    public Items() {
        this(List.of(SOUP, BREAD, MILK, APPLES));
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
