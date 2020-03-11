import java.util.HashMap;
import java.util.Map;

public class Basket {
    private final Map<Item, Integer> items = new HashMap<>();

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void addItem(int quantity, Item item) {
        items.merge(item, quantity, Integer::sum);
    }

    public void clearItems(){
        items.clear();
    }
}
