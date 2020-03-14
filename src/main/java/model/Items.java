package model;

import java.util.Optional;
import java.util.Set;

public class Items {
    public static final Item SOUP = Item.item("soup", 0.65d, "tin");
    public static final Item BREAD = Item.item("bread", 0.80d, "loaf");
    public static final Item MILK = Item.item("milk", 1.30d, "bottle");
    public static final Item APPLES = Item.item("apples", 0.10d, "single");
    private final Set<Item> items;

    private Items(Set<Item> items) {
        this.items = items;
    }

    public static Items liveItems() {
        return new Items(Set.of(SOUP, BREAD, MILK, APPLES));
    }

    public static Items of(Item... items) {
        return new Items(Set.of(items));
    }

    public Set<Item> getItems() {
        return items;
    }

    public Optional<Item> getItemByName(String itemName) {
        return items.stream()
                .filter(i -> i.getName().equalsIgnoreCase(itemName))
                .findFirst();
    }
}
