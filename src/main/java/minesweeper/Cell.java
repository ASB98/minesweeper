package minesweeper;

public class Cell {
    private boolean isMine;
    private boolean isOpened;
    private boolean isFlagged;
    private int neighbourMines;

    //constructor
    public Cell() {
        isMine = false;
        isOpened = false;
        isFlagged = false;
        neighbourMines = 0;
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

    public int getNeighbourMines() {
        return neighbourMines;
    }

    public void setNeighbourMines(int neighbourMines) {
        this.neighbourMines = neighbourMines;
    }
}
