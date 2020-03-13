import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HenrysGroceryTest {

    @Mock
    GroceryOperations groceryOperations;
    @Mock
    PrintStream output;

    @Test
    void executeCommand() throws IOException {
        InputStream input = new ByteArrayInputStream("1 2 3".getBytes());
        new HenrysGrocery(input, output, groceryOperations).run(new RunOnce());
//        verify(commandExecutor).executeCommand(new String[]{"1", "2", "3"});
    }

    @Test
    void unableToProcessInput() throws IOException {
//        doThrow(IOException.class).when(commandExecutor).executeCommand(any());
        InputStream input = new ByteArrayInputStream("1 2 3".getBytes());
        new HenrysGrocery(input, output, groceryOperations).run(new RunOnce());
        verify(output, times(1)).println(String.format(HenrysGrocery.ERROR_MESSAGE, "[1, 2, 3]"));
        verify(output, times(1)).println(HenrysGrocery.USAGE_MESSAGE);
    }

    static class RunOnce implements HenrysGrocery.KeepRunning {
        int i = 0;

        @Override
        public boolean keepRunning() {
            return i++ == 0;
        }
    }

}