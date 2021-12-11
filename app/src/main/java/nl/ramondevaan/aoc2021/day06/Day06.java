package nl.ramondevaan.aoc2021.day06;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day06 {

    private final Map<Integer, Long> initialFishByDay;

    public Day06(List<String> lines) {
        FishByDayParser parser = new FishByDayParser();
        this.initialFishByDay = parser.parse(String.join(",", lines));
    }

    public long solve1() {
        return progressAndCount(80);
    }

    public long solve2() {
        return progressAndCount(256);
    }

    private long progressAndCount(int days) {
        long[] currentFishByDay = new long[9];
        initialFishByDay.forEach((index, numberOfFish) -> currentFishByDay[index] = numberOfFish);

        for (int day = 0; day < days; day++) {
            currentFishByDay[(day + 7) % currentFishByDay.length] += currentFishByDay[day % currentFishByDay.length];
        }

        return Arrays.stream(currentFishByDay).sum();
    }
}
