package minesweeper;

import java.util.Random;

public class GameBoard {
    //2D array to store cells
    //Only dealing with arrays as everything is a fixed size
    private Cell[][] cells;
    private int width;
    private int height;
    private int mineCount;

    //constructor to initialise GB with data members
    public GameBoard(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        cells = new Cell[height][width]; //initialise cell array with given height and width
        initializeBoard();
    }

    //loop through board and place empty cells in each position
    private void initializeBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(); //create new cell object for each position
            }
        }
    }


    //method to place mines after first move to prevent instadeath
    public void placeMinesDynamically(int firstX, int firstY) {
        Random random = new Random();
        int placedMines = 0;

        //mineCount set by user depending on difficulty, place randomly while count not met but only in valid cells
        while (placedMines < mineCount) {
            int x = random.nextInt(width); //random x coordinate for mine placement
            int y = random.nextInt(height); //random y coordinate for mine placement

            //check if the randomly chosen cell is not the first clicked cell or its neighbours
            //if not first cell or immediately neighbour to first or already mined, then place mine
            if (!isNeighbourOrSelf(firstX, firstY, x, y) && !cells[y][x].isMine()) {
                cells[y][x].setMine(true); //set mine at valid cell
                placedMines++; //increment number of mines successfully placed
            }
        }
        calculateNeighbourMines(); //calculate mine adjacency after all mines are placed
    }

    //determine if cell is first one clicked or if it is neighbour
    private boolean isNeighbourOrSelf(int firstX, int firstY, int x, int y) {
        return Math.abs(firstX - x) <= 1 && Math.abs(firstY - y) <= 1; //return true if neighbour or self
    }

    //updates each cell's neighbour mine count after mines have been placed
    private void calculateNeighbourMines() {
        //loop through board and check if not mine, count mines and mark adjacency count
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!cells[y][x].isMine()) {
                    int mines = countNeighbourMines(x, y); //count neighbour mines for each cell
                    cells[y][x].setNeighbourMines(mines); //set  neighbour mine count for cell
                }
            }
        }
    }

    //count number of mines neighbour to a given cell
    private int countNeighbourMines(int x, int y) {
        int count = 0;
        //loop through neighbour mines, all 8 directions around mine
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int numX = x + i;
                int numY = y + j;
                //check bounds and if the neighbouring cell is a mine
                if (numX >= 0 && numX < width && numY >= 0 && numY < height && cells[numY][numX].isMine()) {
                    count++; //increment count if neighbouring cell is a mine
                }
            }
        }
        return count;
    }

    //opens a cell at the specified coordinates, returning true if it's a mine -> game over
    public boolean openCell(int x, int y) {
        //check if coordinates are in bounds
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false; //out of bounds, do nothing
        }

        //retrieve cell object from 2D array
        Cell cell = cells[y][x];

        //if already opened or flagged, do nothing and just return
        if (cell.isOpened() || cell.isFlagged()) {
            return false;
        }

        //if previous checks pass then set cell as open
        cell.setOpened(true);

        //game over if cell is a mine
        if (cell.isMine()) {
            return true;
        }

        //if no neighbour mines, recursively open neighbour cells
        //recursion continues until cells with neighbour mines are found
        if (cell.getNeighbourMines() == 0) {
            //iterate over neighbour rows relative to current cell
            for (int i = -1; i <= 1; i++) {
                //iterate over neighbour columns relative to current cell
                for (int j = -1; j <= 1; j++) {
                    //do not consider current cell for opening
                    if (i != 0 || j != 0) {
                        //recursion for neighbour cells
                        openCell(x + i, y + j);
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

        //looping through all cells
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
        System.out.print("  "); //adjust starting space based on row label width
        for (int x = 0; x < width; x++) {
            System.out.printf(" %3d", x + 1); //use 3 spaces for each column index to account for double digits
        }
        System.out.println("\n   ┌───" + "┬───".repeat(width - 1) + "┐");

        for (int y = 0; y < height; y++) {
            //print row index with padding
            System.out.printf("%2d │", y + 1);
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (cell.isOpened() || (showMines && cell.isMine())) {
                    if (cell.isMine()) {
                        System.out.print("\u001B[41m" + " * " + "\u001B[0m");//reveal mine with red background
                    } else {
                        System.out.print(getColoredNumber(cell.getNeighbourMines()));//print neighbour mine count with colours
                    }
                } else if (cell.isFlagged()) {
                    System.out.print(" F ");//print flagged cell
                } else {
                    System.out.print(" . ");//print unopened cell
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

    //method for conditional formatting of neighbour mine numbers with ansi colour formatting
    //ENHANCED SWITCH, thanks for the recommendation intellij
    private String getColoredNumber(int neighbourMines) {
        String defaultColour = "\u001B[0m";
        String colorCode = switch (neighbourMines) {
            //switch cases are number of neighbouring mines
            case 1 -> "\u001B[34m"; //blue
            case 2 -> "\u001B[32m"; //green
            case 3 -> "\u001B[31m"; //red
            case 4 -> "\u001B[35m"; //purple
            case 5 -> "\u001B[33m"; //yellow
            default -> defaultColour; //default color - white
        };
        return colorCode + (neighbourMines > 0 ? " " + neighbourMines + " " : "\u001B[47m" + "   " + defaultColour) + defaultColour; //if no neighbour mines blank cell
    }

    //method to get cells, only used for testing
    public Cell[][] getCells() {
        return cells;
    }


}

