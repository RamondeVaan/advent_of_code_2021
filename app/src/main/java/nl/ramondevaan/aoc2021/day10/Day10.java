package nl.ramondevaan.aoc2021.day10;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day10 {

    private final Map<Character, Character> legalPairs;
    private final List<String> lines;

    public Day10(List<String> lines) {
        this.legalPairs = Map.of(
                '(', ')',
                '[', ']',
                '{', '}',
                '<', '>'
        );
        this.lines = lines;
    }

    public long solve1() {
        Map<Character, Integer> points = Map.of(
                ')', 3,
                ']', 57,
                '}', 1197,
                '>', 25137
        );
        return lines.stream().flatMap(line -> solve(line, true)).mapToLong(points::get).sum();
    }

    public long solve2() {
        Map<Character, Integer> points = Map.of(
                ')', 1,
                ']', 2,
                '}', 3,
                '>', 4
        );
        long[] scores = lines.stream().mapToLong(line -> solve(line, false).mapToLong(points::get).reduce(
                0L,
                (left, right) -> left * 5 + right
        )).filter(value -> value > 0).sorted().toArray();
        return scores[scores.length / 2];
    }

    private Stream<Character> solve(String line, boolean returnIllegal) {
        ArrayDeque<Character> arrayDeque = new ArrayDeque<>();

        for (char character : line.toCharArray()) {
            Character close = legalPairs.get(character);
            if (close != null) {
                arrayDeque.push(close);
            } else {
                char last = arrayDeque.pop();
                if (last != character) {
                    return returnIllegal ? Stream.of(character) : Stream.empty();
                }
            }
        }

        return returnIllegal ? Stream.empty() : arrayDeque.stream();
    }
}
