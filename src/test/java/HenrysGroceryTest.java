import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HenrysGroceryTest {

    @Mock
    CommandExecutor commandExecutor;
    @Mock
    PrintStream output;

    @Test
    void executeCommand() {
        InputStream input = new ByteArrayInputStream("1 2 3".getBytes());
        new HenrysGrocery(input, output, commandExecutor).run(new RunOnce());
        verify(commandExecutor).execute(new String[]{"1", "2", "3"});
    }

    @Test
    void unableToProcessInput() {
        doThrow(RuntimeException.class).when(commandExecutor).execute(any());
        InputStream input = new ByteArrayInputStream("1 2 3".getBytes());
        new HenrysGrocery(input, output, commandExecutor).run(new RunOnce());
        verify(output, times(1)).println(String.format(CommandExecutor.ERROR_MESSAGE, "[1, 2, 3]"));
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