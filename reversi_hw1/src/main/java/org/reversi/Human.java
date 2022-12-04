package org.reversi;

import java.util.Scanner;

public class Human extends Player {
    Human(char symbol) {
        super(symbol);
    }

    public void doMove(GameField gameField, char enemySymbol) {
        do {
            Cell move = requestMove(gameField, enemySymbol);
            try {
                if (move.getX() == -1 && move.getY() == -1) {
                    gameField.undoMove();
                    continue;
                } else {
                    gameField.makeMove(symbol, enemySymbol, move);
                }
            } catch (Throwable e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        } while (true);
    }

    private Cell requestMove(GameField gameField, char enemySymbol) {
        System.out.println("Your available moves:");
        gameField.printAvailableMoves(symbol, enemySymbol);
        System.out.print("\nEnter coordinates of your move (or enter '0 0' to recover previous game status): ");
        Scanner myInput = new Scanner(System.in);
        int x = myInput.nextInt();
        int y = myInput.nextInt();
        x--;
        y--;
        return new Cell(x, y, symbol);
    }

}
