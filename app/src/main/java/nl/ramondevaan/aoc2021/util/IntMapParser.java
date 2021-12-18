package nl.ramondevaan.aoc2021.util;

import java.util.List;

public class IntMapParser implements Parser<List<String>, IntMap> {
    @Override
    public IntMap parse(List<String> toParse) {
        return new IntMap(toParse.stream().map(line -> line.chars().map(character -> character - '0')));
    }
}
