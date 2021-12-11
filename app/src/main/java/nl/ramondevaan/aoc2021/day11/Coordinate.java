package nl.ramondevaan.aoc2021.day11;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Coordinate(int row, int column) {
    public Stream<Coordinate> neighbors() {
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

    public static Stream<Coordinate> range(int xStart, int xEnd, int yStart, int yEnd) {
        return IntStream.range(xStart, xEnd).boxed()
                .flatMap(x -> IntStream.range(yStart, yEnd).mapToObj(y -> new Coordinate(x, y)));
    }

    public static Coordinate of(int row, int column) {
        return new Coordinate(row, column);
    }
}
