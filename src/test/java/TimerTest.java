import minesweeper.Timer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    void testElapsedTime() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();
        TimeUnit.SECONDS.sleep(1);
        timer.stop();
        assertEquals(1, timer.getElapsedTimeSeconds());
    }

}
