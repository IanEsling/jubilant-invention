package model;

import java.util.HashMap;
import java.util.Map;

public class Basket {
    private final Map<Item, Integer> items = new HashMap<>();

    public Map<Item, Integer> getItems() {
        return items;
    }

    public Basket addItem(int quantity, Item item) {
        if (quantity > 0) {
            items.merge(item, quantity, Integer::sum);
            return this;
        } else {
            throw new UnsupportedOperationException("cannot add less than one item to basket");
        }
    }

    public void clearItems() {
        items.clear();
    }

    @Override
    public String toString() {
        return "model.Basket{" +
                "items=" + items +
                '}';
    }
}
