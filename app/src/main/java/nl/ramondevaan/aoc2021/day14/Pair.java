package nl.ramondevaan.aoc2021.day14;

import java.util.stream.Stream;

public record Pair(char first, char second) {
    public Pair withFirst(char newFirst) {
        return new Pair(newFirst, second);
    }

    public Pair withSecond(char newSecond) {
        return new Pair(first, newSecond);
    }

    public Stream<Character> stream() {
        return Stream.of(first, second);
    }
}
