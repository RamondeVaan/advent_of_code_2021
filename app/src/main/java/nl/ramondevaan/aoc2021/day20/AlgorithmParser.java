package nl.ramondevaan.aoc2021.day20;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;

public class AlgorithmParser implements Parser<String, List<Boolean>> {
    @Override
    public List<Boolean> parse(String toParse) {
        return toParse.chars().mapToObj(character -> character == '#').toList();
    }
}
