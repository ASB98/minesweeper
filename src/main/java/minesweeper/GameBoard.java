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

    public void revealCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || cells[y][x].isRevealed() || cells[y][x].isFlagged()) {
            return; //out of bounds, already revealed, or flagged cells are ignored
        }

        cells[y][x].setRevealed(true);

        if (cells[y][x].getAdjacentMines() == 0 && !cells[y][x].isMine()) {
            //recursively reveal adjacent cells if this cell has no adjacent mines
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) {
                        revealCell(x + i, y + j);
                    }
                }
            }
        }
    }

    public void flagCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height || cells[y][x].isRevealed()) {
            return; //out of bounds or already revealed cells are ignored
        }

        cells[y][x].setFlagged(!cells[y][x].isFlagged()); //toggle flag
    }

    public void printBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = cells[y][x];
                if (cell.isRevealed()) {
                    if (cell.isMine()) {
                        System.out.print("* ");
                    } else {
                        System.out.print(cell.getAdjacentMines() + " ");
                    }
                } else if (cell.isFlagged()) {
                    System.out.print("F ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}

