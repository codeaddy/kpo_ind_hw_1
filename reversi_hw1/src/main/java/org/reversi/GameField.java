package org.reversi;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class GameField {
    final private int width = 8;
    final private int height = 8;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private int maxScore = 0;
    private ArrayList<ArrayList<Cell>> field;
    private ArrayList<Cell> lastAvailableMoves = new ArrayList<>();
    final private ArrayList<ArrayList<ArrayList<Cell>>> moveHistory = new ArrayList<>();
    final private char symbolPlayer1;
    final private char symbolPlayer2;
    final static private ArrayList<Integer> DX = new ArrayList<>(List.of(1, 1, 1, 0, 0, -1, -1, -1));
    final static private ArrayList<Integer> DY = new ArrayList<>(List.of(1, 0, -1, 1, -1, 1, 0, -1));

    {
        field = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < height; i++) {
            field.add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++) {
                field.get(i).add(new Cell(i, j, ' '));
            }
        }
    }

    GameField(char symbol1, char symbol2) {
        symbolPlayer1 = symbol1;
        symbolPlayer2 = symbol2;
        field.get(height / 2 - 1).get(width / 2).setSymbol(symbol1);
        field.get(height / 2).get(width / 2 - 1).setSymbol(symbol1);
        field.get(height / 2 - 1).get(width / 2 - 1).setSymbol(symbol2);
        field.get(height / 2).get(width / 2).setSymbol(symbol2);
    }

    public void printField() {
        System.out.print("\\");
        for (int i = 0; i < width; i++) {
            System.out.printf("-%d", i + 1);
        }
        System.out.println();
        for (int i = 0; i < field.size(); i++) {
            System.out.printf("%d|", i + 1);
            for (Cell cell : field.get(i)) {
                System.out.printf("%c|", cell.getSymbol());
            }
            System.out.println();
        }
        for (int i = 0; i < width; i++) {
            System.out.print('-');
        }
        System.out.println();
        calculateGameScore();
        System.out.printf("(%c:%d) | (%c:%d)\n", symbolPlayer1, scorePlayer1, symbolPlayer2, scorePlayer2);
    }

    public void calculateGameScore() {
        scorePlayer1 = scorePlayer2 = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char current;
                if ((current = field.get(i).get(j).getSymbol()) == symbolPlayer1) {
                    scorePlayer1++;
                } else if (current == symbolPlayer2) {
                    scorePlayer2++;
                }
            }
        }
    }

    public void printAvailableMoves(char mySymbol, char enemySymbol) {
        ArrayList<Cell> availableMoves = findAvailableMoves(mySymbol, enemySymbol);
        for (Cell cell : availableMoves) {
            field.get(cell.getX()).get(cell.getY()).setSymbol('?');
        }
        printField();
        for (Cell cell : availableMoves) {
            field.get(cell.getX()).get(cell.getY()).setSymbol(' ');
        }
    }

    public void printEndGameStatus() {
        maxScore = max(scorePlayer1, scorePlayer2);
        if (scorePlayer1 > scorePlayer2) {
            System.out.printf("Player1 has won with %d points", scorePlayer1);
        } else {
            System.out.printf("Player2 has won with %d points", scorePlayer2);
        }
    }

    public ArrayList<Cell> findAvailableMoves(char mySymbol, char enemySymbol) {
        ArrayList<Cell> availableMoves = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (field.get(i).get(j).getSymbol() == enemySymbol) {
                    for (int q = 0; q < 8; q++) {
                        int newX = i + DX.get(q);
                        int newY = j + DY.get(q);
                        if (newX >= 0 && newX < height && newY >= 0 && newY < width
                                && field.get(newX).get(newY).getSymbol() == ' ') {
                            if (!(calculateMove(mySymbol, enemySymbol, new Cell(newX, newY, ' ')) < 1)) {
                                availableMoves.add(new Cell(newX, newY, ' '));
                            }
                        }
                    }
                }
            }
        }
        lastAvailableMoves = availableMoves;
        return availableMoves;
    }

    public double calculateMove(char mySymbol, char enemySymbol, Cell move) {
        double totalScore = 0;
        for (int q = 0; q < 8; q++) {
            double score = getNeighboursCount(move) == 3 ? 0.8 : getNeighboursCount(move) == 5 ? 0.4 : 0;
            int enemyCellsCount = 0;
            int newX = move.getX();
            int newY = move.getY();
            do {
                newX += DX.get(q);
                newY += DY.get(q);
                if (!(newX >= 0 && newX < height && newY >= 0 && newY < width)) {
                    break;
                }
                if (field.get(newX).get(newY).getSymbol() == mySymbol) {
                    if (enemyCellsCount > 0) {
                        totalScore += score;
                    }
                    break;
                } else if (field.get(newX).get(newY).getSymbol() == enemySymbol) {
                    score += (getNeighboursCount(new Cell(newX, newY, ' ')) < 8 ? 2 : 1);
                    enemyCellsCount++;
                } else {
                    break;
                }
            } while (true);
        }
        return totalScore;
    }

    public int getNeighboursCount(Cell cell) {
        int count = 0;
        for (int q = 0; q < 8; q++) {
            int newX = cell.getX() + DX.get(q);
            int newY = cell.getY() + DY.get(q);
            if (newX >= 0 && newX < height && newY >= 0 && newY < width) {
                count++;
            }
        }
        return count;
    }

    private void saveField() {
        moveHistory.add(new ArrayList<ArrayList<Cell>>());
        for (int i = 0; i < height; i++) {
            moveHistory.get(moveHistory.size() - 1).add(new ArrayList<Cell>());
            for (int j = 0; j < width; j++) {
                moveHistory.get(moveHistory.size() - 1).get(i).add(new Cell(i, j, field.get(i).get(j).getSymbol()));
            }
        }
    }

    public void makeMove(char mySymbol, char enemySymbol, Cell move) throws IncorrectInputException {
        findAvailableMoves(mySymbol, enemySymbol);
        if (!lastAvailableMoves.contains(new Cell(move.getX(), move.getY(), ' '))) {
            throw new IncorrectInputException("You can't do that move!");
        }
        saveField();
        field.get(move.getX()).get(move.getY()).setSymbol(mySymbol);
        for (int q = 0; q < 8; q++) {
            ArrayDeque<Cell> enemyCells = new ArrayDeque<>();
            int newX = move.getX();
            int newY = move.getY();
            do {
                newX += DX.get(q);
                newY += DY.get(q);
                if (!(newX >= 0 && newX < height && newY >= 0 && newY < width)) {
                    break;
                }
                if (field.get(newX).get(newY).getSymbol() == mySymbol) {
                    if (!enemyCells.isEmpty()) {
                        for (Cell cell : enemyCells) {
                            field.get(cell.getX()).get(cell.getY()).setSymbol(mySymbol);
                        }
                    }
                    break;
                } else if (field.get(newX).get(newY).getSymbol() == enemySymbol) {
                    enemyCells.add(new Cell(newX, newY, enemySymbol));
                } else {
                    break;
                }
            } while (true);
        }
    }

    public void undoMove() {
        if (moveHistory.isEmpty()) {
            return;
        }
        field = moveHistory.get(moveHistory.size() - 1);
        moveHistory.remove(moveHistory.size() - 1);
    }

    public int getMaxScore() {
        return maxScore;
    }
}
