package minesweeper;

import java.util.Random;

public class GameBoard {
    private Cell[][] cells;
    private int width;
    private int height;
    private int mineCount;

    public GameBoard(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        cells = new Cell[height][width];
        initializeBoard();
        placeMines();
        calculateAdjacentMines();
    }

    private void initializeBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < mineCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (!cells[y][x].isMine()) {
                cells[y][x].setMine(true);
                placedMines++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!cells[y][x].isMine()) {
                    int mines = countAdjacentMines(x, y);
                    cells[y][x].setAdjacentMines(mines);
                }
            }
        }
    }

    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && cells[ny][nx].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean revealCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false; //out of bounds
        }
        Cell cell = cells[y][x];
        if (cell.isRevealed() || cell.isFlagged()) {
            return false; //already revealed or flagged
        }

        cell.setRevealed(true);
        if (cell.isMine()) {
            return true; //game over
        }

        //if no adjacent mines, recursively reveal adjacent cells
        if (cell.getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revealCell(x + i, y + j);//recursion for adjacent cells
                    }
                }
            }
        }
        return false; //no mine revealed
    }

    public void flagCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || cells[y][x].isRevealed()) {
            return; //out of bounds or already revealed cells are ignored
        }

        cells[y][x].setFlagged(!cells[y][x].isFlagged()); //toggle flag
    }

    public void printBoard(boolean showMines) {
        //print column indexes with proper spacing
        System.out.print("    "); //initial spacing for row labels
        for (int x = 0; x < width; x++) {
            System.out.printf("%3d", x + 1);
        }
        System.out.println();

        //print top border of the board
        System.out.print("    "); //align with column indexes
        for (int x = 0; x < width; x++) {
            System.out.print("---");
        }
        System.out.println();

        for (int y = 0; y < height; y++) {
            //print row index
            System.out.printf("%3d|", y + 1);

            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (showMines && cell.isMine()) {
                    System.out.print(" * ");
                } else if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print(" * ");
                    } else {
                        System.out.printf("%3d", cell.getAdjacentMines());
                    }
                } else if (cell.isFlagged()) {
                    System.out.print(" F ");
                } else {
                    System.out.print(" . ");
                }
            }
            System.out.println(); //new line at the end of each row
        }
    }
}

