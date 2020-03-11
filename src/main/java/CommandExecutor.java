import java.util.Collection;
import java.util.List;

public class CommandExecutor {

    public Collection<Item> listItems() {
        return List.of(new Item(), new Item(), new Item(), new Item(), new Item());
    }
}
