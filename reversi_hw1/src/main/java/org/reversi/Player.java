package org.reversi;

import java.util.ArrayList;

abstract public class Player {
    final protected char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    abstract public void doMove(GameField gameField, char enemySymbol);

    final Cell nextMoveSelector(GameField gameField, char enemySymbol, int difficulty) {
        ArrayList<Cell> moves = gameField.findAvailableMoves(symbol, enemySymbol);
        double mx = -2;
        Cell bestMove = new Cell(0, 0, symbol);
        double temp = 0;
        if (difficulty == 0) {
            for (var move : moves) {
                if ((temp = gameField.calculateMove(symbol, enemySymbol, move)) > mx) {
                    mx = temp;
                    bestMove = move;
                }
            }
        } else {
            for (var move : moves) {
                temp = gameField.calculateMove(symbol, enemySymbol, move);
                try {
                    gameField.makeMove(symbol, enemySymbol, move);
                } catch (Throwable e) {
                    continue;
                }
                ArrayList<Cell> newMoves = gameField.findAvailableMoves(enemySymbol, symbol);
                double enemyMx = 0;
                double enemyTemp = 0;
                for (var newMove : newMoves) {
                    if ((enemyTemp = gameField.calculateMove(enemySymbol, symbol, newMove)) > enemyMx) {
                        enemyMx = enemyTemp;
                    }
                }
                temp -= enemyMx;
                if (temp > mx) {
                    mx = temp;
                    bestMove = move;
                }
                gameField.undoMove();
            }
        }
        return bestMove;
    }
}
