import model.Basket;
import model.Items;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static model.Item.item;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    PrintStream output;
    @Mock
    GroceryOperations groceryOperations;
    @InjectMocks
    CommandExecutor testee;

    @Test
    void addItemToBasketCommand() {
        var itemQuantity = "3";
        var itemName = Items.SOUP.getName();
        var keepRunning = testee.execute(new String[]{CommandExecutor.ADD_COMMAND, itemQuantity, itemName});

        verify(output).println(String.format(CommandExecutor.ITEM_ADDED_MESSAGE, itemQuantity, itemName));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void listItemsCommand() {
        var items = List.of(item("1", 1),
                item("2", 2),
                item("3", 3));

        when(groceryOperations.listItems()).thenReturn(items);
        var keepRunning = testee.execute(new String[]{CommandExecutor.LIST_COMMAND});
        verify(output).println(CommandExecutor.ITEM_LIST_START_MESSAGE);
        items.forEach(i -> verify(output).println(String.format(CommandExecutor.ITEM_LIST_CONTENT_MESSAGE, i.getName(), i.getUnit(), i.getCost().toString())));
        verify(output).println(CommandExecutor.ITEM_LIST_END_MESSAGE);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void basketCommand() {
        var basket = new Basket();
        basket.addItem(1, item("1", 1))
                .addItem(2, item("2", 2))
                .addItem(3, item("3", 3));

        when(groceryOperations.getBasket()).thenReturn(basket);
        var keepRunning = testee.execute(new String[]{CommandExecutor.BASKET_COMMAND});
        verify(output).println(CommandExecutor.BASKET_START_MESSAGE);
        basket.getItems().forEach((key, value) -> verify(output).println(String.format(CommandExecutor.BASKET_CONTENT_MESSAGE, key.getName(), value)));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void clearCommand() {
        var basket = new Basket();
        basket.addItem(1, item("1", 1))
                .addItem(2, item("2", 2))
                .addItem(3, item("3", 3));

        when(groceryOperations.getBasket()).thenReturn(basket);
        var keepRunning = testee.execute(new String[]{CommandExecutor.CLEAR_COMMAND});
        verify(output).println(CommandExecutor.BASKET_CLEAR_MESSAGE);
        assertThat(basket.getItems()).hasSize(0);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void priceCommand() {
        var basketPrice = new BigDecimal("12.34");
        when(groceryOperations.priceBasket()).thenReturn(basketPrice);

        var keepRunning = testee.execute(new String[]{CommandExecutor.PRICE_COMMAND});
        verify(output).println(String.format(CommandExecutor.BASKET_PRICE_MESSAGE, basketPrice));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void setDateCommand() {

        String newDateString = "2020-04-01";
        var newDate = LocalDate.parse(newDateString);
        when(groceryOperations.getDate()).thenReturn(newDate);

        var keepRunning = testee.execute(new String[]{CommandExecutor.SET_DATE_COMMAND, newDateString});
        verify(output).println(String.format(CommandExecutor.DATE_MESSAGE, newDateString));
        verify(groceryOperations).setDate(newDate);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void getDateCommand() {
        var date = LocalDate.now();
        when(groceryOperations.getDate()).thenReturn(date);

        var keepRunning = testee.execute(new String[]{CommandExecutor.DATE_COMMAND});
        verify(output).println(String.format(CommandExecutor.DATE_MESSAGE, date));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void quitCommand() {
        var keepRunning = testee.execute(new String[]{CommandExecutor.QUIT_COMMAND});
        assertThat(keepRunning).isFalse();
    }

    @Test
    void unknownCommand() {
        CommandExecutor testee = new CommandExecutor(output, groceryOperations);
        String[] input = {"bibble", "bobble", "boing"};
        var keepRunning = testee.execute(input);
        verify(output).println(String.format(CommandExecutor.UNKNOWN_COMMAND_MESSAGE, Arrays.toString(input)));
        assertThat(keepRunning).isTrue();
    }
}