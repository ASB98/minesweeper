package minesweeper;

import java.util.Scanner;

public class MinesweeperCLI {
    private static GameBoard board;
    private static boolean gameLost = false;
    private static boolean isFirstMove = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        startNewGame(scanner);
    }

    private static void startNewGame(Scanner scanner) {
        System.out.println("Select difficulty (1 - Easy, 2 - Medium, 3 - Hard):");
        int difficulty = scanner.nextInt();
        int width, height, mineCount;
        switch (difficulty) {
            case 1: //easy
                width = 10; height = 10; mineCount = 10;
                break;
            case 2: //medium
                width = 16; height = 16; mineCount = 40;
                break;
            case 3: //hard
                width = 30; height = 16; mineCount = 99;
                break;
            default:
                System.out.println("Invalid difficulty. Defaulting to Easy.");
                width = 9; height = 9; mineCount = 10;
        }
        board = new GameBoard(width, height, mineCount);
        scanner.nextLine();

        gameLoop(scanner);
    }

    private static void checkAndHandleWin(Scanner scanner) {
        if (board.checkWin()) {
            board.printBoard(true); //show the entire board
            System.out.println("Congratulations, you've won! Play again? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                startNewGame(scanner);
            } else {
                System.out.println("Thank you for playing!");
                System.exit(0);
            }
        }
    }

    private static void gameLoop(Scanner scanner) {
        gameLost = false;
        isFirstMove = true;
        while (!gameLost) {
            board.printBoard(false); //do not show mines unless game is lost
            System.out.println("Enter command (o x y to open a cell, f x y to flag a cell, q to quit): ");
            String input = scanner.nextLine();
            if ("q".equals(input)) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length == 3) {
                int x = Integer.parseInt(parts[1]) - 1; //adjust for 1-based indexing
                int y = Integer.parseInt(parts[2]) - 1;
                switch (parts[0]) {
                    case "o":
                        if (isFirstMove) {
                            board.placeMinesDynamically(x, y);
                            isFirstMove = false;
                        }
                        if (board.openCell(x, y)) {
                            gameLost = true;
                            board.printBoard(true); //show mines
                            System.out.println("Game over! You hit a mine. Try again? (y/n): ");
                            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                                startNewGame(scanner);
                            } else {
                                System.exit(0);
                            }
                        } else {
                            checkAndHandleWin(scanner);
                        }
                        break;
                    case "f":
                        board.flagCell(x, y);
                        checkAndHandleWin(scanner);
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

}

