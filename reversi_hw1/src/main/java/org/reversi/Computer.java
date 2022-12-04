package org.reversi;

public class Computer extends Player {
    private final int difficulty;

    Computer(char symbol, int difficulty) {
        super(symbol);
        this.difficulty = difficulty;
    }

    public void doMove(GameField gameField, char enemySymbol) {
        Cell move = nextMoveSelector(gameField, enemySymbol, difficulty);
        System.out.printf("(%d;%d)\n", move.getX() + 1, move.getY() + 1);
        try {
            gameField.makeMove(symbol, enemySymbol, move);
        } catch (Throwable e) {
            System.out.println("Computer is broken :c");
            return;
        }
    }

}
