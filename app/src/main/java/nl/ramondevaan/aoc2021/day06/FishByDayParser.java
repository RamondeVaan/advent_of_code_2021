package nl.ramondevaan.aoc2021.day06;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FishByDayParser implements Parser<String, Map<Integer, Long>> {

    @Override
    public Map<Integer, Long> parse(String toParse) {
        return Arrays.stream(toParse.split(",")).map(Integer::parseInt)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
