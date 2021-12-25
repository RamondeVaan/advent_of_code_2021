package nl.ramondevaan.aoc2021.day25;

import nl.ramondevaan.aoc2021.util.IntMap;
import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;

public class RegionParser implements Parser<List<String>, IntMap> {
    @Override
    public IntMap parse(List<String> toParse) {
        return new IntMap(toParse.stream().map(line -> line.chars().map(character -> character == '.' ? 0 : character)));
    }
}
