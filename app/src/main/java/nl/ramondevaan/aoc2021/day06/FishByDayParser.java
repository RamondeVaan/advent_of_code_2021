package nl.ramondevaan.aoc2021.day06;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.Arrays;
import java.util.List;

public record FishByDayParser(int numberOfDays) implements Parser<String, List<Long>> {

    @Override
    public List<Long> parse(String toParse) {
        long[] fishByDay = new long[numberOfDays];
        Arrays.stream(toParse.split(",")).map(Integer::parseInt).forEach(day -> fishByDay[day]++);
        return Arrays.stream(fishByDay).boxed().toList();
    }
}
