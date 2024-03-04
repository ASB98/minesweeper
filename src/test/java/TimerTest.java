import minesweeper.Timer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {

    @Test
    public void testElapsedTime() throws InterruptedException {
        Timer timer = new Timer();
        timer.start();
        Thread.sleep(1000); // Sleep for 1 second - given in milliseconds
        timer.stop();
        assertEquals(timer.getElapsedTimeSeconds(),1);
    }

}
