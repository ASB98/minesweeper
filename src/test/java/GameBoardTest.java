import minesweeper.GameBoard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    @Test
    public void testFlagCell() {
        //check cell can be flagged and un-flagged
        GameBoard gameBoard = new GameBoard(9, 9, 10);
        gameBoard.flagCell(0, 0);
        assertTrue(gameBoard.getCells()[0][0].isFlagged());
        gameBoard.flagCell(0, 0);
        assertFalse(gameBoard.getCells()[0][0].isFlagged());
    }

    @Test
    public void testCheckWin() {
        //manually setting up board with mines
        GameBoard gameBoard = new GameBoard(3, 3, 2);
        gameBoard.getCells()[0][0].setMine(false);
        gameBoard.getCells()[0][1].setMine(true);
        gameBoard.getCells()[0][2].setMine(false);
        gameBoard.getCells()[1][0].setMine(true);
        gameBoard.getCells()[1][1].setMine(false);
        gameBoard.getCells()[1][2].setMine(false);
        gameBoard.getCells()[2][0].setMine(false);
        gameBoard.getCells()[2][1].setMine(false);
        gameBoard.getCells()[2][2].setMine(false);

        //flag both mined cells
        gameBoard.flagCell(0, 1);
        gameBoard.flagCell(1, 0);
        assertTrue(gameBoard.checkWin());
    }

    @Test
    public void testOpenCell() {
        GameBoard gameBoard = new GameBoard(9, 9, 10);
        //opening a cell without a mine
        assertFalse(gameBoard.openCell(0, 0));
        assertTrue(gameBoard.getCells()[0][0].isOpened());

        //opening a cell with neighbour mines
        gameBoard.getCells()[0][1].setNeighbourMines(3);
        assertFalse(gameBoard.openCell(0, 1));
        assertTrue(gameBoard.getCells()[0][1].isOpened());
    }

    @Test
    public void testLosingCondition() {
        GameBoard gameBoard = new GameBoard(3, 3, 1);
        gameBoard.placeMinesDynamically(0, 0); //placing mines at random

        //find the coordinates of the mine
        //goodness gracious, I could really do with an index of mined cells
        int mineX = 0;
        int mineY = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameBoard.getCells()[i][j].isMine()) {
                    mineX = j;
                    mineY = i;
                    break;
                }
            }
        }

        //open the cell with the mine
        assertTrue(gameBoard.openCell(mineX, mineY)); //should return true when game over

        //ensure the cell with the mine is opened
        assertTrue(gameBoard.getCells()[mineY][mineX].isOpened());

        //ensure all other cells are still closed
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!(i == mineY && j == mineX)) {
                    assertFalse(gameBoard.getCells()[i][j].isOpened());
                }
            }
        }
    }


}
