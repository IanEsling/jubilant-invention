import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Item {

    private final String name;
    private final BigDecimal cost;

    public Item(String name) {
        this(name, BigDecimal.ZERO);
    }

    public Item(String name, double cost) {
        this(name, BigDecimal.valueOf(cost).setScale(2, RoundingMode.HALF_UP));
    }

    public Item(String name, BigDecimal cost) {
        this.name = Objects.requireNonNull(name);
        this.cost = Objects.requireNonNull(cost);
    }

    public static Item item(String name, double cost) {
        return new Item(name, cost);
    }

    public static Item item(String name, BigDecimal cost) {
        return new Item(name, cost);
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name) &&
                cost.doubleValue() == item.cost.doubleValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost.doubleValue());
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }
}
