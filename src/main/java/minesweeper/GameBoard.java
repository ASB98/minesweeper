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

    //initialise cells as empty
    private void initializeBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(); //create new cell object for each position
            }
        }
    }

    private void placeMines() { //deprecated due to instadeath
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

    //determine if cell is first one clicked or if it is neighbour
    private boolean isNeighborOrSelf(int firstX, int firstY, int x, int y) {
        return Math.abs(firstX - x) <= 1 && Math.abs(firstY - y) <= 1;
    }

    //updates each cell's adjacent mine count after mines have been placed
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

    //count number of mines adjacent to a given cell
    private int countAdjacentMines(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int nx = x + i;
                int ny = y + j;
                //check bounds and if the neighbouring cell is a mine
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && cells[ny][nx].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    //opens a cell at the specified coordinates, returning true if it's a mine - game over
    public boolean openCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) { //check if coordinates are in bounds
            return false; //out of bounds
        }
        Cell cell = cells[y][x];
        if (cell.isOpened() || cell.isFlagged()) {
            return false; //already opened or flagged
        }

        cell.setOpened(true);
        if (cell.isMine()) {
            return true; //game over if cell is mine
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

    //toggle flag status of a cell
    public void flagCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || cells[y][x].isOpened()) {
            return; //out of bounds or already opened cells are ignored
        }

        cells[y][x].setFlagged(!cells[y][x].isFlagged()); //toggle flag
    }

    //check if all non-mine cells have been opened
    public boolean checkWin() {
        int correctlyFlaggedMines = 0;
        int totalOpenedCells = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                //check if a mine cell is correctly flagged
                if (cell.isMine() && cell.isFlagged()) {
                    correctlyFlaggedMines++;
                }
                //count opened cells
                if (cell.isOpened()) {
                    totalOpenedCells++;
                }
            }
        }

        //win conditions: all mines are correctly flagged or all non-mine cells are opened
        boolean allMinesCorrectlyFlagged = correctlyFlaggedMines == mineCount;
        boolean allNonMineCellsOpened = totalOpenedCells == (width * height - mineCount);

        return allMinesCorrectlyFlagged || allNonMineCellsOpened;
    }

    //print current board state to console
    public void printBoard(boolean showMines) {
        System.out.print("  "); // Adjust starting space based on row label width
        for (int x = 0; x < width; x++) {
            System.out.printf(" %3d", x + 1); // Use 3 spaces for each column index
        }
        System.out.println("\n   ┌───" + "┬───".repeat(width - 1) + "┐");

        for (int y = 0; y < height; y++) {
            // Print row index with padding
            System.out.printf("%2d │", y + 1);
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (cell.isOpened() || (showMines && cell.isMine())) {
                    if (cell.isMine()) {
                        System.out.print("\u001B[41m" + "   " + "\u001B[0m");
                    } else {
                        System.out.print(getColoredNumber(cell.getAdjacentMines()));
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

    //conditional formatting of adjacent mine numbers
    //ENHANCED SWITCH, thanks for the recommendation intellij
    private String getColoredNumber(int adjacentMines) {
        String colorCode = switch (adjacentMines) {
            case 1 -> "\u001B[34m";//blue
            case 2 -> "\u001B[32m";//green
            case 3 -> "\u001B[31m";//red
            case 4 -> "\u001B[35m";//purple
            case 5 -> "\u001B[33m";//yellow
            default -> "\u001B[0m";//default color
        };
        return colorCode + (adjacentMines > 0 ? " " + adjacentMines + " " : "\u001B[47m" + "   " + "\u001B[0m") + "\u001B[0m";
    }



}

