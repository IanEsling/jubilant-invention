import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static model.Item.item;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    PrintStream output;
    @Mock
    GroceryOperations groceryOperations;

    @Test
    void listItemsCommand() {
        var items = List.of(item("1", 1),
                item("2", 2),
                item("3", 3));

        when(groceryOperations.listItems()).thenReturn(items);
        CommandExecutor testee = new CommandExecutor(output, groceryOperations);
        testee.execute(new String[]{CommandExecutor.LIST_COMMAND});
        verify(output).println(CommandExecutor.ITEM_LIST_START_MESSAGE);
        items.forEach(i -> verify(output).println(String.format(CommandExecutor.ITEM_LIST_CONTENT_MESSAGE, i.getName(), i.getUnit(), i.getCost().toString())));
        verify(output).println(CommandExecutor.ITEM_LIST_END_MESSAGE);
    }

    @Test
    void unknownCommand() {
        CommandExecutor testee = new CommandExecutor(output, groceryOperations);
        String[] input = {"bibble", "bobble", "boing"};
        testee.execute(input);
        verify(output).println(String.format(CommandExecutor.ERROR_MESSAGE, Arrays.toString(input)));
    }
}