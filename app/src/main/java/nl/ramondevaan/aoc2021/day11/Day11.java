package nl.ramondevaan.aoc2021.day11;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Day11 {

    private final Map<Coordinate, Integer> octopusGrid;

    public Day11(List<String> lines) {
        OctopusGridParser parser = new OctopusGridParser();
        this.octopusGrid = parser.parse(lines);
    }

    public long solve1() {
        Map<Coordinate, Integer> grid = new HashMap<>(octopusGrid);

        long flashCount = 0L;
        for (int i = 0; i < 100; i++) {
            grid = step(grid);
            flashCount += countFlashes(grid);
        }
        return flashCount;
    }

    public long solve2() {
        Map<Coordinate, Integer> grid = new HashMap<>(octopusGrid);

        for (long step = 1L; true; step++) {
            grid = step(grid);
            if (countFlashes(grid) == grid.size()) {
                return step;
            }
        }
    }

    private Map<Coordinate, Integer> step(Map<Coordinate, Integer> grid) {
        Map<Coordinate, Integer> next = new HashMap<>(grid);
        Consumer<Coordinate> increment = key -> next.computeIfPresent(key, (coordinate, integer) -> integer + 1);
        next.keySet().forEach(increment);

        Set<Coordinate> toFlash = next.entrySet().stream().filter(entry -> entry.getValue() >= 10)
                .map(Map.Entry::getKey).collect(Collectors.toUnmodifiableSet());
        Set<Coordinate> flashed = new HashSet<>();

        while (!toFlash.isEmpty()) {
            flashed.addAll(toFlash);
            toFlash = toFlash.stream()
                    .peek(coordinate -> next.put(coordinate, 0))
                    .flatMap(Coordinate::neighbors)
                    .filter(next::containsKey)
                    .filter(coordinate -> !flashed.contains(coordinate))
                    .peek(increment)
                    .filter(coordinate -> next.get(coordinate) >= 10)
                    .collect(Collectors.toUnmodifiableSet());
        }

        return next;
    }

    private static long countFlashes(Map<Coordinate, Integer> grid) {
        return grid.values().stream().filter(value -> value == 0).count();
    }
}
