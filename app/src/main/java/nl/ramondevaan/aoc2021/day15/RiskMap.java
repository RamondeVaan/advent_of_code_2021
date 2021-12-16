package nl.ramondevaan.aoc2021.day15;

import nl.ramondevaan.aoc2021.util.Coordinate;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RiskMap {

    private final int[][] map;
    private final int rows;
    private final int columns;
    private final int size;

    public RiskMap(Stream<IntStream> stream) {
        this.map = stream.map(IntStream::toArray).toArray(int[][]::new);
        this.rows = this.map.length;
        this.columns = this.rows == 0 ? 0 : this.map[0].length;
        this.size = this.rows * this.columns;
        if (Arrays.stream(this.map, 1, rows).anyMatch(row -> row.length != columns)) {
            throw new IllegalStateException();
        }
    }

    private RiskMap(int[][] map, int rows, int columns) {
        this.map = map;
        this.rows = rows;
        this.columns = columns;
        this.size = this.rows * this.columns;
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

    public static Builder builder(int rows, int columns) {
        return new Builder(rows, columns);
    }

    public static class Builder {
        private final int[][] map;
        private final int rows;
        private final int columns;

        private Builder(int rows, int columns) {
            this.map = new int[rows][columns];
            this.rows = rows;
            this.columns = columns;
        }

        public int valueAt(Coordinate coordinate) {
            return map[coordinate.row()][coordinate.column()];
        }

        public void setValue(Coordinate coordinate, int value) {
            map[coordinate.row()][coordinate.column()] = value;
        }

        public void fill(int value) {
            IntStream.range(0, rows).forEach(row -> Arrays.fill(map[row], Integer.MAX_VALUE));
        }

        public RiskMap build() {
            return new RiskMap(map, rows, columns);
        }
    }
}
