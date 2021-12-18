package nl.ramondevaan.aoc2021.day09;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.IntMap;
import nl.ramondevaan.aoc2021.util.IntMapEntry;
import nl.ramondevaan.aoc2021.util.IntMapParser;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class Day09 {

    private final IntMap heightMap;

    public Day09(List<String> lines) {
        IntMapParser parser = new IntMapParser();
        this.heightMap = parser.parse(lines);
    }

    public long solve1() {
        return getLocalMinima()
                .mapToLong(pair -> pair.value() + 1L)
                .sum();
    }

    public long solve2() {
        return getLocalMinima()
                .map(IntMapEntry::coordinate)
                .map(this::getBasin)
                .sorted(Comparator.<Collection<Coordinate>>comparingInt(Collection::size).reversed())
                .limit(3)
                .mapToLong(Collection::size)
                .reduce(1L, (left, right) -> left * right);
    }

    private Stream<IntMapEntry> getLocalMinima() {
        return heightMap.entries()
                .filter(entry -> entry.coordinate().directNeighbors()
                        .filter(heightMap::contains)
                        .map(heightMap::valueAt)
                        .allMatch(value -> value > entry.value()));
    }

    private Collection<Coordinate> getBasin(Coordinate origin) {
        Set<Coordinate> basin = new HashSet<>(Set.of(origin));
        Set<Coordinate> toCheck = validNeighbors(origin, basin).collect(toUnmodifiableSet());

        while (!toCheck.isEmpty()) {
            toCheck = toCheck.stream()
                    .map(coord -> new Tuple(coord, validNeighbors(coord, basin).collect(toUnmodifiableSet())))
                    .filter(tuple -> tuple.neighbors.stream().allMatch(largerThan(tuple.coordinate)))
                    .peek(tuple -> basin.add(tuple.coordinate))
                    .flatMap(tuple -> tuple.neighbors.stream())
                    .collect(toUnmodifiableSet());
        }

        return basin;
    }

    private Predicate<Coordinate> largerThan(Coordinate coordinate) {
        return neighbor -> heightMap.valueAt(neighbor) >= heightMap.valueAt(coordinate);
    }

    private Stream<Coordinate> validNeighbors(Coordinate coordinate, Set<Coordinate> basin) {
        return coordinate.directNeighbors().filter(not(basin::contains))
                .filter(heightMap::contains)
                .filter(neighbor -> heightMap.valueAt(neighbor) < 9);
    }

    private record Tuple(Coordinate coordinate, Set<Coordinate> neighbors) {
    }
}
