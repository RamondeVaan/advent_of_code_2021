package nl.ramondevaan.aoc2021.day01;

import java.util.List;

public class Day01 {

    private final int[] depths;

    public Day01(List<String> lines) {
        this.depths = lines.stream().mapToInt(Integer::parseInt).toArray();
    }

    public long solve1() {
        return countIncreases(1);
    }

    public long solve2() {
        return countIncreases(3);
    }

    private long countIncreases(int window) {
        long count = 0;

        for (int out = 0, in = window; in < depths.length; out++, in++) {
            if (depths[in] > depths[out]) {
                count++;
            }
        }

        return count;
    }
}
