package nl.ramondevaan.aoc2021.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntMap {

    private final int[][] map;
    private final int rows;
    private final int columns;
    private final int size;
    private final List<Coordinate> keys;

    public IntMap(Stream<IntStream> stream) {
        this.map = stream.map(IntStream::toArray).toArray(int[][]::new);
        this.rows = this.map.length;
        this.columns = this.rows == 0 ? 0 : this.map[0].length;
        this.size = this.rows * this.columns;
        this.keys = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                this.keys.add(new Coordinate(row, column));
            }
        }
        if (Arrays.stream(this.map, 1, rows).anyMatch(row -> row.length != columns)) {
            throw new IllegalStateException();
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

    public void copyInto(int row, int[] destination) {
        System.arraycopy(this.map[row], 0, destination, 0, columns);
    }
}
