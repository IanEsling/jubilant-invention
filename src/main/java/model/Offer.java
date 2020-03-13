package model;

import java.util.Map;
import java.util.function.Function;

public interface Offer extends Function<Basket, Map<Item, Integer>> {
}
