package nl.ramondevaan.aoc2021.day15;

import nl.ramondevaan.aoc2021.util.Coordinate;
import nl.ramondevaan.aoc2021.util.IntMap;
import nl.ramondevaan.aoc2021.util.IntMapParser;
import nl.ramondevaan.aoc2021.util.MutableIntMap;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparingInt;

public class Day15 {

    private final IntMap riskMap;
    private final Coordinate start;

    public Day15(List<String> lines) {
        IntMapParser parser = new IntMapParser();
        this.riskMap = parser.parse(lines);
        this.start = new Coordinate(0, 0);
    }

    public long solve1() {
        Coordinate end = new Coordinate(riskMap.rows() - 1, riskMap.columns() - 1);
        return solve(start, end, riskMap);
    }

    public long solve2() {
        Stream<IntStream> extendedValueStream = IntStream.range(0, riskMap.rows() * 5).boxed()
                .map(row -> IntStream.range(0, riskMap.columns() * 5).map(column -> extendedValueAt(row, column)));
        IntMap extendedRiskMap = new IntMap(extendedValueStream);

        Coordinate end = new Coordinate(extendedRiskMap.rows() - 1, extendedRiskMap.columns() - 1);
        return solve(start, end, extendedRiskMap);
    }

    private int extendedValueAt(int row, int column) {
        int mappedValue = riskMap.valueAt(row % riskMap.rows(), column % riskMap.columns());
        int offset = row / riskMap.rows() + column / riskMap.columns();
        return (mappedValue - 1 + offset) % 9 + 1;
    }

    private static int solve(Coordinate source, Coordinate end, IntMap riskMap) {
        MutableIntMap builder = new MutableIntMap(riskMap.rows(), riskMap.columns());
        builder.fill(Integer.MAX_VALUE);
        PriorityQueue<Coordinate> queue = new PriorityQueue<>(riskMap.size(), comparingInt(builder::valueAt));
        builder.setValueAt(source, 0);
        queue.add(source);

        Coordinate coordinate;
        while ((coordinate = queue.poll()) != null) {
            if (coordinate.equals(end)) {
                return builder.valueAt(end);
            }
            int currentRisk = builder.valueAt(coordinate);
            coordinate.directNeighbors().filter(riskMap::contains)
                    .filter(c -> builder.valueAt(c) == Integer.MAX_VALUE)
                    .forEach(neighbor -> {
                        builder.setValueAt(neighbor, currentRisk + riskMap.valueAt(neighbor));
                        queue.add(neighbor);
                    });
        }

        throw new IllegalStateException();
    }
}
