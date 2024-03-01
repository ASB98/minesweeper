package minesweeper;

public class Cell {
    private boolean isMine;
    private boolean isOpened;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell() {
        isMine = false;
        isOpened = false;
        isFlagged = false;
        adjacentMines = 0;
    }

    //get and set methods
    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
