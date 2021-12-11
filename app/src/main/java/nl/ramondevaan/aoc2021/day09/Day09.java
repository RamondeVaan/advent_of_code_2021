package nl.ramondevaan.aoc2021.day09;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.CoordinateIntegerMapParser;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toUnmodifiableSet;

public class Day09 {

    private final Map<Coordinate, Integer> heightMap;

    public Day09(List<String> lines) {
        CoordinateIntegerMapParser parser = new CoordinateIntegerMapParser();
        this.heightMap = parser.parse(lines);
    }

    public long solve1() {
        return getLocalMinima()
                .mapToLong(pair -> pair.getValue() + 1L)
                .sum();
    }

    public long solve2() {
        return getLocalMinima()
                .map(Map.Entry::getKey)
                .map(this::getBasin)
                .sorted(Comparator.<Collection<Coordinate>>comparingInt(Collection::size).reversed())
                .limit(3)
                .mapToLong(Collection::size)
                .reduce(1L, (left, right) -> left * right);
    }

    private Stream<Map.Entry<Coordinate, Integer>> getLocalMinima() {
        return heightMap.entrySet().stream()
                .filter(entry -> entry.getKey().directNeighbors()
                        .filter(heightMap::containsKey)
                        .map(heightMap::get)
                        .allMatch(value -> value > entry.getValue()))
                .map(entry -> ImmutablePair.of(entry.getKey(), entry.getValue()));
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
        return neighbor -> heightMap.get(neighbor) >= heightMap.get(coordinate);
    }

    private Stream<Coordinate> validNeighbors(Coordinate coordinate, Set<Coordinate> basin) {
        return coordinate.directNeighbors().filter(not(basin::contains))
                .filter(neighbor -> Optional.ofNullable(heightMap.get(neighbor))
                        .filter(value -> value < 9).isPresent());
    }

    private record Tuple(Coordinate coordinate, Set<Coordinate> neighbors) {
    }
}
