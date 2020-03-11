import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class CommandExecutorTest {

    @Test
    void listItems() {
        CommandExecutor testee = new CommandExecutor();
        Collection<Item> items = testee.listItems();
        assertThat(items).hasSize(5);
    }

}