package minesweeper;

import java.util.Scanner;

public class MinesweeperCLI {
    private static GameBoard board;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter board width, height, and mine count:");
        int width = scanner.nextInt();
        int height = scanner.nextInt();
        int mineCount = scanner.nextInt();
        scanner.nextLine();

        board = new GameBoard(width, height, mineCount);

        while (true) {
            board.printBoard();
            System.out.println("Enter command (r x y to reveal, f x y to flag, q to quit): ");
            String input = scanner.nextLine();
            if ("q".equals(input)) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length == 3) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                switch (parts[0]) {
                    case "r":
                        board.revealCell(x, y);
                        break;
                    case "f":
                        board.flagCell(x, y);
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            } else {
                System.out.println("Invalid command.");
            }
        }

        scanner.close();
    }
}
