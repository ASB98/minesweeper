import minesweeper.Timer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    void testElapsedTime() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(1000); // Sleep for 1 second - given in milliseconds
        timer.stop();
        assertEquals(1, timer.getElapsedTimeSeconds());
    }

}
