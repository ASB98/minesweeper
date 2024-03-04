package minesweeper;

import java.util.Scanner;
import java.util.InputMismatchException;

//Weird bugs on upper and lower mine density limits

public class MinesweeperCLI {
    private static GameBoard board;
    private static boolean gameLost = false;
    private static boolean isFirstMove = true;
    private static Timer timer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" +
                "███╗░░░███╗██╗███╗░░██╗███████╗░██████╗░██╗░░░░░░░██╗███████╗███████╗██████╗░███████╗██████╗░\n" +
                "████╗░████║██║████╗░██║██╔════╝██╔════╝░██║░░██╗░░██║██╔════╝██╔════╝██╔══██╗██╔════╝██╔══██╗\n" +
                "██╔████╔██║██║██╔██╗██║█████╗░░╚█████╗░░╚██╗████╗██╔╝█████╗░░█████╗░░██████╔╝█████╗░░██████╔╝\n" +
                "██║╚██╔╝██║██║██║╚████║██╔══╝░░░╚═══██╗░░████╔═████║░██╔══╝░░██╔══╝░░██╔═══╝░██╔══╝░░██╔══██╗\n" +
                "██║░╚═╝░██║██║██║░╚███║███████╗██████╔╝░░╚██╔╝░╚██╔╝░███████╗███████╗██║░░░░░███████╗██║░░██║\n" +
                "╚═╝░░░░░╚═╝╚═╝╚═╝░░╚══╝╚══════╝╚═════╝░░░░╚═╝░░░╚═╝░░╚══════╝╚══════╝╚═╝░░░░░╚══════╝╚═╝░░╚═╝");
        startNewGame(scanner);
    }

    private static void startNewGame(Scanner scanner) {
        System.out.println("\nSelect difficulty (1 - Easy, 2 - Medium, 3 - Hard, 4 - Custom):");

        try {
            int difficulty = scanner.nextInt();
            switch (difficulty) {
                //these are the official difficulty levels apparently
                case 1: //easy
                    board = new GameBoard(9, 9, 10);
                    break;
                case 2: //medium
                    board = new GameBoard(16, 16, 40);
                    break;
                case 3: //hard
                    board = new GameBoard(30, 16, 99);
                    break;
                case 4: //custom
                    System.out.println("Enter custom width and height (min 5x5, max 50x50):");
                    int width = validIntInput(scanner, 5, 50);
                    int height = validIntInput(scanner, 5, 50);

                    //calculate and display valid density range
                    float minDensity = 100.0f / (width * height); //minimum density for at least one mine
                    float maxDensity = 100.0f * (width * height - 1) / (width * height); //maximum density for at least one non-mine
                    System.out.printf("Enter mine density percentage (valid range: %.2f%% to %.2f%%):%n", minDensity, maxDensity);

                    float mineDensity = validFloatInput(scanner, minDensity, maxDensity);
                    int mineCount = (int) ((width * height) * (mineDensity / 100));
                    board = new GameBoard(width, height, mineCount);
                    break;
                default:
                    //invalid input handling. Prompts for difficulty selection again.
                    System.out.println("Invalid difficulty. Please enter a number between 1 and 4.");
                    scanner.nextLine();
                    startNewGame(scanner); //call startNewGame to prompt for difficulty again.
                    return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values.");
            scanner.nextLine();
            startNewGame(scanner);
            return;
        }
        scanner.nextLine();
        timer = new Timer();//initialise timer
        timer.start();//start timer
        gameLoop(scanner);
    }

    //integer validation within specified range
    private static int validIntInput(Scanner scanner, int min, int max) {
        int input;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer value.");
                scanner.next(); //clear incorrect input
            }
            input = scanner.nextInt();
            if (input < min || input > max) {
                System.out.printf("Please enter a value between %d and %d.%n", min, max);
            }
        } while (input < min || input > max);
        return input;
    }

    private static float validFloatInput(Scanner scanner, float min, float max) {
        float input;
        do {
            while (!scanner.hasNextFloat()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.next(); //clear incorrect input
            }
            input = scanner.nextFloat();
            if (input < min || input > max) {
                System.out.printf("Please enter a value between %.2f and %.2f.%n", min, max);
            }
        } while (input < min || input > max);
        return input;
    }



    private static void checkAndHandleWin(Scanner scanner) {
        if (board.checkWin()) {
            timer.stop(); //stop timer
            board.printBoard(true); //show the entire board
            long elapsedTimeSeconds = timer.getElapsedTimeSeconds();
            System.out.println("Congratulations, you've won!" + "\nTime taken: " + elapsedTimeSeconds + " seconds"  + "\nPlay again? (y/n): ");
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

