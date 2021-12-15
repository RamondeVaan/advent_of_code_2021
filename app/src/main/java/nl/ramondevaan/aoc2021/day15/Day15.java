package nl.ramondevaan.aoc2021.day15;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.CoordinateIntegerMapParser;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;
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
        return minRiskMap(start, end, this.riskMap::containsKey, this.riskMap::get);
    }

    public long solve2() {
        Predicate<Coordinate> rangeCheck = coordinate -> coordinate.row() >= 0 && coordinate.row() < rows * 5
                && coordinate.column() >= 0 && coordinate.column() < columns * 5;
        Function<Coordinate, Integer> riskFunction = coordinate -> {
            int value = this.riskMap.get(new Coordinate(coordinate.row() % rows, coordinate.column() % columns));
            return (value - 1 + coordinate.row() / rows + coordinate.column() / columns) % 9 + 1;
        };
        Coordinate end = new Coordinate(rows * 5 - 1, columns * 5 - 1);
        return minRiskMap(start, end, rangeCheck, riskFunction);
    }

    private static long minRiskMap(Coordinate source, Coordinate end, Predicate<Coordinate> inRangePredicate, Function<Coordinate, Integer> riskFunction) {
        Set<Coordinate> handled = new HashSet<>();
        TreeSet<ImmutablePair<Coordinate, Long>> set = new TreeSet<>((p1, p2) -> p1.right > p2.right ? 1 : -1);
        set.add(ImmutablePair.of(source, 0L));
        handled.add(source);

        while (!set.isEmpty()) {
            ImmutablePair<Coordinate, Long> current = Objects.requireNonNull(set.pollFirst());
            for (Coordinate neighbor : current.left.directNeighbors()
                    .filter(not(handled::contains))
                    .filter(inRangePredicate)
                    .collect(Collectors.toSet())) {
                long risk = current.right + riskFunction.apply(neighbor);
                if (neighbor.equals(end)) {
                    return risk;
                }
                handled.add(neighbor);
                set.add(ImmutablePair.of(neighbor, risk));
            }
        }

        throw new IllegalStateException();
    }
}
