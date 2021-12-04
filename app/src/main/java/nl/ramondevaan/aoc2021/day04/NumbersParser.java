package nl.ramondevaan.aoc2021.day04;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.Arrays;
import java.util.List;

public class NumbersParser implements Parser<String, List<Integer>> {
    @Override
    public List<Integer> parse(String toParse) {
        return Arrays.stream(toParse.split(",")).map(Integer::parseInt).toList();
    }
}
