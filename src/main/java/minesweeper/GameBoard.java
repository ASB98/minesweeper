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
        //placeMines(); //removed so there is no instadeath
        //calculateAdjacentMines();
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

    public void placeMinesDynamically(int firstX, int firstY) {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mineCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            //check if the randomly chosen cell is not the first clicked cell or its neighbors
            if (!isNeighborOrSelf(firstX, firstY, x, y) && !cells[y][x].isMine()) {
                cells[y][x].setMine(true);
                placedMines++;
            }
        }
        calculateAdjacentMines(); //recalculate mine adjacency after all mines are placed
    }

    private boolean isNeighborOrSelf(int firstX, int firstY, int x, int y) {
        return Math.abs(firstX - x) <= 1 && Math.abs(firstY - y) <= 1;
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

    public boolean openCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false; //out of bounds
        }
        Cell cell = cells[y][x];
        if (cell.isOpened() || cell.isFlagged()) {
            return false; //already opened or flagged
        }

        cell.setOpened(true);
        if (cell.isMine()) {
            return true; //game over
        }

        //if no adjacent mines, recursively open adjacent cells
        if (cell.getAdjacentMines() == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        openCell(x + i, y + j);//recursion for adjacent cells
                    }
                }
            }
        }
        return false; //no mine opened
    }

    public void flagCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || cells[y][x].isOpened()) {
            return; //out of bounds or already opened cells are ignored
        }

        cells[y][x].setFlagged(!cells[y][x].isFlagged()); //toggle flag
    }

    public boolean checkWin() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (!cell.isMine() && !cell.isOpened()) {
                    return false; //found a non-mine cell that's not opened yet
                }
            }
        }
        return true; //all non-mine cells have been opened
    }


    public void printBoard(boolean showMines) {
        //print column indexes
        System.out.print("  "); //adjust starting space based on row label width
        for (int x = 0; x < width; x++) {
            System.out.printf(" %3d", x + 1); //use 3 spaces for each column index
        }
        System.out.println("\n   ┌───" + "┬───".repeat(width - 1) + "┐");

        for (int y = 0; y < height; y++) {
            //print row index with padding
            System.out.printf("%2d │", y + 1);
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (cell.isOpened() || (showMines && cell.isMine())) {
                    if (cell.isMine()) {
                        System.out.print(" * ");
                    } else {
                        System.out.print(cell.getAdjacentMines() > 0 ? " " + cell.getAdjacentMines() + " " : "   ");
                    }
                } else if (cell.isFlagged()) {
                    System.out.print(" F ");
                } else {
                    System.out.print(" . ");
                }
                if (x < width - 1) {
                    System.out.print("│");
                }
            }
            System.out.println("│");
            if (y < height - 1) {
                System.out.println("   ├───" + "┼───".repeat(width - 1) + "┤");
            }
        }

        System.out.println("   └───" + "┴───".repeat(width - 1) + "┘");
    }


}

