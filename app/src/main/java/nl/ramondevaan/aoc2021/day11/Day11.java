package nl.ramondevaan.aoc2021.day11;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.IntMap;
import nl.ramondevaan.aoc2021.util.IntMapParser;
import nl.ramondevaan.aoc2021.util.MutableIntMap;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.function.Predicate.not;

public class Day11 {

    private final IntMap octopusGrid;

    public Day11(List<String> lines) {
        IntMapParser parser = new IntMapParser();
        this.octopusGrid = parser.parse(lines);
    }

    public long solve1() {
        MutableIntMap grid = new MutableIntMap(octopusGrid);

        long flashCount = 0L;
        for (int i = 0; i < 100; i++) {
            step(grid);
            flashCount += countFlashes(grid.values());
        }
        return flashCount;
    }

    public long solve2() {
        MutableIntMap grid = new MutableIntMap(octopusGrid);

        for (long step = 1L; true; step++) {
            step(grid);
            if (countFlashes(grid.values()) == grid.size()) {
                return step;
            }
        }
    }

    private void step(MutableIntMap grid) {
        grid.computeAll((value) -> value + 1);

        boolean[][] flashed = new boolean[grid.rows()][grid.columns()];

        Set<Coordinate> toFlash = new HashSet<>();
        for (Coordinate coordinate : grid.keys()) {
            if (grid.valueAt(coordinate) >= 10) {
                toFlash.add(coordinate);
            }
        }
        toFlash = Collections.unmodifiableSet(toFlash);

        while (!toFlash.isEmpty()) {
            toFlash.forEach(coordinate -> flashed[coordinate.row()][coordinate.column()] = true);
            toFlash = toFlash.stream()
                    .peek(coordinate -> grid.setValueAt(coordinate, 0))
                    .flatMap(Coordinate::allNeighbors)
                    .filter(grid::contains)
                    .filter(not(coordinate -> flashed[coordinate.row()][coordinate.column()]))
                    .peek(neighbor -> grid.compute(neighbor, (value) -> value + 1))
                    .filter(coordinate -> grid.valueAt(coordinate) >= 10)
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private static long countFlashes(IntStream values) {
        return values.filter(value -> value == 0).count();
    }
}
