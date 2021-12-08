package nl.ramondevaan.aoc2021.day08;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.stream.Collectors;

public class DigitParser implements Parser<String, Digit> {
    @Override
    public Digit parse(String toParse) {
        return new Digit(toParse.chars().mapToObj(c -> (char) c).collect(Collectors.toUnmodifiableSet()));
    }
}
