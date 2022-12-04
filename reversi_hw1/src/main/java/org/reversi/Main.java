package org.reversi;

import java.util.Scanner;

import static java.lang.Math.max;

public class Main {
    final private static Scanner scanner = new Scanner(System.in);
    private static int maxScore = 0;

    public static void aiVsAi() {
        int computerDifficulty = readComputerDifficulty();
        Computer player1 = new Computer('o', computerDifficulty);
        Computer player2 = new Computer('*', computerDifficulty);
        GameField gameField = new GameField(player1.symbol, player2.symbol);
        gameField.printField();
        do {
            player1.doMove(gameField, player2.symbol);
            gameField.printField();
            if (gameField.findAvailableMoves(player2.symbol, player1.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            System.out.println("Player1 made move.\nEnter any symbol to continue.");
            scanner.next();
            player2.doMove(gameField, player1.symbol);
            gameField.printField();
            if (gameField.findAvailableMoves(player1.symbol, player2.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            System.out.println("Player2 made move.\nEnter any symbol to continue.");
            scanner.next();
        } while (true);
        maxScore = max(maxScore, gameField.getMaxScore());
    }

    public static void humanVsHuman() {
        Human player1 = new Human('o');
        Human player2 = new Human('*');
        GameField gameField = new GameField(player1.symbol, player2.symbol);
        do {
            player1.doMove(gameField, player2.symbol);
            gameField.printField();
            if (gameField.findAvailableMoves(player2.symbol, player1.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            System.out.println("Player1 made move.");
            player2.doMove(gameField, player1.symbol);
            gameField.printField();
            if (gameField.findAvailableMoves(player1.symbol, player2.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            System.out.println("Player2 made move.");
        } while (true);
        maxScore = max(maxScore, gameField.getMaxScore());
    }

    public static void humanVsAi() {
        int computerDifficulty = readComputerDifficulty();
        Human player1 = new Human('o');
        Computer player2 = new Computer('*', 0);
        GameField gameField = new GameField(player1.symbol, player2.symbol);
        do {
            player1.doMove(gameField, player2.symbol);
            gameField.printField();
            if (gameField.findAvailableMoves(player2.symbol, player1.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            player2.doMove(gameField, player1.symbol);
            if (gameField.findAvailableMoves(player1.symbol, player2.symbol).isEmpty()) {
                gameField.printEndGameStatus();
                break;
            }
            System.out.println("Enemy made move.");
        } while (true);
        maxScore = max(maxScore, gameField.getMaxScore());
    }

    public static int readComputerDifficulty() {
        System.out.println("Enter computer difficulty (0 or 1)");
        int computerDifficulty = 0;
        do {
            try {
                computerDifficulty = scanner.nextInt();
            } catch (Throwable e) {
                continue;
            }
        } while (!(computerDifficulty >= 0 && computerDifficulty <= 1));
        return computerDifficulty;
    }

    public static void setMaxScore(int score) {
        maxScore = score;
    }

    public static void printResults() {
        System.out.printf("Maximal score: %d. Congratulations!", maxScore);
    }

    public static void main(String[] args) {
        do {
            System.out.println("""
                                        
                    Select gamemode:
                    1. Human vs. Ai
                    2. Human vs. Human
                    3. Ai vs. Ai
                    0. Exit""");
            int gameType = scanner.nextInt();

            switch (gameType) {
                case 1 -> humanVsAi();
                case 2 -> humanVsHuman();
                case 3 -> aiVsAi();
            }
            if (gameType == 0) {
                printResults();
                break;
            }
        } while (true);
    }
}