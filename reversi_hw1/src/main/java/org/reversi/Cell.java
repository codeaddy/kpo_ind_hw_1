package org.reversi;

public class Cell {
    private char symbol;
    final private int x, y;

    Cell(int x, int y, char symbol) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
    }

    char getSymbol() {
        return symbol;
    }

    void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Cell) && (this.x == ((Cell) o).x) && (this.y == ((Cell) o).y)
                && (this.symbol == ((Cell) o).symbol);
    }
}
