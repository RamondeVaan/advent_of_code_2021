package nl.ramondevaan.aoc2021.day13;

import nl.ramondevaan.aoc2021.util.Parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DotParser implements Parser<String, Dot> {

    private final static Pattern DOT_PATTERN = Pattern.compile("(?<x>\\d+),(?<y>\\d+)");

    @Override
    public Dot parse(String toParse) {
        Matcher matcher = DOT_PATTERN.matcher(toParse);
        if(matcher.matches()) {
            return new Dot(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")));
        }

        throw new IllegalArgumentException();
    }
}
