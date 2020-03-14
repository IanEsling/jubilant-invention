import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.fail;
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

    @Test
    void quitIfCommandExecutorSaysSo() {
        when(commandExecutor.execute(any())).thenReturn(false);
        InputStream input = new ByteArrayInputStream("1 2 3".getBytes());
        new HenrysGrocery(input, output, commandExecutor).run(new TimeLimitedRunner(5));
        verify(output, times(1)).println(HenrysGrocery.QUIT_MESSAGE);
    }

    static class TimeLimitedRunner implements HenrysGrocery.KeepRunning {

        final LocalDateTime start = LocalDateTime.now();
        private final long limit;

        TimeLimitedRunner(long limit) {
            this.limit = limit;
        }

        @Override
        public boolean keepRunning() {
            if (Duration.between(start, LocalDateTime.now()).getSeconds() < limit) {
                return true;
            } else {
                fail("grocery has run for longer than the limit of " + limit + " seconds.");
            }
            return false;
        }
    }

    static class RunOnce implements HenrysGrocery.KeepRunning {
        int i = 0;

        @Override
        public boolean keepRunning() {
            return i++ == 0;
        }
    }

}