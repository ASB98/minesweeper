import minesweeper.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellTest {

    @Test
    public void testCellProperties() {
        Cell cell = new Cell();
        assertFalse(cell.isMine());
        assertFalse(cell.isOpened());
        assertFalse(cell.isFlagged());
        assertEquals(0, cell.getNeighbourMines());
    }

    @Test
    public void testSettersAndGetters() {
        Cell cell = new Cell();
        cell.setMine(true);
        cell.setOpened(true);
        cell.setFlagged(true);
        cell.setNeighbourMines(3);
        assertTrue(cell.isMine());
        assertTrue(cell.isOpened());
        assertTrue(cell.isFlagged());
        assertEquals(3, cell.getNeighbourMines());
    }


}
