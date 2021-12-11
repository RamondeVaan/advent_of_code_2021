package nl.ramondevaan.aoc2021.util;

import java.util.stream.Stream;

public record Coordinate(int row, int column) {
    public Stream<Coordinate> directNeighbors() {
        return Stream.of(
                new Coordinate(row + 1, column),
                new Coordinate(row, column + 1),
                new Coordinate(row - 1, column),
                new Coordinate(row, column - 1)
        );
    }

    public Stream<Coordinate> allNeighbors() {
        return Stream.of(
                new Coordinate(row + 1, column),
                new Coordinate(row, column + 1),
                new Coordinate(row - 1, column),
                new Coordinate(row, column - 1),
                new Coordinate(row - 1, column - 1),
                new Coordinate(row + 1, column - 1),
                new Coordinate(row - 1, column + 1),
                new Coordinate(row + 1, column + 1)
        );
    }

    public static Coordinate of(int row, int column) {
        return new Coordinate(row, column);
    }
}
