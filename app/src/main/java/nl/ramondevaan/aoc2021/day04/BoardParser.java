package nl.ramondevaan.aoc2021.day04;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.*;

public class BoardParser implements Parser<List<String>, Board> {
    @Override
    public Board parse(List<String> toParse) {
        int[] values = toParse.stream()
                .flatMap(line -> Arrays.stream(line.trim().split("\\s+")))
                .mapToInt(Integer::parseInt).toArray();
        return new Board(values, toParse.size());
    }
}
