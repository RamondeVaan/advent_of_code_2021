package nl.ramondevaan.aoc2021.day04;

public class Position {
    public final int row;
    public final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static Position of(int row, int column) {
        return new Position(row, column);
    }
}
