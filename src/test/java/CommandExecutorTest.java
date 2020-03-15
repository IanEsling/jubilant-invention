import model.Basket;
import model.Items;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.math.BigDecimal;
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
        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.ADD, itemQuantity, itemName});

        verify(output).println(String.format(CommandExecutor.Messages.ITEM_ADDED, itemQuantity, itemName));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void listItemsCommand() {
        var items = List.of(item("1", 1),
                item("2", 2),
                item("3", 3));

        when(groceryOperations.listItems()).thenReturn(items);
        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.LIST});
        verify(output).println(CommandExecutor.Messages.ITEM_LIST_START);
        items.forEach(i -> verify(output).println(String.format(CommandExecutor.Messages.ITEM_LIST_CONTENT, i.getName(), i.getUnit(), i.getCost().toString())));
        verify(output).println(CommandExecutor.Messages.ITEM_LIST_END);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void basketCommand() {
        var basket = new Basket();
        basket.addItem(1, item("1", 1))
                .addItem(2, item("2", 2))
                .addItem(3, item("3", 3));

        when(groceryOperations.getBasket()).thenReturn(basket);
        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.BASKET});
        verify(output).println(CommandExecutor.Messages.BASKET_CONTENT_START);
        basket.getItems().forEach((key, value) -> verify(output).println(String.format(CommandExecutor.Messages.BASKET_CONTENT, key.getName(), value)));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void clearCommand() {
        var basket = new Basket();
        basket.addItem(1, item("1", 1))
                .addItem(2, item("2", 2))
                .addItem(3, item("3", 3));

        when(groceryOperations.getBasket()).thenReturn(basket);
        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.CLEAR});
        verify(output).println(CommandExecutor.Messages.BASKET_EMPTIED);
        assertThat(basket.getItems()).hasSize(0);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void priceCommand() {
        var basketPrice = new BigDecimal("12.34");
        when(groceryOperations.priceBasket()).thenReturn(basketPrice);

        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.PRICE});
        verify(output).println(String.format(CommandExecutor.Messages.BASKET_PRICE, basketPrice));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void setDateCommand() {

        String newDateString = "2020-04-01";
        var newDate = LocalDate.parse(newDateString);
        when(groceryOperations.getDate()).thenReturn(newDate);

        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.SET_DATE, newDateString});
        verify(output).println(String.format(CommandExecutor.Messages.DATE_TODAY, newDateString));
        verify(groceryOperations).setDate(newDate);
        assertThat(keepRunning).isTrue();
    }

    @Test
    void getDateCommand() {
        var date = LocalDate.now();
        when(groceryOperations.getDate()).thenReturn(date);

        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.DATE});
        verify(output).println(String.format(CommandExecutor.Messages.DATE_TODAY, date));
        assertThat(keepRunning).isTrue();
    }

    @Test
    void quitCommand() {
        var keepRunning = testee.execute(new String[]{CommandExecutor.Inputs.QUIT});
        assertThat(keepRunning).isFalse();
    }

    @Test
    void unknownCommand() {
        CommandExecutor testee = new CommandExecutor(output, groceryOperations);
        String[] input = {"bibble", "bobble", "boing"};
        var keepRunning = testee.execute(input);
        verify(output).println(String.format(CommandExecutor.Messages.UNKNOWN_COMMAND, Arrays.toString(input)));
        assertThat(keepRunning).isTrue();
    }
}