import minesweeper.Cell;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    void testCellProperties() {
        Cell cell = new Cell();
        assertFalse(cell.isMine());
        assertFalse(cell.isOpened());
        assertFalse(cell.isFlagged());
        assertEquals(0, cell.getNeighbourMines());
    }

    @Test
    void testSettersAndGetters() {
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
