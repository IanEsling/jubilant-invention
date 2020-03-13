package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Items {
    public static final Item SOUP = Item.item("soup", 0.65d);
    public static final Item BREAD = Item.item("bread", 0.80d);
    public static final Item MILK = Item.item("milk", 1.30d);
    public static final Item APPLES = Item.item("apples", 0.10d);
    private final Collection<Item> items;

    private Items(Collection<Item> items) {
        this.items = items;
    }

    public static Items liveItems(){
        return new Items(List.of(SOUP, BREAD, MILK, APPLES));
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
