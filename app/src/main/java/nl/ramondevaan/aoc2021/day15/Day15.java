package nl.ramondevaan.aoc2021.day15;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.CoordinateIntegerMapParser;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class Day15 {

    private final Map<Coordinate, Integer> riskMap;
    private final Coordinate start;
    private final int rows;
    private final int columns;

    public Day15(List<String> lines) {
        CoordinateIntegerMapParser parser = new CoordinateIntegerMapParser();
        this.riskMap = parser.parse(lines);
        this.rows = this.riskMap.keySet().stream().mapToInt(Coordinate::row).max().orElseThrow() + 1;
        this.columns = this.riskMap.keySet().stream().mapToInt(Coordinate::column).max().orElseThrow() + 1;
        this.start = new Coordinate(0, 0);
    }

    public long solve1() {
        Coordinate end = new Coordinate(rows - 1, columns - 1);
        return solve(start, end, this.riskMap::containsKey, this.riskMap::get);
    }

    public long solve2() {
        Coordinate end = new Coordinate(rows * 5 - 1, columns * 5 - 1);
        return solve(start, end, this::rangeCheckExtended, this::riskAtExtended);
    }

    private boolean rangeCheckExtended(Coordinate coordinate) {
        return coordinate.row() >= 0 && coordinate.row() < rows * 5
                && coordinate.column() >= 0 && coordinate.column() < columns * 5;
    }

    private int riskAtExtended(Coordinate coordinate) {
        int value = this.riskMap.get(new Coordinate(coordinate.row() % rows, coordinate.column() % columns));
        return (value - 1 + coordinate.row() / rows + coordinate.column() / columns) % 9 + 1;
    }

    private static long solve(Coordinate source, Coordinate end, Predicate<Coordinate> isInRange, Function<Coordinate, Integer> riskFunction) {
        Set<Coordinate> handled = new HashSet<>();
        TreeMap<Long, Set<Coordinate>> map = new TreeMap<>();
        map.put(0L, new HashSet<>(Set.of(source)));
        handled.add(source);

        while (!map.isEmpty()) {
            Map.Entry<Long, Set<Coordinate>> entry = map.pollFirstEntry();
            for (Coordinate coordinate : entry.getValue()) {
                Set<Coordinate> neighbors = coordinate.directNeighbors().filter(isInRange)
                        .filter(not(handled::contains)).collect(Collectors.toSet());
                for (Coordinate neighbor : neighbors) {
                    long risk = entry.getKey() + riskFunction.apply(neighbor);
                    if (neighbor.equals(end)) {
                        return risk;
                    }
                    handled.add(neighbor);
                    map.compute(risk, addOrCreate(neighbor));
                }
            }
        }

        throw new IllegalStateException();
    }

    private static <K, V> BiFunction<? super K, ? super Set<V>, ? extends Set<V>> addOrCreate(V value) {
        return (key, currentSet) -> {
            Set<V> set = currentSet == null ? new HashSet<>() : currentSet;
            set.add(value);
            return set;
        };
    }
}
