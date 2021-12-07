package nl.ramondevaan.aoc2021.day07;

import org.apache.commons.math3.stat.descriptive.rank.Median;

import java.util.List;
import java.util.function.LongUnaryOperator;
import java.util.stream.LongStream;

public class Day07 {

    private final List<Integer> horizontalPositions;

    public Day07(List<String> lines) {
        HorizontalPositionParser parser = new HorizontalPositionParser();
        this.horizontalPositions = parser.parse(String.join(",", lines));
    }

    public long solve1() {
        return calculate(
                LongUnaryOperator.identity(),
                (new Median()).evaluate(horizontalPositions.stream().mapToDouble(Integer::doubleValue).toArray())
        );
    }

    public long solve2() {
        return calculate(
                difference -> difference * (difference + 1) / 2,
                horizontalPositions.stream().mapToLong(Integer::longValue).average().orElseThrow()
        );
    }

    private long calculate(LongUnaryOperator fuelCost, double target) {
        return LongStream.of((long) Math.floor(target), (long) Math.ceil(target)).distinct()
                .map(positionTarget -> horizontalPositions.stream()
                        .mapToLong(position -> Math.abs(position - positionTarget))
                        .map(fuelCost)
                        .sum())
                .min().orElseThrow();
    }
}
