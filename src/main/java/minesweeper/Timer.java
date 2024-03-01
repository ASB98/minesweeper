package minesweeper;

public class Timer {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public long getElapsedTimeSeconds() {
        long elapsedTimeMilliseconds = endTime - startTime;
        return elapsedTimeMilliseconds / 1000; // Convert milliseconds to seconds
    }
}
