package nl.ramondevaan.aoc2021.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MutableIntMap {

    private final int[][] map;
    private final int rows;
    private final int columns;
    private final int size;
    private final List<Coordinate> keys;

    public MutableIntMap(IntMap map) {
        this(map.rows(), map.columns());
        for (int row = 0; row < rows; row++) {
            map.copyInto(row, this.map[row]);
        }
    }

    public MutableIntMap(int rows, int columns) {
        this.map = new int[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.size = rows * columns;
        this.keys = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.keys.add(new Coordinate(row, column));
            }
        }
    }

    public Stream<Coordinate> keys() {
        return keys.stream();
    }

    public IntStream values() {
        return IntStream.range(0, rows).flatMap(row -> Arrays.stream(this.map[row], 0, columns));
    }

    public Stream<IntMapEntry> entries() {
        return this.keys.stream().map(coordinate -> new IntMapEntry(
                coordinate,
                this.map[coordinate.row()][coordinate.column()]
        ));
    }

    public void computeIfPresent(Coordinate coordinate, IntUnaryOperator operator) {
        if (contains(coordinate)) {
            this.map[coordinate.row()][coordinate.column()] =
                    operator.applyAsInt(this.map[coordinate.row()][coordinate.column()]);
        }
    }

    public void compute(Coordinate coordinate, IntUnaryOperator operator) {
        this.map[coordinate.row()][coordinate.column()] =
                operator.applyAsInt(this.map[coordinate.row()][coordinate.column()]);
    }

    public void computeAll(IntUnaryOperator operator) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.map[row][column] = operator.applyAsInt(this.map[row][column]);
            }
        }
    }

    public boolean contains(Coordinate coordinate) {
        return coordinate.row() >= 0 && coordinate.row() < rows
                && coordinate.column() >= 0 && coordinate.column() < columns;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public int size() {
        return size;
    }

    public int valueAt(int row, int column) {
        return map[row][column];
    }

    public int valueAt(Coordinate coordinate) {
        return map[coordinate.row()][coordinate.column()];
    }

    public void setValueAt(int row, int column, int value) {
        this.map[row][column] = value;
    }

    public void setValueAt(Coordinate coordinate, int value) {
        this.map[coordinate.row()][coordinate.column()] = value;
    }

    public void fill(int value) {
        for (int row = 0; row < rows; row++) {
            Arrays.fill(this.map[row], value);
        }
    }
}
