package nl.ramondevaan.aoc2021.day09;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Coordinate(int x, int y) {
    public Stream<Coordinate> neighbors() {
        return Stream.of(
                new Coordinate(x + 1, y),
                new Coordinate(x, y + 1),
                new Coordinate(x - 1, y),
                new Coordinate(x, y - 1)
        );
    }

    public static Stream<Coordinate> range(int xStart, int xEnd, int yStart, int yEnd) {
        return IntStream.range(xStart, xEnd).boxed()
                .flatMap(x -> IntStream.range(yStart, yEnd).mapToObj(y -> new Coordinate(x, y)));
    }

    public static Coordinate of(int x, int y) {
        return new Coordinate(x, y);
    }
}
