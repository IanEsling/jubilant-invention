import model.Items;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecificationAcceptanceTest {

    /*
    Price a basket containing: 3 tins of soup and 2 loaves of bread, bought today,
    Expected total cost = 3.15;
     */
    @Test
    void test1() {
        GroceryOperations testee = GroceryOperations.liveDataExecutor();
        testee.addToBasket(3, Items.SOUP.getName());
        testee.addToBasket(2, Items.BREAD.getName());

        assertThat(testee.priceBasket()).isEqualTo(BigDecimal.valueOf(3.15));
    }

    /*
    Price a basket containing: 6 apples and a bottle of milk, bought today,
    Expected total cost = 1.90;
     */
    @Test
    void test2() {
        GroceryOperations testee = GroceryOperations.liveDataExecutor();
        testee.addToBasket(6, Items.APPLES.getName());
        testee.addToBasket(1, Items.MILK.getName());

        assertThat(testee.priceBasket()).isEqualTo(BigDecimal.valueOf(1.90).setScale(2, RoundingMode.HALF_UP));
    }

    /*
    Price a basket containing: 6 apples and a bottle of milk, bought in 5 days time,
    Expected total cost = 1.84;
     */
    @Test
    void test3() {
        GroceryOperations testee = GroceryOperations.liveDataExecutor();
        testee.addToBasket(6, Items.APPLES.getName());
        testee.addToBasket(1, Items.MILK.getName());

        testee.setDate(Clock.fixed(Instant.now().plus(5, ChronoUnit.DAYS), ZoneId.systemDefault()));
        assertThat(testee.priceBasket()).isEqualTo(BigDecimal.valueOf(1.84));
    }

    /*
    Price a basket containing: 3 apples, 2 tins of soup and a loaf of bread, bought in 5 days time,
    Expected total cost = 1.97.
     */
    @Test
    void test4() {
        GroceryOperations testee = GroceryOperations.liveDataExecutor();
        testee.addToBasket(3, Items.APPLES.getName());
        testee.addToBasket(2, Items.SOUP.getName());
        testee.addToBasket(1, Items.BREAD.getName());

        testee.setDate(Clock.fixed(Instant.now().plus(5, ChronoUnit.DAYS), ZoneId.systemDefault()));
        assertThat(testee.priceBasket()).isEqualTo(BigDecimal.valueOf(1.97));
    }
}
