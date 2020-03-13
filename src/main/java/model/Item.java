package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Item {

    private final String name;
    private final BigDecimal cost;
    private final String unit;

    private Item(String name, double cost, String unit) {
        this(name, BigDecimal.valueOf(cost), unit);
    }

    private Item(String name, BigDecimal cost, String unit) {
        this.unit = unit;
        this.name = Objects.requireNonNull(name);
        this.cost = Objects.requireNonNull(cost)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static Item item(String name, double cost) {
        return new Item(name, cost, "unit");
    }

    public static Item item(String name, double cost, String unit) {
        return new Item(name, cost, unit);
    }

    public static Item item(String name, BigDecimal cost) {
        return new Item(name, cost, "unit");
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
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
        return "model.Item{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                '}';
    }
}
