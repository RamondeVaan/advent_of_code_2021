package nl.ramondevaan.aoc2021.day09;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
import java.util.stream.Stream;

public class Day09 {

    private final HeightMap heightMap;

    public Day09(List<String> lines) {
        HeightMapParser parser = new HeightMapParser();
        this.heightMap = parser.parse(lines);
    }

    public long solve1() {
        return getLocalMinima()
                .mapToLong(pair -> pair.right + 1L)
                .sum();
    }

    public long solve2() {
        return getLocalMinima().map(ImmutablePair::getLeft)
                .map(this::getBasin)
                .sorted(Comparator.<Collection<Coordinate>>comparingInt(Collection::size).reversed())
                .limit(3)
                .mapToLong(Collection::size)
                .reduce(1L, (left, right) -> left * right);
    }

    private Stream<ImmutablePair<Coordinate, Integer>> getLocalMinima() {
        return Coordinate.range(0, heightMap.getWidth(), 0, heightMap.getHeight())
                .map(coordinate -> ImmutablePair.of(coordinate, heightMap.valueAt(coordinate)))
                .filter(pair -> pair.left.neighbors().filter(heightMap::isInRange).map(heightMap::valueAt)
                        .allMatch(neighborValue -> neighborValue > pair.right));
    }

    private Collection<Coordinate> getBasin(Coordinate origin) {
        Set<Coordinate> basin = new HashSet<>();
        Set<Coordinate> temp = new HashSet<>();
        Set<Coordinate> toCheck = new HashSet<>(origin.neighbors().filter(heightMap::isInRange).toList());
        basin.add(origin);

        while (!toCheck.isEmpty()) {
            for (Coordinate coordinate : toCheck) {
                int value = heightMap.valueAt(coordinate);
                if (value >= 9) {
                    continue;
                }
                List<Coordinate> neighbors = coordinate.neighbors()
                        .filter(heightMap::isInRange)
                        .filter(neighbor -> !basin.contains(neighbor))
                        .toList();
                if (neighbors.stream().allMatch(neighbor -> heightMap.valueAt(neighbor) >= value)) {
                    basin.add(coordinate);
                    temp.addAll(neighbors);
                }
            }

            toCheck = temp;
            temp = new HashSet<>();
        }

        return basin;
    }
}
