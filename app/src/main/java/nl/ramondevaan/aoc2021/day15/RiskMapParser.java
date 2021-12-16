package nl.ramondevaan.aoc2021.day15;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.List;

public class RiskMapParser implements Parser<List<String>, RiskMap> {
    @Override
    public RiskMap parse(List<String> toParse) {
        return new RiskMap(toParse.stream().map(line -> line.chars().map(character -> character - '0')));
    }
}
